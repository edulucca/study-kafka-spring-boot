package org.lucca.model;


import jakarta.persistence.*;
import lombok.*;
import org.lucca.dto.ShopDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;
    private String status;

    @Column(name = "date_shop")
    private LocalDate dateShop;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shop")
    private List<ShopItem> items;

    public static Shop fromShopDTO(ShopDTO shopDTO){
        return Shop.builder()
                .identifier(shopDTO.getIdentifier())
                .status(shopDTO.getStatus())
                .dateShop(shopDTO.getDateShop())
                .items(shopDTO.getItems()
                        .stream()
                        .map(ShopItem::fromShopItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }


}
