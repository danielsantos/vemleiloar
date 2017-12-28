package com.vemleiloar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vemleiloar.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.username = :username")
    Usuario findByUsername(@Param("username") String username);

    @Query("select u from Usuario u where u.token = :token")
    Usuario findByToken(@Param("token") String token);
    
    @Query("select u from Usuario u where u.uuid = :uuid")
    Usuario findByUuid(@Param("uuid") String uuid);

    @Query("select u from Usuario u where u.email = :email")
	Usuario findByEmail(@Param("email") String email);

}
