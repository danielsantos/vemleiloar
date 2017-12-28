package com.vemleiloar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.LeilaoParticipacao;

public interface LeilaoParticipacaoRepository extends JpaRepository<LeilaoParticipacao, Long> {


    @Query(value="SELECT l.* FROM leilao_participacao l WHERE l.leilao_id = :idLeilao ORDER BY l.lance DESC LIMIT 1", nativeQuery = true)
    LeilaoParticipacao findByLeilao(@Param("idLeilao") Long idLeilao);

    @Query(value="SELECT l.* FROM leilao_participacao l WHERE l.leilao_id = :idLeilao AND l.usuario_id = :idUsuario ORDER BY l.lance DESC LIMIT 1",
                 nativeQuery = true)
    LeilaoParticipacao findByUserAndLeilao(@Param("idUsuario") Long idUsuario, @Param("idLeilao") Long idLeilao);

    @Query(value="SELECT COUNT(1) FROM leilao_participacao l WHERE l.leilao_id = :idLeilao", nativeQuery = true)
    Integer quantPartLeilao(@Param("idLeilao") Long idLeilao);

    @Query("select l from LeilaoParticipacao l where l.leilao = :leilao ORDER BY l.dataCadastro DESC")
    List<LeilaoParticipacao> findByLeilao(@Param("leilao") Leilao leilao);

}
