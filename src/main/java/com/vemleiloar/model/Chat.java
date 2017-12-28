package com.vemleiloar.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Leilao leilao;

    private Date data;

    @OneToOne
    private Usuario usuarioVendedor;
    
    @OneToOne
    private Usuario usuarioComprador;

    @Transient
    private String dataStr;
    
    private String situacao;

    public Chat() {
		super();
	}

	public Chat(Leilao leilao, Usuario usuarioVendedor, Usuario usuarioComprador) {
		super();
		this.leilao = leilao;
		this.usuarioVendedor = usuarioVendedor;
		this.usuarioComprador = usuarioComprador;
		this.situacao = "1";
		this.data = new Date();
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Leilao getLeilao() {
        return leilao;
    }

    public void setLeilao(Leilao leilao) {
        this.leilao = leilao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataStr() {
        if ( this.data != null )
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.data);
        else
            return "";
    }

	public Usuario getUsuarioVendedor() {
		return usuarioVendedor;
	}

	public void setUsuarioVendedor(Usuario usuarioVendedor) {
		this.usuarioVendedor = usuarioVendedor;
	}

	public Usuario getUsuarioComprador() {
		return usuarioComprador;
	}

	public void setUsuarioComprador(Usuario usuarioComprador) {
		this.usuarioComprador = usuarioComprador;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

}
