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

package io.github.maksymsan.persistentwebapp1.controller;

import io.github.maksymsan.persistentwebapp1.api.CrudService;
import io.github.maksymsan.persistentwebapp1.api.FileService;
import io.github.maksymsan.persistentwebapp1.model.NamedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudController {

    private final CrudService crudService;

    @Autowired
    public CrudController(CrudService crudService, FileService fileService) {
        this.crudService = crudService;
    }

    @GetMapping(value="/namedObject/{objectId}")
    public NamedObject getNamedObject(@PathVariable String objectId) {
            return crudService.getNamedObject(objectId);
    }

    @DeleteMapping(value="/namedObject/{objectId}")
    public void deleteNamedObject(@PathVariable String objectId) {
        crudService.deleteNamedObject(objectId);
    }

}
