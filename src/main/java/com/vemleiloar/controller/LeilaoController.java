package com.vemleiloar.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.LeilaoParticipacao;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.service.CategoriaService;
import com.vemleiloar.service.EmailService;
import com.vemleiloar.service.LeilaoImagemService;
import com.vemleiloar.service.LeilaoParticipacaoService;
import com.vemleiloar.service.LeilaoService;
import com.vemleiloar.service.UsuarioService;
import com.vemleiloar.util.Util;

@Controller
@RequestMapping("/leilao")
public class LeilaoController {
	
	@Autowired
	private LeilaoImagemService serviceLeilaoImagem;

	@Autowired
	private LeilaoService service;

	@Autowired
	private LeilaoParticipacaoService serviceLeilaoPart;

	@Autowired
	private CategoriaService serviceCategoria;

	@Autowired
	private UsuarioService serviceUsuario;
	
	@Autowired
	private EmailService serviceEmail;
	
	
	// TODO REFATORAR
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(Leilao leilao, HttpServletRequest req) {
		try {
			ModelAndView modelAndView = new ModelAndView("/leilao/listarTodos");
			List<Leilao> lista = service.pesquisarLeilao(req, leilao);
			if (lista.isEmpty()) {
				modelAndView.addObject("msgListaVazia", "Não foram encontrados leilões");
			} else {
				modelAndView.addObject("leiloes", lista);
			}
			modelAndView.addObject("leilao", leilao);
			modelAndView.addObject("semFoto", Util.semFoto);
			modelAndView.addObject("categorias", serviceCategoria.findAll());
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}
	
	
	@GetMapping("/invalidar/{id}")
	public ModelAndView invalidar(@PathVariable("id") Long id, HttpServletRequest req) {
		try {
			Leilao leilao = service.findOne(id);
			leilao.setStatus("IN");
			service.save(leilao);
			
			//Usuario usuario = serviceUsuario.findOne(leilao.getUsuario().getId());
			//serviceEmail.informarUsuarioSobreInvalidacaoLeilao(usuario, leilao); // TODO REFATORAR PARA TER UM MOTIVO PARA INVALIDACAO
			
			ModelAndView modelAndView = service.paginaListarPendentes(req);
			modelAndView.addObject("msgSucesso", "Leilão inválidado com sucesso");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/liberar/{id}")
	public ModelAndView liberar(@PathVariable("id") Long id, HttpServletRequest req) {
		try {
			Leilao leilao = service.findOne(id);
			leilao.setStatus("AB");
			service.save(leilao);
			
			Usuario usuario = serviceUsuario.findOne(leilao.getUsuario().getId());
			serviceEmail.informarUsuarioSobreLiberacaoLeilao(usuario, leilao);
			
			ModelAndView modelAndView = service.paginaListarPendentes(req);
			modelAndView.addObject("msgSucesso", "Leilão liberado com sucesso");
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/pendente/{id}")
	public ModelAndView leilaoPendente(@PathVariable("id") Long id) {
		try {
			ModelAndView modelAndView = new ModelAndView("/leilao/leilaoPendente");
			Leilao leilao = service.findOne(id);
			modelAndView.addObject("leilao", leilao);
			return modelAndView;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/pendentes")
	public ModelAndView listarPendentes(HttpServletRequest req) {
		try {
			return service.paginaListarPendentes(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/listar/meus")
	public ModelAndView listarMeus(HttpServletRequest req) {
		try {
			return service.paginaListarMeus(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/listar/quais/participo")
	public ModelAndView listarQuaisParticipo(HttpServletRequest req) {
		try {
			return service.paginaListarQuaisParticipo(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// TODO RENOMEAR PARA LISTAR ATIVOS
	@GetMapping("/listar/todos")
	public ModelAndView listarTodos(HttpServletRequest req) {
		try {
			return service.paginaListarTodos(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/mostraImagem")
	public File mostraImagem(HttpServletRequest req) {
		try {
			return new File("c:\\arquivos\\teste.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@GetMapping("/add")
	public ModelAndView add(Leilao leilao) {
		ModelAndView modelAndView = new ModelAndView("/leilao/add");
		modelAndView.addObject("leilao", leilao);
		modelAndView.addObject("categorias", serviceCategoria.findAllWithOrder());
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView save(@Valid Leilao leilao, @RequestParam("file") MultipartFile file, BindingResult result,
			                 HttpServletRequest req, HttpSession session) {
		try {
			if (file.getSize() > 2000000) { // TODO COLOCAR EM UMA PROPERTIE - 2 MB
				result.addError(new ObjectError("", "A imagem deve ter no máximo 2 MB."));
				return add(leilao);
			}
			
			if (leilao.getNomeProduto().equals("")) {
				result.addError(new ObjectError("nomeProduto", "O nome do produto é obrigatório."));
				return add(leilao);
			}
			
			if (leilao.getCategoria().getId() == 1l) {
				result.addError(new ObjectError("nomeProduto", "A categoria é obrigatória."));
				return add(leilao);
			}

			if (result.hasErrors()) {
				return add(leilao);
			}

			leilao = service.montaLeilaoParaSalvar(leilao, file, req);
			service.save(leilao);
			
			leilao.getImagem().setLeilao(leilao);
			serviceLeilaoImagem.save(leilao.getImagem());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		serviceEmail.analisarLeilao(leilao, usuario);
		
		ModelAndView mv = service.paginaListarMeus(req); 
		mv.addObject("msgSucesso", "Seu leilão foi cadastrado e está em análise para ser exibido aos compradores. Lhe enviaremos um e-mail quando a análise for concluída.");
		return mv;
	}

	@PostMapping("/participacao/save")
	public ModelAndView participacaoSave(@Valid Leilao leilao, BindingResult result, HttpServletRequest req) {

		BigDecimal lance = leilao.getLance(); 
		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		//Leilao leilao = service.findOne(leilaoForm.getId());
		leilao = service.findOne(leilao.getId());
		Usuario usuarioVendedor = serviceUsuario.findOne(leilao.getUsuario().getId());
		ModelAndView modelAndView = new ModelAndView("/leilao/participar");
		modelAndView.addObject("leilao", leilao);
		
		if (lance == null) {
			modelAndView.addObject("msgError", "O lance é obrigatório.");
			return modelAndView;
		}		
		
		if (leilao.getLanceInicial().compareTo(lance) == 1 ) {
			modelAndView.addObject("msgError", "O seu lance deve ser maior que o lance inicial estipulado.");
			return modelAndView;
		}

		if (leilao.getUltimoLance() != null && leilao.getUltimoLance().compareTo(lance) == 1) {
			modelAndView.addObject("msgError", "O seu lance deve ser maior que o último para o produto.");
			return modelAndView;
		}
		
		/*
		 * if(result.hasErrors()) { return participar(null, leilao, req); }
		 */

		LeilaoParticipacao part = new LeilaoParticipacao();
		part.setDataCadastro(new Date());
		part.setLance(lance);
		part.setUsuario(usuario);
		part.setLeilao(leilao);

		service.save(part);
		serviceEmail.informarLanceSuperado(usuario, leilao);
		
		// TODO MANDA EMAIL PARA O VENDEDOR AVISANDO DOS LANCES NO PRIMEIRO E DE QUATRO EM QUATRO
		Integer qtdLances = serviceLeilaoPart.quantPartLeilao(leilao);
		if ( qtdLances == 1 || qtdLances % 4 == 0 ) {
			serviceEmail.informarLancesAoVendedor(leilao, usuarioVendedor);
		}
		
		ModelAndView mv = service.paginaListarQuaisParticipo(req);
		mv.addObject("msgSucesso", "Lance cadastrado com sucesso!");
		return mv;
	}

	@GetMapping("/edit/{uuid}")
	public ModelAndView edit(@PathVariable("uuid") String uuid) {
		return add(service.findByUuid(uuid));
	}

	@GetMapping("/lances/{uuid}")
	public ModelAndView lances(@PathVariable("uuid") String uuid) {
		Leilao leilao = service.findByUuid(uuid);
		List<LeilaoParticipacao> participacoes = serviceLeilaoPart.buscaParticipacoes(leilao);

		ModelAndView modelAndView = new ModelAndView("/leilao/lances");
		modelAndView.addObject("leilao", leilao);
		modelAndView.addObject("participacoes", participacoes);
		return modelAndView;
	}

	@GetMapping("/participar/{id}")
	public ModelAndView participar(@PathVariable("id") Long id, Leilao leilao, HttpServletRequest req) {

		ModelAndView modelAndView = new ModelAndView("/leilao/participar");

		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		Leilao l;
		LeilaoParticipacao lp;

		if (id != null) { // QUANDO ABRE A TELA

			l = service.findOne(id);
			lp = serviceLeilaoPart.findByUserAndLeilao(usuario, l);

			if (lp != null)
				l.setLance(lp.getLance());
			
			if (l.getUsuario().getId() == usuario.getId()) 
				l.setMeuLeilao(true);

			modelAndView.addObject("leilao", l);

		} else { // QUANDO TEM ERRO E A TELA RECEBE AS CRITICAS

			lp = serviceLeilaoPart.findByUserAndLeilao(usuario, leilao);

			if (lp != null)
				leilao.setLance(lp.getLance());

			modelAndView.addObject("leilao", leilao);

		}

		return modelAndView;
	}

	@GetMapping("/executa/fechamentos")
	public ModelAndView executaFechamentos() {
		List<Leilao> lista = service.findReadyToClose();

		for (Leilao l : lista) {
			l.setStatus("FE");
			service.save(l);
		}
		return new ModelAndView("/admin/fechamentoLeiloes");
	}

	@GetMapping("/filtro")
	public ModelAndView filtro(Leilao leilao) {
		List<Leilao> leiloes = service.findByFiltro(leilao.getCategoria());

		ModelAndView modelAndView = new ModelAndView("/leilao/listarTodos");
		modelAndView.addObject("categorias", serviceCategoria.findAll());
		modelAndView.addObject("leiloes", leiloes);
		modelAndView.addObject("leilao", leilao);
		return modelAndView;
	}

	@GetMapping("/opa")
	public ModelAndView opa() {
		ModelAndView modelAndView = new ModelAndView("/leilao/opa");
		return modelAndView;
	}

	@PostMapping("/teste")
	public ModelAndView teste(@RequestParam("file") MultipartFile file) {

		try {
			String caminho = "C:\\arquivos\\" + file.getOriginalFilename();
			file.transferTo(new File(caminho));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Leilao leilao = service.findOne(12l);
		ModelAndView modelAndView = new ModelAndView("/leilao/listarTodos");
		modelAndView.addObject("categorias", serviceCategoria.findAll());
		modelAndView.addObject("leiloes", service.findAll(null));
		modelAndView.addObject("leilao", leilao);
		return modelAndView;
	}
	
	@GetMapping("/finalizar/{uuid}")
	public ModelAndView finalizar(@PathVariable("uuid") String uuid, HttpServletRequest req) {
		Leilao leilao = service.findByUuid(uuid);
		leilao.setStatus("FI");
		service.save(leilao);
		
		ModelAndView mv = service.paginaListarMeus(req);
		mv.addObject("msgSucesso", "Leilão finalizado com sucesso!");
		return mv;
	}

}