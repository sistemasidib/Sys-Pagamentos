package br.com.bioregistro.flowbank.service.client.Checkout.model.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductReq(
        String name,
        BigDecimal price,
        String branchId,
        Map<String, String> metadata,
        List<PriceFee> priceFees
) {

}
