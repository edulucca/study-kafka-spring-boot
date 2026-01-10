package org.lucca.events;




import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.lucca.dto.ShopDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /*
        Define propriedades de acesso ao Kafka
     */
    public ProducerFactory<String, ShopDTO> producerFactory(){
        Map<String, Object> props = new HashMap<>();

        //endereco do Kafka
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        //tipo de chave
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        //tipo de Mensagem (JSON)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        //identificador para o produtor
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "shop-api");

        return new DefaultKafkaProducerFactory<>(props);
    }

    /*
        Retorna as definicoes do metodo ProducerFactory. Como Bean para que possa ser usado em outros pontos da aplicação
     */
    @Bean
    public KafkaTemplate<String, ShopDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /*
        Define as propriedades de consumo de mensagens
     */
    public ConsumerFactory<String, ShopDTO> consumerFactory(){
        JsonDeserializer<ShopDTO> deserializer = new JsonDeserializer<>(ShopDTO.class);

        deserializer.addTrustedPackages("*");

        deserializer.setUseTypeHeaders(false);

        ErrorHandlingDeserializer<ShopDTO> errorDeserializer = new ErrorHandlingDeserializer<>(deserializer);

        Map<String, Object> props = new HashMap<>();

        //Define endereço do kakfa
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), errorDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShopDTO> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, ShopDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
