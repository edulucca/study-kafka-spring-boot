package org.lucca.shopvalidator.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
    Não há necessidade de metodo de conversão em classe de modelo, pois não há
    mapeamento dessa objeto no banco de dados deste projeto.
    Os dados deste objeto decorrem diretamente do consumo do kafka
 */
@Getter
@Setter
public class ShopDTO {
    private String identifier;
    private String status;
    private LocalDate dateShop;
    private List<ShopItemDTO> items = new ArrayList<>();
}
