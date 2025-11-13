package com.sparta.deliveryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouteMQDeclarer implements ApplicationRunner {

    private final RabbitAdmin rabbitAdmin;
    private final TopicExchange deliveryExchange;

    private final Queue requestHubRoutesQueue;
    private final Queue responseHubRoutesQueue;
    private final Queue responseHubRoutesDLQ;

    private final Binding requestHubRoutesBinding;
    private final Binding responseHubRoutesBinding;
    private final Binding responseHubRoutesDLQBinding;

    // ✨ 이제 생성자 주입은 순환 참조를 일으키지 않습니다.
    // RabbitMQConfig 빈이 아닌, 이미 생성된 RabbitAdmin 빈 등을 주입받기 때문입니다.


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 컨텍스트가 완전히 뜬 후, 큐 선언 실행

        // 교환기 선언
        rabbitAdmin.declareExchange(deliveryExchange);

        // 각 큐 및 바인딩 선언
        rabbitAdmin.declareQueue(requestHubRoutesQueue);
        rabbitAdmin.declareQueue(responseHubRoutesQueue);
        rabbitAdmin.declareQueue(responseHubRoutesDLQ);

        rabbitAdmin.declareBinding(requestHubRoutesBinding);
        rabbitAdmin.declareBinding(responseHubRoutesBinding);
        rabbitAdmin.declareBinding(responseHubRoutesDLQBinding);

        System.out.println("✅ RabbitMQ components declared successfully (via ApplicationRunner).");
    }
}