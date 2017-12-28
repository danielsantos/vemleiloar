package com.vemleiloar.service;

import com.vemleiloar.model.Categoria;
import com.vemleiloar.model.Estado;
import com.vemleiloar.repository.CategoriaRepository;
import com.vemleiloar.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repository;

    public List<Estado> findAll() {
        return repository.findAll();
    }

    public Estado findOne(Long id) {
        return repository.findOne(id);
    }

    public Estado save(Estado estado) {
        return repository.saveAndFlush(estado);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

}
