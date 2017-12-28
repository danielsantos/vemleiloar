package com.vemleiloar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vemleiloar.model.Chat;
import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.repository.ChatRepository;

@Service
public class ChatService {

	@Autowired
	private ChatRepository repository;
	
	public List<Chat> findAll() {
		return repository.findAll(); 
	}
	
	public Chat findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Chat findChat(Leilao leilao, Usuario usuario) {
		return repository.findChat(leilao, usuario);
	}
	
	public Chat save(Chat chat) {
		return repository.saveAndFlush(chat);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}
	
}