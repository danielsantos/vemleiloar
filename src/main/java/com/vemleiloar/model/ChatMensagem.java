package com.vemleiloar.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class ChatMensagem {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Chat chat;

    private Date data;

    private String tpUsuario;

    private String mensagem;
    
    private Boolean visto;
    

    @Transient
    private String dataStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getTpUsuario() {
		return tpUsuario;
	}

	public void setTpUsuario(String tpUsuario) {
		this.tpUsuario = tpUsuario;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

    public String getDataStr() {
        if ( this.data != null )
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(this.data);
        else
            return "";
    }

	public Boolean getVisto() {
		return visto;
	}

	public void setVisto(Boolean visto) {
		this.visto = visto;
	}
    

}
