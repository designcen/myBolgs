package com.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author cenkang
 * @Date 2020/2/19 15:24
 */
@Configuration
public class RabbitMqConfig {
    // 队列名称
    public final static String ES_QUEUE = "es_queue";
    // 交换机名称
    public final static String ES_EXCHANGE = "es_exchange";
    // 路由键名称
    public final static String ES_BIND_KEY = "es_index_message";
    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue exQueue() {
        return new Queue(ES_QUEUE);
    }

    /**
     * 声明交换机
     * @return
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(ES_EXCHANGE);
    }

    /**
     * 绑定交换机和队列
     * @param exQueue
     * @param exchange
     * @return
     */
    @Bean
    Binding bindingExchangeMessage(Queue exQueue, DirectExchange exchange) {
        return BindingBuilder.bind(exQueue).to(exchange).with(ES_BIND_KEY);
    }
}
