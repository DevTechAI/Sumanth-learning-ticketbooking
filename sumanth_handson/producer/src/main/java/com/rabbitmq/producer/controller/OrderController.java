package com.rabbitmq.producer.controller;

import com.rabbitmq.producer.rabbitMQ.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.rabbitmq.producer.dto.OrderMessage;
import com.rabbitmq.producer.service.OrderPublisher;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderPublisher orderPublisher;

    public OrderController(OrderPublisher orderPublisher) {
        this.orderPublisher = orderPublisher;
    }

    @PostMapping("/publish")
    public ResponseEntity<Map<String, Object>> publishOrder(
            @Valid @RequestBody OrderMessage orderMessage) {

        orderPublisher.publishOrder(orderMessage);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Order published successfully");
        response.put("orderId", orderMessage.getOrderId());
        response.put("exchange", "order.exchange");
        response.put(
                "routingKey",
                "order.notification.created"
        );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(response);
    }
}