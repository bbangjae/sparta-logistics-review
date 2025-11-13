package com.example.sparta.hub_route_service.infrastructure.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 큐 이름 상수
    public static final String HUB_ROUTES_REQUEST_QUEUE = "hub.routes.request.queue";
    public static final String DELIVERY_EXCHANGE = "delivery.exchange";
    public static final String HUB_ROUTES_REQUEST_ROUTING_KEY = "hub.routes.request";

    /**
     * 허브 경로 요청을 받을 큐
     */
    @Bean
    public Queue hubRoutesRequestQueue() {
        return new Queue(HUB_ROUTES_REQUEST_QUEUE, true);
    }

    /**
     * 배송 서비스의 Exchange
     */
    @Bean
    public DirectExchange deliveryExchange() {
        return new DirectExchange(DELIVERY_EXCHANGE);
    }

    /**
     * 큐와 Exchange 바인딩
     */
    @Bean
    public Binding hubRoutesRequestBinding(
        Queue hubRoutesRequestQueue,
        DirectExchange deliveryExchange
    ) {
        return BindingBuilder
            .bind(hubRoutesRequestQueue)
            .to(deliveryExchange)
            .with(HUB_ROUTES_REQUEST_ROUTING_KEY);
    }

    /**
     * JSON 메시지 컨버터
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * RabbitMQ 리스너 컨테이너 팩토리 설정
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory connectionFactory,
        MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
            new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
