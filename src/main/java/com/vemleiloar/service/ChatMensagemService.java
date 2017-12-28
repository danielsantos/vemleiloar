package com.vemleiloar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vemleiloar.model.Chat;
import com.vemleiloar.model.ChatMensagem;
import com.vemleiloar.repository.ChatMensagemRepository;

@Service
public class ChatMensagemService {

	@Autowired
	private ChatMensagemRepository repository;
	
	public List<ChatMensagem> findAll() {
		return repository.findAll(); 
	}
	
	public ChatMensagem findOne(Long id) {
		return repository.findOne(id);
	}
	
	public ChatMensagem save(ChatMensagem chatMensagem) {
		return repository.saveAndFlush(chatMensagem);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}

	public List<ChatMensagem> findByChat(Chat chat) {
		return repository.findByChat(chat);
	}
	
}