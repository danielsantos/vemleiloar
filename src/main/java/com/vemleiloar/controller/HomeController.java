package com.vemleiloar.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.vemleiloar.service.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.service.LeilaoService;
import com.vemleiloar.service.UsuarioService;
import com.vemleiloar.util.Util;

@Controller
public class HomeController {

	@Autowired
	private LeilaoService service;

	@Autowired
	private UsuarioService serviceUsuario;

	@Autowired
	private EstadoService serviceEstado;

	@GetMapping("/")
	public ModelAndView index() {
		List<Leilao> lista = service.buscarPorStatus(null, "AB");
		ModelAndView mv = new ModelAndView("/index");
		mv.addObject("semFoto", Util.semFoto);
		mv.addObject("leiloes", lista);
		return mv;
	}
	
	@GetMapping("/inicio")
	public ModelAndView home() {

		ModelAndView mv = new ModelAndView("/home");

		List<Leilao> lista = service.findAll(null);
		mv.addObject("leiloes", lista);

		return mv;
	}

	@GetMapping("/comoFunciona")
	public ModelAndView comoFunciona() {
		return new ModelAndView("/comoFunciona");
	}

	@GetMapping("/cadastro")
	public ModelAndView cadastro() {
		ModelAndView mv = new ModelAndView("/cadastro");
		mv.addObject("usuario", new Usuario());
		mv.addObject("estados", serviceEstado.findAll());
		return mv;
	}

	@GetMapping("/perfil")
	public ModelAndView perfil(HttpServletRequest req) {
		ModelAndView mv = new ModelAndView("/perfil");
		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		mv.addObject("usuario", usuario);
		return mv;
	}

	/*
	@GetMapping("/confirmaEmail")
	public ModelAndView confirmaEmail(@PathVariable("token") String token) {

		Usuario usuario = serviceUsuario.findByToken(token);
		usuario.setSituacao("A");
		serviceUsuario.save(usuario);

		ModelAndView mv = new ModelAndView("/login");
		mv.addObject("msgSucesso", "E-mail confirmado com sucesso!");
		return mv;
	}
	*/

	@GetMapping("/403")
	public ModelAndView error() {
		return new ModelAndView("/error/403");
	}
	
	@GetMapping("/dashbord/q1w2e3r4t5y6")
	public ModelAndView dashbord(HttpServletRequest req) {
		try {
			ModelAndView mv = new ModelAndView("/dashbord");
			mv.addObject("qtdUsuarios", serviceUsuario.count());
			return mv;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}