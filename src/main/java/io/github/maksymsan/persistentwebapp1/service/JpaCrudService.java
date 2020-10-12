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

import io.github.maksymsan.persistentwebapp1.api.CrudService;
import io.github.maksymsan.persistentwebapp1.exception.ObjectNotFoundException;
import io.github.maksymsan.persistentwebapp1.model.NamedObject;
import io.github.maksymsan.persistentwebapp1.repository.NamedObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class JpaCrudService implements CrudService {

    NamedObjectRepository namedObjectRepository;

    @Autowired
    public JpaCrudService(NamedObjectRepository namedObjectRepository) {
        this.namedObjectRepository = namedObjectRepository;
    }

    @Override
    public NamedObject getNamedObject(String objectId) {
        NamedObject namedObject = new NamedObject();
        namedObject.setPrimaryKey(objectId);
        namedObject.setName("Name");
        namedObject.setDescription("Description");
        namedObject.setUpdatedTimestamp(LocalDateTime.now());
        return namedObjectRepository.findById(objectId).orElseThrow(() -> new ObjectNotFoundException(
                "Object not found"));
    }

    @Override
    public void deleteNamedObject(String objectId) {
        try {
            namedObjectRepository.deleteById(objectId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Object not found", e);
        }
    }

}
