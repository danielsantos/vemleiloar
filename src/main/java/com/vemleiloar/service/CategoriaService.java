package com.vemleiloar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vemleiloar.model.Categoria;
import com.vemleiloar.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public List<Categoria> findAllWithOrder() {
		return repository.findAllWithOrder(); 
	}
	
	public List<Categoria> findAll() {
		return repository.findAll(); 
	}
	
	public Categoria findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Categoria save(Categoria categoria) {
		return repository.saveAndFlush(categoria);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}
	
}