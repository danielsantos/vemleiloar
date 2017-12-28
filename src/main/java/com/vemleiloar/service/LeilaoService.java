package com.vemleiloar.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.vemleiloar.model.Categoria;
import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.LeilaoImagem;
import com.vemleiloar.model.LeilaoParticipacao;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.repository.LeilaoImagemRepository;
import com.vemleiloar.repository.LeilaoParticipacaoRepository;
import com.vemleiloar.repository.LeilaoRepository;
import com.vemleiloar.util.Util;

@Service
public class LeilaoService {

	@Autowired
	private LeilaoRepository repository;

	@Autowired
	private LeilaoParticipacaoRepository repositoryPart;

	@Autowired
	private UsuarioService serviceUsuario;
	
	@Autowired
	private CategoriaService serviceCategoria;
	
	@Autowired
	private LeilaoImagemRepository repositoryLeilaoImagem;
	
	
	public ModelAndView paginaListarPendentes(HttpServletRequest req) {
		ModelAndView modelAndView = new ModelAndView("/leilao/listarPendentes");
		modelAndView.addObject("leiloes", buscarPorStatus(req, "PE"));
		modelAndView.addObject("semFoto", Util.semFoto);
		return modelAndView;
	}
	
	public ModelAndView paginaListarTodos(HttpServletRequest req) {
		ModelAndView modelAndView = new ModelAndView("/leilao/listarTodos");
		modelAndView.addObject("leiloes", buscarPorStatus(req, "AB"));
		modelAndView.addObject("leilao", new Leilao());
		modelAndView.addObject("semFoto", Util.semFoto);
		modelAndView.addObject("categorias", serviceCategoria.findAll());
		return modelAndView;
	}
	
	public ModelAndView paginaListarQuaisParticipo(HttpServletRequest req) {
		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		ModelAndView modelAndView = new ModelAndView("/leilao/listarQuaisParticipo");
		modelAndView.addObject("leiloes", listarQuaisParticipo(req));
		modelAndView.addObject("usuario", usuario);
		return modelAndView;
	}
	
	public ModelAndView paginaListarMeus(HttpServletRequest req) {
		List<Leilao> lista = findByUser(req);
		ModelAndView modelAndView = new ModelAndView("/leilao/listarMeus");
		modelAndView.addObject("leiloes", lista);
		modelAndView.addObject("semFoto", Util.semFoto);
		return modelAndView;
	}
	
	public ModelAndView paginaInicial() {
		ModelAndView modelAndView = new ModelAndView("/index");
		modelAndView.addObject("semFoto", Util.semFoto);
		modelAndView.addObject("leiloes", findAll(null));
		return modelAndView;	
	}
	
	public List<Leilao> findAll(HttpServletRequest req) {

		Usuario usuario = new Usuario();

		if ( req != null ) {
			usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		}

		LeilaoParticipacao lp;

		List<Leilao> list = repository.findAll();

		for (Leilao leilao : list) {

			if ( usuario.getId() != null ) {
				lp = repositoryPart.findByUserAndLeilao(usuario.getId(), leilao.getId());

				if ( leilao.getUsuario().getId() == usuario.getId() )
					leilao.setMeuLeilao(true);

			} else {
				lp = repositoryPart.findByLeilao(leilao.getId());
			}

			if (lp != null)
				leilao.setUltimoLance(lp.getLance());

			leilao.setNomeProduto(StringUtils.rightPad( leilao.getNomeProduto() , 30 , "" ));

		}

		return list;
	}
	
	public List<Leilao> pesquisarLeilao(HttpServletRequest req, Leilao leilaoForm) {
		Usuario usuario = new Usuario();
		if ( req != null ) {
			usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		}
		LeilaoParticipacao lp;
		List<Leilao> list;
		
		if ( leilaoForm.getCategoria() != null ) {
			list = repository.findByFormComCategoria(leilaoForm.getNomeProduto(), leilaoForm.getCategoria());
		} else {
			list = repository.findByFormSemCategoria(leilaoForm.getNomeProduto());
		}
		
		for (Leilao leilao : list) {
			if ( usuario.getId() != null ) {
				lp = repositoryPart.findByUserAndLeilao(usuario.getId(), leilao.getId());
				if ( leilao.getUsuario().getId() == usuario.getId() )
					leilao.setMeuLeilao(true);
			} else {
				lp = repositoryPart.findByLeilao(leilao.getId());
			}
			if (lp != null)
				leilao.setUltimoLance(lp.getLance());
			
			leilao.setNomeProduto(StringUtils.rightPad( leilao.getNomeProduto() , 30 , "" ));
			
			LeilaoImagem imagem = repositoryLeilaoImagem.findByLeilao(leilao);
			if (imagem != null && imagem.getFoto() != null && !imagem.getFoto().equals("")) {
				leilao.setImagem(imagem);
			}
		}
		return list;
	}
	
