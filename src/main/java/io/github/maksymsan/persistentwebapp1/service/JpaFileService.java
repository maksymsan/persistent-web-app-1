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
import io.github.maksymsan.persistentwebapp1.api.RecordProcessService;
import io.github.maksymsan.persistentwebapp1.exception.FileInvalidHeaderException;
import io.github.maksymsan.persistentwebapp1.exception.FileReadException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class JpaFileService implements FileService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final List<String> csvHeaderList = Arrays.asList(CSVFormatConstants.HEADERS);

    private final RecordProcessService recordProcessService;

    @Autowired
    public JpaFileService(RecordProcessService recordProcessService) {
        this.recordProcessService = recordProcessService;
    }

    @Override
    public void postNamedObjectFile(InputStream namedObjectFile) {
        try (InputStreamReader isr = new InputStreamReader(namedObjectFile);
             BufferedReader in = new BufferedReader(isr)) {

            CSVFormat csvFormat = CSVFormat.RFC4180.withFirstRecordAsHeader()
                    .withAllowDuplicateHeaderNames(false).withAllowMissingColumnNames(false).withSkipHeaderRecord(true);
            CSVParser parser;

            try {
                parser = csvFormat.parse(in);

                if (!csvHeaderList.equals(parser.getHeaderNames())) {
                    throw new IllegalArgumentException("Header does not correspond to the defined format");
                }
            } catch (IllegalArgumentException e) {
                throw new FileInvalidHeaderException("File header is not valid", e);
            }

            recordProcessService.process(parser);
        } catch (IOException e) {
            log.error("Error on parsing CSV file", e);
            throw new FileReadException(e);
        }
    }
}
