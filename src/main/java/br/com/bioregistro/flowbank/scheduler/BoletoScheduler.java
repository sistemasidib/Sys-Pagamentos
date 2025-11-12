package br.com.bioregistro.flowbank.scheduler;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.ConcursoBancoLogin;
import br.com.bio.registro.core.runtime.entities.idecan.dbo.Edital;
import br.com.bioregistro.flowbank.service.client.BB.BancoBrasilService;
import br.com.bioregistro.flowbank.service.client.BB.BbTokenService;
import br.com.bioregistro.flowbank.service.client.strategy.ClientStrategyFactory;
import io.quarkus.scheduler.Scheduled;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BoletoScheduler {

    private final BancoBrasilService boletoService;
    @Inject
    BbTokenService bbTokenService;

    public BoletoScheduler(BancoBrasilService boletoService) {
        this.boletoService = boletoService;
    }

    @Inject
    ClientStrategyFactory strategyFactory;

    @Scheduled(every = "45s")
    void agendarBaixaBoleto() {
        List<Edital> editais = Edital.listAbertosAgora();
        System.out.println("[SCHEDULER] Editais abertos: " + editais.size());

        for (Edital edital : editais) {
            Integer editalId = edital.ediId;
            System.out.println("[SCHEDULER] Rodando baixa para edital " + edital.ediNomeConcurso + " (id=" + editalId + ")");

            // Pode passar null - o service vai usar BbTokenService
            strategyFactory.baixaParaTodosOsBancosDoEdital(editalId, null);
        }}
}