	public List<Leilao> buscarPorStatus(HttpServletRequest req, String status) {
		Usuario usuario = new Usuario();
		if ( req != null ) {
			usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		}
		LeilaoParticipacao lp;
		List<Leilao> list = repository.findByStatus(status);
		for (Leilao leilao : list) {
			if ( usuario.getId() != null ) {
				lp = repositoryPart.findByUserAndLeilao(usuario.getId(), leilao.getId());
				if ( leilao.getUsuario().getId() == usuario.getId() )
					leilao.setMeuLeilao(true);
			} else {
				lp = repositoryPart.findByLeilao(leilao.getId());
			}
			if (lp != null)
				leilao.setUltimoLance(lp.getLance());
			
			leilao.setNomeProduto(StringUtils.rightPad( leilao.getNomeProduto() , 30 , "" ));
			
			LeilaoImagem imagem = repositoryLeilaoImagem.findByLeilao(leilao);
			if (imagem != null && imagem.getFoto() != null && !imagem.getFoto().equals("")) {
				leilao.setImagem(imagem);
			}
		}
		return list;
	}
	
	/*
	public List<Leilao> buscarPendentes(HttpServletRequest req) {
		Usuario usuario = new Usuario();
		if ( req != null ) {
			usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		}
		LeilaoParticipacao lp;
		List<Leilao> list = repository.findPendentes();
		for (Leilao leilao : list) {
			if ( usuario.getId() != null ) {
				lp = repositoryPart.findByUserAndLeilao(usuario.getId(), leilao.getId());
				if ( leilao.getUsuario().getId() == usuario.getId() )
					leilao.setMeuLeilao(true);
			} else {
				lp = repositoryPart.findByLeilao(leilao.getId());
			}
			if (lp != null)
				leilao.setUltimoLance(lp.getLance());
			
			leilao.setNomeProduto(StringUtils.rightPad( leilao.getNomeProduto() , 30 , "" ));
		}
		return list;
	}
	*/

	public Leilao montaLeilaoParaSalvar(Leilao leilao, MultipartFile file, HttpServletRequest req) {
		try {
			// IMAGEM
			byte[] encodeBase64 = Base64.encodeBase64(file.getBytes());
			String base64Encoded = new String(encodeBase64, "UTF-8");
			LeilaoImagem imagem = new LeilaoImagem();
			imagem.setDataCadastro(new Date());
			imagem.setFoto(base64Encoded);
			
			Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
			leilao.setDataCadastro(new Date());
			leilao.setDataFechamento(Util.retornaDataFechamento());
			leilao.setStatus("PE");
			leilao.setUsuario(usuario);
			leilao.setUuid(UUID.randomUUID().toString());
			leilao.setImagem(imagem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return leilao;
	}
	
	public Leilao findOne(Long id) {
		Leilao leilao = repository.findOne(id);
		LeilaoParticipacao participacao = repositoryPart.findByLeilao(leilao.getId());

		if ( participacao != null ) {
			leilao.setUltimoLance(participacao.getLance());
		}
		
		LeilaoImagem imagem = repositoryLeilaoImagem.findByLeilao(leilao);
		if (imagem != null) 
			leilao.setImagem(imagem);

		return leilao;
	}
	
	public Leilao save(Leilao leilao) {
		return repository.saveAndFlush(leilao);
	}

	public void save(LeilaoParticipacao participacao) {
		repositoryPart.saveAndFlush(participacao);
	}
	
	public void delete(Long id) {
		repository.delete(id);
	}

	public List<Leilao> findByUser(HttpServletRequest req) {

		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());

		LeilaoParticipacao lp;

		List<Leilao> list = repository.findByUser(usuario);

		for (Leilao l : list) {

			lp = repositoryPart.findByLeilao(l.getId());
			Integer qtdLances = repositoryPart.quantPartLeilao(l.getId());

			l.setQtdLances(qtdLances);

			if (lp != null)
				l.setUltimoLance(lp.getLance());

			LeilaoImagem imagem = repositoryLeilaoImagem.findByLeilao(l);
			
			if (imagem != null && imagem.getFoto() != null) {
				//System.out.println("###############");
				//System.out.println("Leilao: " + l.getNomeProduto());
				//System.out.println("---------------------------------------------------------");
				//System.out.println(imagem.getFoto());
				//System.out.println("---------------------------------------------------------");
				l.setImagem(imagem);
			}

			/*
			if (l.getFoto() != null) {
				String image = "data:image/jpg;base64,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";
				l.setFotoStr(image);
			}
			*/


		}

		return list;
	}

	public List<Leilao> findReadyToClose() {

		return repository.findReadyToClose(new Date());

	}

	public List<Leilao> findByFiltro(Categoria categoria) {

		List<Leilao> list;

		if ( categoria != null ) {
			list = repository.findByFiltro(categoria);
		} else {
			list = repository.findAll();
		}

		LeilaoParticipacao lp;

		for (Leilao l : list) {

			lp = repositoryPart.findByLeilao(l.getId());

			if (lp != null)
				l.setUltimoLance(lp.getLance());

		}

		return list;

	}

	public Leilao findByUuid(String uuid) {
		return repository.findByUuid(uuid);
	}

	public List<Leilao> listarQuaisParticipo(HttpServletRequest req) {

		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		List<Leilao> leilaoList = new ArrayList<Leilao>();
		List<Leilao> lista = findAll(null);

		for (Leilao leilao : lista) {
			LeilaoParticipacao part = repositoryPart.findByUserAndLeilao(usuario.getId(), leilao.getId());
			if ( part != null ) {
				leilaoList.add(leilao);
			}
		}

		return leilaoList;
	}

}