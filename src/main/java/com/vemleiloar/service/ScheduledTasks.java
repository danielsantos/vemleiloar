package com.vemleiloar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.vemleiloar.model.Leilao;

@Component
public class ScheduledTasks {
	
	private static final String JOB_FECHA_LEILOES = "job.fecha.leiloes";
	private static final String TRUE = "1"; // FIXME .. EXTRAIR PARA UM ENUM
	
	@Autowired
    private Environment env;

    @Autowired
    private LeilaoService service;

    @Autowired
    private EmailService serviceEmail;

    @Scheduled(fixedRate = 3600000)
    public void executaFechamentos() {
    	
    	if ( env.getProperty(JOB_FECHA_LEILOES).equals(TRUE) ) {
    		
	        List<Leilao> lista = service.findReadyToClose();
	
	        for (Leilao l : lista) {
	            l.setStatus("FE");
	            service.save(l);
	        }
	
	        serviceEmail.fechamentoLeiloes(lista.size());
    	}
    	
    }

}
