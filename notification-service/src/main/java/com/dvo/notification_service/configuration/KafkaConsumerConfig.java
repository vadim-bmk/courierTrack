package com.dvo.notification_service.configuration;

import com.dvo.notification_service.event.DeliveryAssignmentsEvent;
import com.dvo.notification_service.event.OrderChangedOrderItem;
import com.dvo.notification_service.event.OrderStatusChangedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupID;

    @Bean
    public ConsumerFactory<String, OrderStatusChangedEvent> orderStatusChangedEventConsumerFactory() {
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(OrderStatusChangedEvent.class, false)
        );
    }

    @Bean(name = "orderStatusChangedKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, OrderStatusChangedEvent> orderStatusChangedKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderStatusChangedEvent>();
        factory.setConsumerFactory(orderStatusChangedEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderChangedOrderItem> orderItemAddedEventConsumerFactory() {
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(OrderChangedOrderItem.class, false)
        );
    }

    @Bean(name = "orderItemAddedKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, OrderChangedOrderItem> orderItemAddedKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, OrderChangedOrderItem>();
        factory.setConsumerFactory(orderItemAddedEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, DeliveryAssignmentsEvent> deliveryAssignmentsEventConsumerFactory(){
        Map<String, Object> config = baseConsumerConfig();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                new JsonDeserializer<>(DeliveryAssignmentsEvent.class, false)
        );
    }

    @Bean(name = "deliveryAssignmentsKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, DeliveryAssignmentsEvent> deliveryAssignmentsKafkaListenerContainerFactory (){
        var factory = new ConcurrentKafkaListenerContainerFactory<String, DeliveryAssignmentsEvent>();
        factory.setConsumerFactory(deliveryAssignmentsEventConsumerFactory());
        return factory;
    }

    private Map<String, Object> baseConsumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return config;
    }
}
