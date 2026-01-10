package org.lucca.events;

import lombok.RequiredArgsConstructor;
import org.lucca.dto.ShopDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
    Para enviar a mensagem, recebe ShopDTO e envia a mensagem para o tópico definido.
    Caso o tópico SHOP_TOPIC não exista, será criado automaticamente na primeira chamada
 */

@Service
@RequiredArgsConstructor
public class KafkaClient {
    private final KafkaTemplate<String, ShopDTO> kafkaTemplate;

    private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";

    public void sendMessage(ShopDTO msg){
        kafkaTemplate.send(SHOP_TOPIC_NAME, msg);
    }
}
