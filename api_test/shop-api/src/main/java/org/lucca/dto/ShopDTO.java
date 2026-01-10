package org.lucca.dto;

import lombok.*;
import org.lucca.model.Shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDTO {
    private String identifier;
    private String status;
    private LocalDate dateShop;
    private List<ShopItemDTO> items = new ArrayList<>();

    public static ShopDTO fromShop(Shop shop){
        return ShopDTO.builder()
                .identifier(shop.getIdentifier())
                .status(shop.getStatus())
                .dateShop(shop.getDateShop())
                .items(shop.getItems()
                        .stream()
                        .map(ShopItemDTO::fromShopItem)
                        .collect(Collectors.toList()))
                .build();
    }
}
