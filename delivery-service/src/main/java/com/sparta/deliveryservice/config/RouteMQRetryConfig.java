package com.sparta.deliveryservice.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RouteMQRetryConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);


        // 재시도 정책 추가
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(3) // 총 3회 재시도
                .backOffOptions(5000, 2.0, 30000) // 초기 5초 대기, 2배식 증가, 최대 30초
                .recoverer(new RejectAndDontRequeueRecoverer()) // 3번 실패 후 DLQ로 보냄
                .build());
                //
        return factory;
    }
}
