package com.vemleiloar.service;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.vemleiloar.model.Leilao;
import com.vemleiloar.model.Usuario;

@Service
public class EmailService {
	
	@Autowired
    private Environment env;
	
	private static final String HOST_NAME     = "email.host.name";
	private static final String PORT          = "email.port";
	private static final String USER_NAME     = "email.user.name";
	private static final String USER_PASSWORD = "email.user.password";
	private static final String EMAIL_FROM    = "email.from";
	private static final String DOMINIO    	  = "dominio.vemleiloar";
		
	public void confirmarCadastro(Usuario usuario) {
		try {
			String texto = "Olá " + usuario.getNome() + ", " +
			               " \n\n" +
						   " Clique no link abaixo para confirmar seu cadastro no Vem Leiloar: " +
						   " \n\n " +
			               " http://www.vemleiloar.com.br/usuario/confirmaCadastro/" + usuario.getToken();
			
			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Confirmação de Cadastro");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recuperarSenha(Usuario usuario) {
		try {
			String texto = "Olá " + usuario.getNome() + ", " +
		               " \n\n" +
					   " Clique no link abaixo fazer a alteração da sua senha no Vem Leiloar: " +
					   " \n\n " +
		               " http://www.vemleiloar.com.br/alterarSenha/" + usuario.getToken();
			
			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Alterar Senha");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void analisarLeilao(Leilao leilao, Usuario usuario) {
		try {
			String texto = "Novo Leilão Criado. " +
		               " \n\n" +
					   " ID: " + leilao.getId() + " - " + "Produto: " + leilao.getNomeProduto() + 
					   " \n\n " +
					   " Descricao: " + leilao.getDescricao() +
					   " \n\n " +
					   " Usuario: " + usuario.getNome() + " " + usuario.getSobrenome() + " - "  + usuario.getEmail();

			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! Novo Leilão Cadastrado");
			email.setMsg(texto);
			email.addTo("danielsantosr.rj@gmail.com");
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void fechamentoLeiloes(Integer qtdLeiloesFechados) {
		try {
			String texto = "Foi executada a rotina de fechamento de leiloes neste momento. " +
					" \n\n" +
					" Quantidade de Leiloes Fechados: " + qtdLeiloesFechados;

			Usuario usuario = new Usuario();
			usuario.setEmail("danielsantosr.rj@gmail.com");

			Email email = getEmail(usuario);
			email.setSubject("Fechamento de Leilões Executado");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void informarLancesAoVendedor(Leilao leilao, Usuario usuario) {
		try {
			String texto = "Seu leilão está recebendo lances. " +
		               " \n\n" +
					   " ID: " + leilao.getId() + " - " + "Produto: " + leilao.getNomeProduto() + 
					   " \n\n " +
					   " Descricao: " + leilao.getDescricao() +
					   " \n\n " +
					   " Usuario: " + usuario.getNome() + " " + usuario.getSobrenome() + " - "  + usuario.getEmail();

			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Seu leilão está recebendo lances");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void informarLanceSuperado(Usuario usuario, Leilao leilao) {
		try {
			String texto = "Olá " + usuario.getNome() + ", \n " + " \n " +
					" Um novo lance superou o seu no leilao pelo produto '" + leilao.getNomeProduto() + "' " + 
					//" \n \n " + 
					//" FOTO DO PRODUTO " + 
					" \n \n " +
					" Fique atento! " +
					//" \n \n " + 
					//" Este leilao fecha DATA FECHAMENTO" + 
					" \n \n " + 
					" Ainda da tempo para você fazer um novo lance! " +
					" \n \n " +
					" Vem Leiloar!";

			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Seu lance foi superado");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void informarUsuarioSobreNovaMensagem(Usuario usuario, Leilao leilao) {
		try {
			String texto = "Olá " + usuario.getNome() + "," + 
				     "\n\n" +
				     " Você recebeu uma nova mensagem sobre o seguinte leilão: " + 
				     "\n\n" +
				     " ID: " + leilao.getId() + " - " + "Produto: " + leilao.getNomeProduto() + 
					 " \n\n " +
					 " Descricao: " + leilao.getDescricao() +
					 " \n\n " +
					 " \n\n " +
					 " Acesse http://www.vemleiloar.com.br Vem Leiloar!";
			
			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Nova Mensagem de Leilão");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// TODO REFATORAR PARA TER APENAS UM METODO PARAMETRIZADO COM A SITUACAO
	public void informarUsuarioSobreLiberacaoLeilao(Usuario usuario, Leilao leilao) {
		try {
			String texto = "Olá " + usuario.getNome() + "," + 
				     "\n\n" +
				     " Seu leilão foi liberado e já disponível no site. " + 
				     "\n\n" +
				     " ID: " + leilao.getId() + " - " + "Produto: " + leilao.getNomeProduto() + 
					 " \n\n " +
					 " Descrição: " + leilao.getDescricao() +
					 " \n\n " +
					 " \n\n " +
					 " Acesse http://www.vemleiloar.com.br Vem Leiloar!";
			
			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Seu Leilão foi Liberado");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void informarUsuarioSobreInvalidacaoLeilao(Usuario usuario, Leilao leilao) {
		try {
			String texto = "Olá " + usuario.getNome() + "," + 
				     "\n\n" +
				     " Seu leilão foi invalidado. " + // TODO CITAR UM MOTIVO PELA INVALIDACAO
				     " \n\n " +
				     " ID: " + leilao.getId() + " - " + "Produto: " + leilao.getNomeProduto() + 
					 " \n\n " +
					 " Descrição: " + leilao.getDescricao() +
					 " \n\n " +
					 " \n\n " +
					 " Acesse http://www.vemleiloar.com.br Vem Leiloar!";
			
			Email email = getEmail(usuario);
			email.setSubject("Vem Leiloar! - Seu Leilão foi Invalidado");
			email.setMsg(texto);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// TODO REFATORAR PARA TER APENAS UM METODO PARAMETRIZADO COM A SITUACAO
	
	/*
	private Email getEmail(Usuario usuario) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(env.getProperty(HOST_NAME));
			email.setSmtpPort(new Integer(env.getProperty(PORT)));
			email.setAuthenticator(new DefaultAuthenticator(env.getProperty(USER_NAME), env.getProperty(USER_PASSWORD)));
			email.setSSL(true);
			email.setFrom(env.getProperty(EMAIL_FROM));
			email.addTo(usuario.getEmail());
			return email;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	private Email getEmail(Usuario usuario) {
		try {
			Email email = new SimpleEmail();
			email.setHostName("smtp.vemleiloar.com.br");
			email.setSmtpPort(587);
			email.setAuthenticator(new DefaultAuthenticator("contato@vemleiloar.com.br", "Alina200263"));
			email.setSSL(false);
			email.setFrom("contato@vemleiloar.com.br");
			email.addTo(usuario.getEmail());
			return email;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
