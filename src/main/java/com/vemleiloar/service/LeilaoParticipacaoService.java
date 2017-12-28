package com.vemleiloar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vemleiloar.model.Chat;
import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.LeilaoParticipacao;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.repository.ChatRepository;
import com.vemleiloar.repository.LeilaoParticipacaoRepository;
import com.vemleiloar.repository.UsuarioRepository;

@Service
public class LeilaoParticipacaoService {

	@Autowired
	private LeilaoParticipacaoRepository repository;

	@Autowired
	private UsuarioRepository repositoryUsuario;
	
	@Autowired
	private ChatRepository repositoryChat;
	
	public Integer quantPartLeilao(Leilao leilao) {
		return repository.quantPartLeilao(leilao.getId());
	}
	
	public LeilaoParticipacao findByUserAndLeilao(Usuario usuario, Leilao leilao) {
		return repository.findByUserAndLeilao(usuario.getId(), leilao.getId());
	}

	public List<LeilaoParticipacao> findByLeilao(Leilao leilao) {
		return repository.findByLeilao(leilao);
	}

	public List<LeilaoParticipacao> buscaParticipacoes(Leilao leilao){

		List<LeilaoParticipacao> participacoes = findByLeilao(leilao);
		boolean chatSituacao = false;

		for (LeilaoParticipacao lp : participacoes) {
			Usuario usuario = repositoryUsuario.findOne(lp.getUsuario().getId());
			lp.setUsuario(usuario);
			
			if (!chatSituacao) {
				
				Chat chat = repositoryChat.findChat(leilao, usuario);
				
				if ( chat == null ) {
					
					lp.setChatAguardandoCriacao(true);
					lp.setChatCriado(false);
					chatSituacao = true;
					
				} else if ( chat.getSituacao().equals("1") ) {
					
					lp.setChatAguardandoCriacao(false);
					lp.setChatCriado(true);
					chatSituacao = true;
					
				}
				
			}
			
		}

		return participacoes;
	}

}