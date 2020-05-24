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
