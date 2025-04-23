package org.kammerer_neuhold.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue userStatusQueue() {
        return new Queue("user.status.queue", true);
    }

    @Bean
    public Exchange userStatusExchange() {
        return ExchangeBuilder.directExchange("user.status.exchange")
                .durable(true)
                .build();
    }

    @Bean
    public Binding userStatusBinding(Queue userStatusQueue, Exchange userStatusExchange) {
        return BindingBuilder.bind(userStatusQueue)
                .to(userStatusExchange)
                .with("user.status.update")
                .noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}