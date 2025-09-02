package org.lucca.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.lucca.model.ShopItem;

@Getter
@Setter
@Builder
public class ShopItemDTO {
    private String productIdentifier;
    private Integer amount;
    private float price;

    public static ShopItemDTO fromShopItem(ShopItem shopItem){
        return ShopItemDTO.builder()
                .productIdentifier(shopItem.getProductIdentifier())
                .amount(shopItem.getAmount())
                .price(shopItem.getPrice())
                .build();
    }
}
