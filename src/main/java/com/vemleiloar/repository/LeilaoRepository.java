package com.vemleiloar.repository;

import com.vemleiloar.model.Categoria;
import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

    @Query("select l from Leilao l where l.usuario = :usuario")
    List<Leilao> findByUser(@Param("usuario") Usuario usuario);

    @Query(value="SELECT * FROM leilao WHERE data_fechamento <= :dataAtual", nativeQuery = true)
    List<Leilao> findReadyToClose(@Param("dataAtual") Date dataAtual);

    @Query("select l from Leilao l where l.uuid = :uuid")
    Leilao findByUuid(@Param("uuid") String uuid);

    @Query("select l from Leilao l where l.categoria = :categoria")
    List<Leilao> findByFiltro(@Param("categoria") Categoria categoria);
    
    @Query("select l from Leilao l where l.status = :status")
    List<Leilao> findByStatus(@Param("status") String status);
    
    @Query("select l from Leilao l where l.nomeProduto like CONCAT ('%',:nomeProduto,'%') and l.status = 'AB'")
    List<Leilao> findByFormSemCategoria(@Param("nomeProduto") String nomeProduto);
    
    @Query("select l from Leilao l where l.nomeProduto like CONCAT ('%',:nomeProduto,'%') and l.status = 'AB' and l.categoria = :categoria")
    List<Leilao> findByFormComCategoria(@Param("nomeProduto") String nomeProduto, @Param("categoria") Categoria categoria);

}
