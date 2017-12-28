package com.vemleiloar.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class LeilaoParticipacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal lance;

	private Date dataCadastro;

	@OneToOne
	private Usuario usuario;

	@OneToOne
	private Leilao leilao;
	
	@Transient
	private boolean chatCriado;
	
	@Transient
	private boolean chatAguardandoCriacao;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public BigDecimal getLance() {
		return lance;
	}

	public void setLance(BigDecimal lance) {
		this.lance = lance;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Leilao getLeilao() {
		return leilao;
	}

	public void setLeilao(Leilao leilao) {
		this.leilao = leilao;
	}

	public String getDataCadastroStr() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.dataCadastro);
	}

	public boolean isChatCriado() {
		return chatCriado;
	}

	public void setChatCriado(boolean chatCriado) {
		this.chatCriado = chatCriado;
	}

	public boolean isChatAguardandoCriacao() {
		return chatAguardandoCriacao;
	}

	public void setChatAguardandoCriacao(boolean chatAguardandoCriacao) {
		this.chatAguardandoCriacao = chatAguardandoCriacao;
	}

}
