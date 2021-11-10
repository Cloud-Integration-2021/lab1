package fr.lacazethomas.lab1.service;

import fr.lacazethomas.lab1.controller.dto.BaseDTO;

import java.util.List;
import java.util.Optional;

public interface CrudService<T extends BaseDTO> {

    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T dto);

    void delete(Long id);

    T update(Long id, T dto);
}