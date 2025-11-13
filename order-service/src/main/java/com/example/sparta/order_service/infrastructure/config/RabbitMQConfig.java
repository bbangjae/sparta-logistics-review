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

    @Value("${mq.order.exchange}")
    private String ORDER_EXCHANGE;
    @Value("${mq.delivery.exchange}")
    private String DELIVERY_EXCHANGE;
    @Value("${mq.order.queue.delivery.created}")
    private String DELIVERY_CREATED_QUEUE;
    @Value("${mq.order.queue.delivery.completed}")
    private String DELIVERY_COMPLETED_QUEUE;
    @Value("${mq.order.routing_key.delivery.created}")
    private String DELIVERY_CREATED_ROUTING_KEY;
    @Value("${mq.order.routing_key.delivery.completed}")
    private String DELIVERY_COMPLETED_ROUTING_KEY;

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean TopicExchange deliveryExchange() {
        return new TopicExchange(DELIVERY_EXCHANGE);
    }

    @Bean
    public Queue deliveryCreatedQueue() {
        return new Queue(DELIVERY_CREATED_QUEUE);
    }

    @Bean
    public Queue deliveryCompletedQueue() {
        return new Queue(DELIVERY_COMPLETED_QUEUE);
    }

    // 소비
    // delivery.exchange로 오는 메시지 중 order.delivery.key 라우팅 키를 가진 것을 order.queue.delivery.created 큐로 보내 달라고 신청
    @Bean
    public Binding deliveryCreatedBinding() {
        return BindingBuilder.bind(deliveryCreatedQueue())
                .to(deliveryExchange())
                .with(DELIVERY_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding deliveryCompletedBinding() {
        return BindingBuilder.bind(deliveryCompletedQueue())
                .to(deliveryExchange())
                .with(DELIVERY_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
