package com.vemleiloar.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
public class Leilao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String uuid;

	@NotNull
	private String nomeProduto;

	private String descricao;

	@NotNull
	private BigDecimal lanceInicial;

	private Date dataCadastro;

	private Date dataFechamento;

	@OneToOne
	private Usuario usuario;

	private String status;

	@NotNull
	@OneToOne
	private Categoria categoria;

//	@Lob
//	@Column(name="fotoBase64New", length=1000000)
//	private String foto;
	
	@Transient
	private LeilaoImagem imagem;

	@Transient
	private BigDecimal lance;

	@Transient
	private String statusStr;

	@Transient
	private String dataFechamentoStr;

	@Transient
	private BigDecimal ultimoLance;

	@Transient
	private Integer qtdLances;

	@Transient
	private Boolean meuLeilao = false;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public BigDecimal getLanceInicial() {
		return lanceInicial;
	}

	public void setLanceInicial(BigDecimal lanceInicial) {
		this.lanceInicial = lanceInicial;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusStr() {
		if (status.equals("AB")) {
			return "ABERTO";
		} else if (status.equals("FE")) {
			return "FECHADO";
		} else if (status.equals("FI")) {
			return "FINALIZADO";
		} else if (status.equals("PE")) {
			return "PENDENTE";
		} else if (status.equals("IN")) {
			return "INVÁLIDADO";
		} else if (status.equals("CA")) {
			return "CANCELADO";
		}
		return "";
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getDataFechamentoStr() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.dataFechamento);
	}

	public BigDecimal getUltimoLance() {
		if (ultimoLance == null) {
			return new BigDecimal(0);
		} else {
			return ultimoLance;
		}
	}

	public void setUltimoLance(BigDecimal ultimoLance) {
		this.ultimoLance = ultimoLance;
	}

	public Integer getQtdLances() {
		return qtdLances;
	}

	public void setQtdLances(Integer qtdLances) {
		this.qtdLances = qtdLances;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getMeuLeilao() {
		return meuLeilao;
	}

	public void setMeuLeilao(Boolean meuLeilao) {
		this.meuLeilao = meuLeilao;
	}

	public LeilaoImagem getImagem() {
		return imagem;
	}

	public void setImagem(LeilaoImagem imagem) {
		this.imagem = imagem;
	}

}
