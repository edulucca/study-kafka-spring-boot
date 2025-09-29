package org.lucca.model;



import jakarta.persistence.*;
import lombok.*;
import org.lucca.dto.ShopItemDTO;


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

    private Float price;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public static ShopItem fromShopItemDTO(ShopItemDTO shopItemDTO){
        return ShopItem.builder()
                .productIdentifier(shopItemDTO.getProductIdentifier())
                .amount(shopItemDTO.getAmount())
                .price(shopItemDTO.getPrice())
                .build();
    }

}
