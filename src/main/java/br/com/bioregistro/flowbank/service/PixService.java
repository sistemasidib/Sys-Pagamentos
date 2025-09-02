package br.com.bioregistro.flowbank.service;

import br.com.bio.registro.core.runtime.entities.idecan.dbo.Pix;
import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;
import br.com.bioregistro.flowbank.model.PixForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PixService {

    @Transactional
    public Integer createPix(PixForm pixForm) {
        Pix pix = new Pix();
        pix.valor = pixForm.pedido().getValor();
        pix.numero = pixForm.pedido().getNumero();
        pix.descricao = pixForm.pedido().getDescricao();
        pix.dataRegistro = pixForm.dataCriacao();
        pix.persist();
        return pix.Id;
    }
}
