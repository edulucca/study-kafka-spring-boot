package org.lucca.model;

import jakarta.persistence.*;
import lombok.*;
import org.lucca.dto.ShopItemDTO;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@Entity(name = "shop_item")
@NoArgsConstructor
@AllArgsConstructor
public class ShopItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_identifier")
    private String productIdentifier;

    private int amount;

    private float price;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Long shopId;

    public static ShopItem fromShopItemDTO(ShopItemDTO shopItemDTO){
        return ShopItem.builder()
                .productIdentifier(shopItemDTO.getProductIdentifier())
                .amount(shopItemDTO.getAmount())
                .price(shopItemDTO.getPrice())
                .build();
    }

}
