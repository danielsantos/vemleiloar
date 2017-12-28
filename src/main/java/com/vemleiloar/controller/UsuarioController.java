package com.vemleiloar.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vemleiloar.model.Usuario;
import com.vemleiloar.service.EmailService;
import com.vemleiloar.service.LeilaoService;
import com.vemleiloar.service.UsuarioService;


@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private LeilaoService serviceLeilao;
	
	@Autowired
	private EmailService serviceEmail;
		

	@GetMapping("/usuarios")
	public ModelAndView findAll() {
		List<Usuario> lista = service.findAll();
		ModelAndView modelAndView = new ModelAndView("usuarios");
		modelAndView.addObject("usuarios", lista);
		return modelAndView;
	}
	
	@GetMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("/add");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@PostMapping("/salvar")
	public ModelAndView salvar(Usuario usuario, BindingResult result) {
		try {
			ModelAndView mv = service.validarCadastroUsuario(usuario);
			if (mv != null) return mv;
			service.salvar(usuario);
			serviceEmail.confirmarCadastro(usuario);
			ModelAndView modelAndView = serviceLeilao.paginaInicial();
			modelAndView.addObject("msgSucesso", "Você receberá um e-mail com um link para confirmação do seu cadastro");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/confirmaCadastro/{token}")
	public ModelAndView confirmaCadastro(@PathVariable("token") String token) {
		Usuario usuario = service.findByToken(token);
		
		if ( usuario != null ) {
			ModelAndView mv = new ModelAndView("/confirmaCadastro");
			mv.addObject("usuario", usuario);
			return mv;
		} else {
			ModelAndView mv = serviceLeilao.paginaInicial();
			mv.addObject("msgErro", "Link inválido.");
			return mv;
		}
	}
	
	@PostMapping("/confirmaCadastro")
	public ModelAndView confirmaCadastro(@Valid Usuario usuario) {
		service.confirmaCadastro(usuario);
		
		ModelAndView mv = new ModelAndView("/login");
		mv.addObject("msgSucesso", "Cadastro confirmado com sucesso!");
		return mv;
	}

	@GetMapping("/edit/{id}")
	public ModelAndView editar(@PathVariable("id") Long id) {
		return novo(service.findOne(id));
	}
	
	@GetMapping("/esqueciMinhaSenha")
	public ModelAndView esqueciMinhaSenhaForm(Usuario usuario) {
		ModelAndView mv = new ModelAndView("/esqueciMinhaSenha");
		mv.addObject("usuario", usuario);
		return mv;
	}
	
	@PostMapping("/emailRecupSenha")
	public ModelAndView emailRecuperacaoSenha(@Valid Usuario usuario) {
		Usuario u = service.esqueciMinhaSenha(usuario);
		if ( u != null ) {
			serviceEmail.recuperarSenha(usuario);	
			ModelAndView mv = serviceLeilao.paginaInicial();
			mv.addObject("msgSucesso", "Você receberá um e-mail com um link para fazer a alteração da sua senha");
			return mv;		
		} else {
			ModelAndView mv = new ModelAndView("/esqueciMinhaSenha");
			mv.addObject("msgErro", "O E-mail informado não está cadastrado.");
			mv.addObject("usuario", usuario);
			return mv;
		}
	}
	
}