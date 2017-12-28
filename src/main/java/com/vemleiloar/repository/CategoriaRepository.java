package com.vemleiloar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vemleiloar.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	@Query("select c from Categoria c order by c.id")
	List<Categoria> findAllWithOrder();
	
}
