package org.lucca.controller;

import lombok.RequiredArgsConstructor;
import org.lucca.dto.ShopDTO;
import org.lucca.model.Shop;
import org.lucca.model.ShopItem;
import org.lucca.repository.ShopRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopRepository shopRepository;

    @GetMapping
    public List<ShopDTO> getShop() {
        return shopRepository.findAll()
                .stream()
                .map(ShopDTO::fromShop)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ShopDTO saveShop(@RequestBody ShopDTO shopDTO) {
        shopDTO.setIdentifier(UUID.randomUUID().toString());
        shopDTO.setDateShop(LocalDate.now());
        shopDTO.setStatus("PENDING");

        Shop shop = Shop.fromShopDTO(shopDTO);

        for(ShopItem shopItem : shop.getItems()){
            shopItem.setShop(shop);
        }

        return ShopDTO.fromShop(shopRepository.save(shop));
    }
}
