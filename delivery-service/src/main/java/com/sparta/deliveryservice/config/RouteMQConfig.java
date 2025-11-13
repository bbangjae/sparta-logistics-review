package com.sparta.deliveryservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteMQConfig {

    public static final String EXCHANGE_NAME = "delivery.exchange";

    // 배송 생성 중 경로 요청 -> 허브
    public static final String REQUEST_HUB_ROUTES_QUEUE_NAME = "hub.queue.delivery.route.request";
    public static final String REQUEST_HUB_ROUTES_ROUTING_KEY = "hub.delivery.route.request";

    // 요청에 대한 허브 경로 계산 완료 -> 배송
    public static final String RESPONSE_HUB_ROUTES_QUEUE_NAME = "delivery.queue.hub.route.created";
    public static final String RESPONSE_HUB_ROUTES_ROUTING_KEY = "delivery.hub.route.created"; // 예시


    // DLQ (Dead Letter Queue)
    public static final String RESPONSE_HUB_ROUTES_DLQ_NAME = "delivery.queue.hub.route.created.dlq";
    public static final String RESPONSE_HUB_ROUTES_DLQ_ROUTING_KEY = "delivery.hub.route.created.dlq";

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
    public Queue requestHubRoutesQueue() {

        return new Queue(REQUEST_HUB_ROUTES_QUEUE_NAME, true);
    }

    // === 응답 큐 (DLQ 설정 추가됨) ===
    @Bean
    public Queue responseHubRoutesQueue() {

//        return new Queue(RESPONSE_HUB_ROUTES_QUEUE_NAME, true); // durable 큐
        return QueueBuilder.durable(RESPONSE_HUB_ROUTES_QUEUE_NAME)
                // 실패 시 메시지를 보낼 DLX 설정
                .withArgument("x-dead-letter-exchange", EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", RESPONSE_HUB_ROUTES_DLQ_ROUTING_KEY)
                // 재시도 전 대기 시간 (ms)
                .withArgument("x-message-ttl", 10_000) // optional: 재시도 간격
                .build();
    }

    // === DLQ 정의 ===
    @Bean
    public Queue responseHubRoutesDLQ() {
        return QueueBuilder.durable(RESPONSE_HUB_ROUTES_DLQ_NAME).build();
    }


    // === 큐 - 익스체인지 바인딩 ===
    @Bean
    public Binding requestHubRoutesBinding(Queue requestHubRoutesQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(requestHubRoutesQueue).to(deliveryExchange).with(REQUEST_HUB_ROUTES_ROUTING_KEY);
    }

    @Bean
    public Binding responseHubRoutesBinding(Queue responseHubRoutesQueue, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(responseHubRoutesQueue).to(deliveryExchange).with(RESPONSE_HUB_ROUTES_ROUTING_KEY);
    }

    @Bean
    public Binding responseHubRoutesDLQBinding(Queue responseHubRoutesDLQ, TopicExchange deliveryExchange) {
        return BindingBuilder.bind(responseHubRoutesDLQ)
                .to(deliveryExchange)
                .with(RESPONSE_HUB_ROUTES_DLQ_ROUTING_KEY);
    }

    // === RabbitAdmin 등록 ===

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }
}
