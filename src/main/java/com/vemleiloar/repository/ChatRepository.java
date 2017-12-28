package com.vemleiloar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vemleiloar.model.Chat;
import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.Usuario;

public interface ChatRepository extends JpaRepository<Chat, Long> {

	@Query("select c from Chat c where c.leilao = :leilao and c.usuarioComprador = :usuarioComprador")
	Chat findChat(@Param("leilao") Leilao leilao, @Param("usuarioComprador") Usuario usuarioComprador);

}
