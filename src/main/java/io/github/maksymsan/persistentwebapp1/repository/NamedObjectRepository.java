package io.github.maksymsan.persistentwebapp1.repository;

import io.github.maksymsan.persistentwebapp1.model.NamedObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NamedObjectRepository extends JpaRepository<NamedObject, String> {
}
