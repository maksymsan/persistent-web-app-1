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

import io.github.maksymsan.persistentwebapp1.api.FileService;
import io.github.maksymsan.persistentwebapp1.exception.FileInvalidHeaderException;
import io.github.maksymsan.persistentwebapp1.exception.FileInvalidLineException;
import io.github.maksymsan.persistentwebapp1.exception.FileReadException;
import io.github.maksymsan.persistentwebapp1.model.NamedObject;
import io.github.maksymsan.persistentwebapp1.repository.NamedObjectRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class JpaFileService implements FileService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final String[] HEADERS = {"PRIMARY_KEY","NAME","DESCRIPTION","UPDATED_TIMESTAMP"};
    private final List<String> headerList = Arrays.asList(HEADERS);

    private final NamedObjectRepository namedObjectRepository;

    @Autowired
    public JpaFileService(NamedObjectRepository namedObjectRepository) {
        this.namedObjectRepository = namedObjectRepository;
    }

    @Override
    public void postNamedObjectFile(InputStream namedObjectFile) {
        HashSet<String> keySet = new HashSet<>();
        try (InputStreamReader isr = new InputStreamReader(namedObjectFile);
             BufferedReader in = new BufferedReader(isr)) {

            CSVFormat csvFormat = CSVFormat.RFC4180.withFirstRecordAsHeader()
                    .withAllowDuplicateHeaderNames(false).withAllowMissingColumnNames(false).withSkipHeaderRecord(true);
            CSVParser parser;

            try {
                parser = csvFormat.parse(in);

                if (!headerList.equals(parser.getHeaderNames())) {
                    throw new IllegalArgumentException("Header does not correspond to the defined format");
                }
            } catch (IllegalArgumentException e) {
                throw new FileInvalidHeaderException("File header is not valid", e);
            }

            int lineNumber = 1;
            for (CSVRecord record : parser) {
                lineNumber++;
                if (!record.isConsistent()) {
                    throw new FileInvalidLineException(String.format("Line %d has invalid number of columns",
                            lineNumber));
                }

                final String primaryKeyString = record.get(HEADERS[0]);
                if (primaryKeyString == null || primaryKeyString.isBlank()) {
                    throw new FileInvalidLineException(String.format("Primary key is blank at line %d",
                            lineNumber));
                }
                if (keySet.contains(primaryKeyString)) {
                    throw new FileInvalidLineException(String.format("Duplicate primary key at line %d",
                            lineNumber));
                }
                keySet.add(primaryKeyString);
                final String nameString = record.get(HEADERS[1]);
                final String descriptionString = record.get(HEADERS[2]);
                final String timeStampString = record.get(HEADERS[3]);
                NamedObject namedObject = namedObjectRepository.findById(primaryKeyString).orElse(null);
                if (namedObject == null) {
                    namedObject = new NamedObject();
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
                namedObjectRepository.save(namedObject);
            }
        } catch (IOException e) {
            log.error("Error on parsing CSV file", e);
            throw new FileReadException(e);
        }
    }
}
