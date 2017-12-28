package com.vemleiloar.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.vemleiloar.model.Chat;
import com.vemleiloar.model.ChatMensagem;
import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.LeilaoParticipacao;
import com.vemleiloar.model.Usuario;
import com.vemleiloar.service.ChatMensagemService;
import com.vemleiloar.service.ChatService;
import com.vemleiloar.service.EmailService;
import com.vemleiloar.service.LeilaoParticipacaoService;
import com.vemleiloar.service.LeilaoService;
import com.vemleiloar.service.UsuarioService;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService service;
	
	@Autowired
	private ChatMensagemService serviceChatMensagem;	

	@Autowired
	private UsuarioService serviceUsuario;
	
	@Autowired
	private LeilaoService serviceLeilao;
	
	@Autowired
	private LeilaoParticipacaoService serviceLeilaoPart;
	
	@Autowired
	private EmailService serviceEmail;
	

	@GetMapping("/leilao")
	public ModelAndView home(Chat chat) {
		List<Chat> lista = service.findAll();

		ModelAndView modelAndView = new ModelAndView("/chat/home");
		modelAndView.addObject("lista", lista);
		modelAndView.addObject("chat", chat);
		modelAndView.addObject("usuarioPrincipal", 13);
		modelAndView.addObject("usuarioSecundario", 4);
		return modelAndView;
	}

	@PostMapping("/save")
	public ModelAndView save(@Valid ChatMensagem chatMensagem, BindingResult result, HttpServletRequest req) {

		/*
		 * if(result.hasErrors()) { return add(usuario); }
		 */
		
		//Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		//chat.setUsuario(usuario);
		//chat.setData(new Date());
		//service.save(chat);
		
		Chat chat = service.findOne(chatMensagem.getChat().getId());
		Leilao leilao = serviceLeilao.findOne(chat.getLeilao().getId());
		Usuario usuario = serviceUsuario.findByUsername(req.getRemoteUser());
		
		chatMensagem.setData(new Date());
		chatMensagem.setVisto(false);
		serviceChatMensagem.save(chatMensagem);

		if ( chatMensagem.getTpUsuario().equals("V") ) {
			Usuario usuarioComprador = serviceUsuario.findOne(chat.getUsuarioComprador().getId());
			serviceEmail.informarUsuarioSobreNovaMensagem(usuarioComprador, leilao);
			return viewV(chat.getUsuarioComprador().getUuid(), leilao.getUuid());
		} else {
			Usuario usuarioVendedor = serviceUsuario.findOne(chat.getUsuarioVendedor().getId());
			serviceEmail.informarUsuarioSobreNovaMensagem(usuarioVendedor, leilao);
			return viewC(usuario.getUuid(), leilao.getUuid());
		}
		
	}
	
	@GetMapping("/view/v/{usuarioUuid}/{leilaoUuid}")
	public ModelAndView viewV(@PathVariable("usuarioUuid") String usuarioUuid, @PathVariable("leilaoUuid") String leilaoUuid) {
		
		ModelAndView modelAndView = new ModelAndView("/chat/home");
		ChatMensagem chatMensagem = new ChatMensagem();
		
		Leilao leilao = serviceLeilao.findByUuid(leilaoUuid);
		Usuario usuarioVendedor  = serviceUsuario.findOne(leilao.getUsuario().getId());
		Usuario usuarioComprador = serviceUsuario.findByUuid(usuarioUuid);
		
		// TODO TESTAR QUANDO OBJS FOREM NULL
		//if ( != null )
		
		Chat chat = service.findChat(leilao, usuarioComprador);
		if (chat != null) {

			List<ChatMensagem> lista = serviceChatMensagem.findByChat(chat);
			
			// TODO TESTAR QUANDO OBJS FOREM NULL
			//if ( != null )
			
			modelAndView.addObject("lista", lista);
			
		} else {
			
			chat = new Chat(leilao, usuarioVendedor, usuarioComprador); 
			service.save(chat);
			
		}
		
		chatMensagem.setChat(chat);
		chatMensagem.setTpUsuario("V");
		
		modelAndView.addObject("chat", chat);
		modelAndView.addObject("chatMensagem", chatMensagem);
		return modelAndView;
	}
	
	@GetMapping("/fechar/{usuarioUuid}/{leilaoUuid}")
	public ModelAndView fechar(@PathVariable("usuarioUuid") String usuarioUuid, @PathVariable("leilaoUuid") String leilaoUuid) {
		
		Leilao leilao = serviceLeilao.findByUuid(leilaoUuid);
		Usuario usuarioComprador = serviceUsuario.findByUuid(usuarioUuid);
		
		Chat chat = service.findChat(leilao, usuarioComprador);
		chat.setSituacao("0");
		service.save(chat);
		
		List<LeilaoParticipacao> participacoes = serviceLeilaoPart.buscaParticipacoes(leilao);

		ModelAndView modelAndView = new ModelAndView("/leilao/lances");
		modelAndView.addObject("leilao", leilao);
		modelAndView.addObject("participacoes", participacoes);
		return modelAndView;
	}
	
	@GetMapping("/view/c/{usuarioUuid}/{leilaoUuid}")
	public ModelAndView viewC(@PathVariable("usuarioUuid") String usuarioUuid, @PathVariable("leilaoUuid") String leilaoUuid) {
		
		ModelAndView modelAndView = new ModelAndView("/chat/home");
		ChatMensagem chatMensagem = new ChatMensagem();
		
		Leilao leilao = serviceLeilao.findByUuid(leilaoUuid);
		Usuario usuarioVendedor  = serviceUsuario.findOne(leilao.getUsuario().getId());
		Usuario usuarioComprador = serviceUsuario.findByUuid(usuarioUuid);
		
		// TODO TESTAR QUANDO OBJS FOREM NULL
		//if ( != null )
		
		Chat chat = service.findChat(leilao, usuarioComprador);
		if (chat != null) {

			List<ChatMensagem> lista = serviceChatMensagem.findByChat(chat);
			
			// TODO TESTAR QUANDO OBJS FOREM NULL
			//if ( != null )
			
			modelAndView.addObject("lista", lista);
			
		} else {
			
			chat = new Chat(leilao, usuarioVendedor, usuarioComprador); 
			service.save(chat);
			
		}
		
		chatMensagem.setChat(chat);
		chatMensagem.setTpUsuario("C");
		
		modelAndView.addObject("chat", chat);
		modelAndView.addObject("chatMensagem", chatMensagem);
		return modelAndView;
	}

}
