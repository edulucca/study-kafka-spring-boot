package org.lucca.shopvalidator.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lucca.shopvalidator.dto.ShopDTO;
import org.lucca.shopvalidator.dto.ShopItemDTO;
import org.lucca.shopvalidator.model.Product;
import org.lucca.shopvalidator.repository.ProductRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiveKafkaMessage {
    /** Topico que recebe a mensagem sobre uma compra **/
    private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";

    /** Topico que indica se a compra foi efetuada com sucesso ou não **/
    private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";

    /** Objeto de Acesso a base de dados **/
    private final ProductRepository productRepository;

    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    /** Método que "escuta" se o topico SHOP_TOPIC recebeu uma mensagem
     *
     * O parâmetro do tipo ShopDTO se dá pois o Kafka vai ler exatamente esse padrão, pois foi produzido nesse padrão.
     * Aqui nasce novamente a necessidade de criar uma lib
     *
     * Lógica: Verifica se os produtos da compra existem na quantidade solicitada. Caso existam, retorna SUCCESS,
     * caso contrário retorna ERROR.
     *
     * Retorna o mesmo objeto (opcional)
     * **/

    @KafkaListener(topics = SHOP_TOPIC_NAME, groupId = "group")
    public void listenShopTopic(ShopDTO shopDTO){
        try {
            log.info("Compra detectada no tópico: {}.", shopDTO.getIdentifier());

            boolean success = true;
            for (ShopItemDTO shopItemDTO : shopDTO.getItems()) {
                Product product = productRepository.findByProductIdentifier(shopItemDTO.getProductIdentifier());

                if (!isValidShop(shopItemDTO, product)) {
                    shopError(shopDTO);
                    success = false;
                    break;
                }
            }

            if (success) {
                shopSucces(shopDTO);
            }
        } catch (Exception erro){
            log.error("Erro no processamento da compra {}.", shopDTO.getIdentifier());
            log.error("Erro: {}", erro.getMessage());
        }
    }

    private void shopError(ShopDTO shopDTO) {
        log.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());

        shopDTO.setStatus("ERROR");

        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }

    private boolean isValidShop(ShopItemDTO item, Product product){
        return product != null && product.getAmount() >= item.getAmount();
    }

    private void shopSucces(ShopDTO shopDTO){
        log.info("Compra {} efetuada com sucesso", shopDTO.getIdentifier());

        shopDTO.setStatus("SUCCESS");

        kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
    }
}
