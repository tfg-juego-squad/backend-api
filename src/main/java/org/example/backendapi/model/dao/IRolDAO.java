package org.example.backendapi.model.dao;

import org.example.backendapi.model.entities.Rol;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IRolDAO extends CrudRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);
}
