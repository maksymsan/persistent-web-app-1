package io.github.maksymsan.persistentwebapp1.api;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void postNamedObjectFile(MultipartFile namedObjectFile);
}
