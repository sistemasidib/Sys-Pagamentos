package br.com.bioregistro.flowbank.scheduler;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import br.com.bioregistro.flowbank.service.client.BB.BancoBrasilService;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BoletoScheduler {

    private final BancoBrasilService boletoService;

    public BoletoScheduler(BancoBrasilService boletoService) {
        this.boletoService = boletoService;
    }

    // Roda todo dia às 2h da manhã
    @Scheduled(cron = "0 0 2 * * ?")
    void agendarBaixaBoleto() {
        // Monta os dados que precisa
        HttpServerRequest request = null; // se não precisar, pode passar null
        Long idEdital = 123L;
        ConcursoBancoLogin login = new ConcursoBancoLogin();


        boletoService.baixaBoleto(request, idEdital, login);
    }
}