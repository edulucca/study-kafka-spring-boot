package org.lucca.shopvalidator.dto;

import lombok.Getter;
import lombok.Setter;

/*
    Não há necessidade de metodo de conversão em classe de modelo, pois não há
    mapeamento dessa objeto no banco de dados deste projeto.
    Os dados deste objeto decorrem diretamente do consumo do kafka
 */
@Getter
@Setter
public class ShopItemDTO {
    private String productIdentifier;
    private Integer amount;
    private float price;
}
