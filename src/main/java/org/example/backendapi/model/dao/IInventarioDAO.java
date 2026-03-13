package org.example.backendapi.model.dao;

import org.example.backendapi.model.entities.Inventario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IInventarioDAO extends CrudRepository<Inventario, String> {
    Optional<Inventario> findInventarioById(String id);

    List<Inventario> findInventarioByNombreObjeto(String nombreObjeto);
}
