package br.com.bioregistro.flowbank.form.Boleto.BB;

import br.com.bioregistro.flowbank.service.client.strategy.interfaces.ClientBankResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@RegisterForReflection
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoletoBancoBrasilResponse implements ClientBankResponse {


}
