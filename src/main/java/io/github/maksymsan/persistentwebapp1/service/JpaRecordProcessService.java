/*
 *    Copyright 2020 Maksym Sanzharov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package io.github.maksymsan.persistentwebapp1.service;

import io.github.maksymsan.persistentwebapp1.api.RecordProcessService;
import io.github.maksymsan.persistentwebapp1.exception.FileInvalidLineException;
import io.github.maksymsan.persistentwebapp1.model.NamedObject;
import io.github.maksymsan.persistentwebapp1.repository.NamedObjectRepository;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;

@Service
@Transactional
public class JpaRecordProcessService implements RecordProcessService {
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final NamedObjectRepository namedObjectRepository;

    @Autowired
    public JpaRecordProcessService(NamedObjectRepository namedObjectRepository) {
        this.namedObjectRepository = namedObjectRepository;
    }

    public void process(Iterable<CSVRecord> records) {
        HashSet<String> keySet = new HashSet<>();
        int lineNumber = 1;
        for (CSVRecord record : records) {
            lineNumber++;
            log.debug("Input record: {}", record);
            if (!record.isConsistent()) {
                throw new FileInvalidLineException(String.format("Line %d has invalid number of columns",
                        lineNumber));
            }

            final String primaryKeyString = record.get(CSVFormatConstants.PRIMARY_KEY);
            if (primaryKeyString == null || primaryKeyString.isBlank()) {
                throw new FileInvalidLineException(String.format("Primary key is blank at line %d",
                        lineNumber));
            }
            if (keySet.contains(primaryKeyString)) {
                throw new FileInvalidLineException(String.format("Duplicate primary key at line %d",
                        lineNumber));
            }
            keySet.add(primaryKeyString);
            final String nameString = record.get(CSVFormatConstants.NAME);
            final String descriptionString = record.get(CSVFormatConstants.DESCRIPTION);
            final String timeStampString = record.get(CSVFormatConstants.UPDATED_TIMESTAMP);
            NamedObject namedObject = namedObjectRepository.findById(primaryKeyString).orElse(new NamedObject());
            if (namedObject.getPrimaryKey() == null) {
                namedObject.setPrimaryKey(primaryKeyString);
            }
            namedObject.setName(nameString);
            namedObject.setDescription(descriptionString);
            try {
                namedObject.setUpdatedTimestamp(LocalDateTime.parse(timeStampString));
            } catch (DateTimeParseException e) {
                throw new FileInvalidLineException(String.format("Invalid timestamp in the line %d: %s",
                        lineNumber, timeStampString), e);
            }
            log.debug("Saving object: {}", namedObject);
            namedObjectRepository.save(namedObject);
        }
    }
}
