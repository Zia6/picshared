package com.zhai.picshared.config;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "picshare.topic";
    public static final String QUEUE_PICTURE = "picshare.picture";

    // 多个 routing key（插入、编辑、删除）
    public static final String ROUTING_KEY_PREFIX = "picture.";  // 通配前缀

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue pictureQueue() {
        return new Queue(QUEUE_PICTURE);
    }

    @Bean
    public Binding pictureBinding() {
        return BindingBuilder.bind(pictureQueue())
                .to(topicExchange())
                .with("picture.*"); // 匹配所有 picture. 前缀的事件类型
    }

    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new SimpleMessageConverter());
        return rabbitTemplate;
    }
}

