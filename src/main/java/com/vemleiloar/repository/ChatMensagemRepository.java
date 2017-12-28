package com.vemleiloar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vemleiloar.model.Chat;
import com.vemleiloar.model.ChatMensagem;

public interface ChatMensagemRepository extends JpaRepository<ChatMensagem, Long> {

	@Query("select cm from ChatMensagem cm where cm.chat = :chat")
	List<ChatMensagem> findByChat(@Param("chat") Chat chat);

}
