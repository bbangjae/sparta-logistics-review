package com.example.sparta.order_service.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private String ORDER_EXCHANGE;
    private String QUEUE_NAME;
    private String ROUTING_KEY;

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order.exchange");
    }

    @Bean TopicExchange deliveryExchange() {
        return new TopicExchange("delivery.exchange");
    }

    @Bean
    public Queue orderQueueDeliveryCreated() {
        return new Queue("order.queue.delivery.created");
    }

    // 소비
    // delivery.exchange로 오는 메시지 중 order.delivery.key 라우팅 키를 가진 것을 order.queue.delivery.created 큐로 보내 달라고 신청
    @Bean
    public Binding deliveryCreatedBinding() {
        return BindingBuilder.bind(orderQueueDeliveryCreated())
                .to(deliveryExchange())
                .with("order.delivery.key");
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
