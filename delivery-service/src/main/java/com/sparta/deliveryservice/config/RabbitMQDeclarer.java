package com.sparta.deliveryservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQDeclarer implements ApplicationRunner {

    private final RabbitAdmin rabbitAdmin;
    private final TopicExchange deliveryExchange;

    private final Queue deliveryCreatedQueue;
    private final Queue deliveryCompletedQueue;
    private final Queue orderCreatedQueue;

    private final Binding deliveryCreatedBinding;
    private final Binding deliveryCompletedBinding;
    private final Binding orderCreatedBinding;

    // ✨ 이제 생성자 주입은 순환 참조를 일으키지 않습니다.
    // RabbitMQConfig 빈이 아닌, 이미 생성된 RabbitAdmin 빈 등을 주입받기 때문입니다.


    public RabbitMQDeclarer(RabbitAdmin rabbitAdmin, TopicExchange deliveryExchange, Queue deliveryCreatedQueue, Queue deliveryCompletedQueue, Queue orderCreatedQueue, Binding deliveryCreatedBinding, Binding deliveryCompletedBinding, Binding orderCreatedBinding) {
        this.rabbitAdmin = rabbitAdmin;
        this.deliveryExchange = deliveryExchange;
        this.deliveryCreatedQueue = deliveryCreatedQueue;
        this.deliveryCompletedQueue = deliveryCompletedQueue;
        this.orderCreatedQueue = orderCreatedQueue;
        this.deliveryCreatedBinding = deliveryCreatedBinding;
        this.deliveryCompletedBinding = deliveryCompletedBinding;
        this.orderCreatedBinding = orderCreatedBinding;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 컨텍스트가 완전히 뜬 후, 큐 선언 실행

        // 교환기 선언
        rabbitAdmin.declareExchange(deliveryExchange);

        // 각 큐 및 바인딩 선언
        rabbitAdmin.declareQueue(deliveryCreatedQueue);
        rabbitAdmin.declareQueue(deliveryCompletedQueue);
        rabbitAdmin.declareQueue(orderCreatedQueue);

        rabbitAdmin.declareBinding(deliveryCreatedBinding);
        rabbitAdmin.declareBinding(deliveryCompletedBinding);
        rabbitAdmin.declareBinding(orderCreatedBinding);

        System.out.println("✅ RabbitMQ components declared successfully (via ApplicationRunner).");
    }
}