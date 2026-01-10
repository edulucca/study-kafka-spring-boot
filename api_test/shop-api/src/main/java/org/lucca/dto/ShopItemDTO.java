package org.lucca.dto;

import lombok.*;
import org.lucca.model.ShopItem;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
