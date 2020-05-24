package io.github.maksymsan.persistentwebapp1.api;

import io.github.maksymsan.persistentwebapp1.model.NamedObject;

public interface CrudService {
    NamedObject getNamedObject(String objectId);
    void deleteNamedObject(String objectId);
}
