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
