package com.vemleiloar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.LeilaoImagem;

public interface LeilaoImagemRepository extends JpaRepository<LeilaoImagem, Long> {
	
	@Query("select li from LeilaoImagem li where li.leilao = :leilao")
	LeilaoImagem findByLeilao(@Param("leilao") Leilao leilao);

}
