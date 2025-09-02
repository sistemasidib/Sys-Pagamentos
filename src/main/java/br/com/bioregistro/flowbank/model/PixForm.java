package br.com.bioregistro.flowbank.model;



import br.com.bioregistro.flowbank.form.Boleto.Bradesco.BoletoBradescoResponse;

import java.time.LocalDateTime;
import java.util.Date;

public record PixForm(
       br.com.bioregistro.flowbank.model.enuns.TypeOperation operation,
       BoletoBradescoResponse.Pedido pedido,
       Integer inscricao,
       LocalDateTime dataCriacao

) {
    public PixForm (BoletoBradescoResponse response,Integer inscricao,br.com.bioregistro.flowbank.model.enuns.TypeOperation operation){
        this(operation,response.getPedido(),inscricao,LocalDateTime.now());
    }
}
