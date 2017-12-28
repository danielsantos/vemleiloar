package com.vemleiloar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vemleiloar.model.LeilaoImagem;
import com.vemleiloar.repository.LeilaoImagemRepository;

@Service
public class LeilaoImagemService {
	
	@Autowired
	private LeilaoImagemRepository repository;

	public LeilaoImagem save(LeilaoImagem imagem) {
		return repository.saveAndFlush(imagem);
	}	

}
