package io.github.maksymsan.persistentwebapp1.controller;

import io.github.maksymsan.persistentwebapp1.api.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/namedObjectFile")
    void postNamedObjectFile(@RequestParam("file") MultipartFile file) {
        fileService.postNamedObjectFile(file);
    }

}
