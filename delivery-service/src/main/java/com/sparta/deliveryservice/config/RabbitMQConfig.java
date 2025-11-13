package com.sparta.deliveryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "delivery.exchange";

    // 주문 생성 -> 배송
    public static final String ORDER_CREATED_QUEUE_NAME = "delivery.queue.order.created";
    public static final String ORDER_CREATED_ROUTING_KEY = "delivery.order.created";

    // 배송 생성 -> 주문
    public static final String DELIVERY_CREATED_QUEUE_NAME = "order.queue.delivery.created";
    public static final String DELIVERY_CREATED_ROUTING_KEY = "order.delivery.created"; // 예시

    // 배송 완료 -> 주문
    public static final String DELIVERY_COMPLETED_QUEUE_NAME = "order.queue.delivery.completed";
    public static final String DELIVERY_COMPLETED_ROUTING_KEY = "order.delivery.completed"; // 예시

    // === 공통 설정 ===
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // === 개별 큐 정의 ===
    @Bean
    public Queue deliveryCreatedQueue() {
        return new Queue(DELIVERY_CREATED_QUEUE_NAME, true);
    }

    @Bean
    public Queue deliveryCompletedQueue() {
        return new Queue(DELIVERY_COMPLETED_QUEUE_NAME, true); // durable 큐
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(ORDER_CREATED_QUEUE_NAME, true); // durable 큐
    }


    // === 큐 - 익스체인지 바인딩 ===
    @Bean
    public Binding deliveryCreatedBinding(Queue deliveryCreatedQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(deliveryCreatedQueue).to(deliveryExchange).with(DELIVERY_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding deliveryCompletedBinding(Queue deliveryCompletedQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(deliveryCompletedQueue).to(deliveryExchange).with(DELIVERY_COMPLETED_ROUTING_KEY);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(deliveryExchange).with(ORDER_CREATED_ROUTING_KEY);
    }

    // === RabbitAdmin 등록 ===

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
