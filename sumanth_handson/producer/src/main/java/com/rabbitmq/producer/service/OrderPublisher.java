package com.rabbitmq.producer.service;

import com.rabbitmq.producer.rabbitMQ.RabbitMQConfig;
import com.rabbitmq.producer.dto.OrderMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderPublisher
{
    private final RabbitTemplate rabbitTemplate;

    public OrderPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrder(OrderMessage orderMessage) {

        orderMessage.setStatus("CREATED");
        orderMessage.setCreatedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                orderMessage
        );

        System.out.println(
                "Order message published successfully: "
                        + orderMessage.getOrderId()
        );
    }
}