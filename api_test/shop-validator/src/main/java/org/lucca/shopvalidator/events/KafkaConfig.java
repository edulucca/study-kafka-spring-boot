package org.lucca.shopvalidator.events;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.lucca.shopvalidator.dto.ShopDTO;
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

/*
    Como esta classe é idêntica a do projeto shop-api, ela poderia fazer parte de uma lib a parte
    A lib pode absorver tanto a classe KafkaConfig quanto as classes DTO's
 */
@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /*
        Propriedades de acesso ao Kafka
     */
    @Bean
    public ProducerFactory<String, ShopDTO> producerFactory(){
        Map<String, Object> props = new HashMap<>();

        //endereço do Kafka
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

        //tipo de chave
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        //tipo de Mensagem (JSON)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        //identificador
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "shop-api");

        return new DefaultKafkaProducerFactory<>(props);
    }

    /*
        Retorna as definicoes do metodo producerFactory. Como Bean para que possa ser usado em outros pontos da aplicação
     */
    @Bean
    public KafkaTemplate<String, ShopDTO> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    /*
        Define as propriedades de consumo de mensagens
     */
    public ConsumerFactory<String, ShopDTO> consumerFactory(){
        //Padrao DTO
        JsonDeserializer<ShopDTO> deserializer = new JsonDeserializer<>(ShopDTO.class);
        //Confia em qualquer pacote
        deserializer.addTrustedPackages("*");
        //Ignora o header que diz "org.lucca.dto..." e força o uso do DTO local
        deserializer.setUseTypeHeaders(false);
        //Evita loop infinito em caso de erro de deserializer
        ErrorHandlingDeserializer<ShopDTO> errorDeserializer = new ErrorHandlingDeserializer<>(deserializer);


        //Mapa de propriedades do Kafka
        Map<String, Object> props = new HashMap<>();

        //Configura endereco consumo
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);



        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    /*
        Retorna as definicoes do metodo ConsumerFactory. Como Bean para que possa ser usado em outros pontos da aplicação
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShopDTO> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, ShopDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
