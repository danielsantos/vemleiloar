package com.vemleiloar.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.vemleiloar.model.Role;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.repository.UsuarioRepository;
import com.vemleiloar.util.Util;

@Service
public class UsuarioService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UsuarioRepository repository;

	//@Autowired
	//private RoleRepository repositoryRole;

	public Long count() {
		return repository.count();
	}
	
	public List<Usuario> findAll() {
		return repository.findAll(); 
	}
	
	public Usuario findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Usuario salvar(Usuario usuario) {
		Set<Role> roles = new HashSet<Role>();
		roles.add(new Role(1l));

		usuario.setRoles(roles);
		usuario.setUuid(UUID.randomUUID().toString());
		usuario.setToken(Util.geraToken());
		usuario.setDataCadastro(new Date());
		usuario.setSituacao("P");
		usuario.setUsername(usuario.getEmail()); // TODO REFATORAR
		return repository.saveAndFlush(usuario);
	}
	
	public void confirmaCadastro(Usuario usuario) {
		Usuario u = findByUuid(usuario.getUuid());
		u.setSituacao("A");
		u.setPassword(bCryptPasswordEncoder.encode(usuario.getPassword()));
		u.setDataAtivacao(new Date());
		u.setToken(null);
		repository.saveAndFlush(u);
	}
	
	public Usuario esqueciMinhaSenha(Usuario usuario) {
		
		Usuario u = repository.findByEmail(usuario.getEmail());
		
		if ( u != null ) {
			u.setToken(Util.geraToken());
			repository.saveAndFlush(u);
			return u;
		} else {
			return null;
		}
		
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}

	public Usuario findByUsername(String username) {
		return repository.findByUsername(username);
	}
	
	public Usuario findByEmail(String email) {
		return repository.findByEmail(email);
	}

	public Usuario findByToken(String token) {
		return repository.findByToken(token);
	}
	
	public Usuario findByUuid(String uuid) {
		return repository.findByUuid(uuid);
	}
	
	public ModelAndView validarCadastroUsuario(Usuario usuario) {
		
		ModelAndView mv = new ModelAndView("/cadastro");
		mv.addObject("usuario", usuario);

		if ( StringUtils.isEmpty(usuario.getNome()) || StringUtils.isEmpty(usuario.getSobrenome()) ||
			 StringUtils.isEmpty(usuario.getEmail()) || StringUtils.isEmpty(usuario.getConfirmeEmail()) ||
			 usuario.getDataNascimento() == null ) {
			mv.addObject("msgError", "Todos os campos são obrigatórios.");
			return mv;
		}
		if ( !usuario.getEmail().equals(usuario.getConfirmeEmail()) ) {
			mv.addObject("msgError", "E-mails não coincidem.");
			return mv;
		}
		if (Util.emailValido(usuario.getEmail()) == false) {
			mv.addObject("msgError", "E-mail inválido.");
			return mv;
		}
		if ( findByEmail(usuario.getEmail()) != null ) {
			mv.addObject("msgError", "O E-mail informado já está cadastrado.");
			return mv;
		}		
		
		return null;
	}
	
}