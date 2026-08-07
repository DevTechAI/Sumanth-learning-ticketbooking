package com.rabbitmq.producer.rabbitMQ;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig
{

    public static final String EXCHANGE_NAME = "order.exchange";

    public static final String QUEUE_NAME = "order.notification";

    public static final String ROUTING_KEY =
            "order.notification.created";

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(
                EXCHANGE_NAME,
                true,
                false
        );
    }

    @Bean
    public Queue orderNotificationQueue() {
        return new Queue(
                QUEUE_NAME,
                true
        );
    }

    @Bean
    public Binding orderNotificationBinding(
            Queue orderNotificationQueue,
            DirectExchange orderExchange) {

        return BindingBuilder
                .bind(orderNotificationQueue)
                .to(orderExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }




//        @Value("${app.rabbitmq.queue}")
//        private String queueName;
//
//        @Value("${app.rabbitmq.exchange}")
//        private String exchangeName;
//
//        @Value("${app.rabbitmq.routingkey}")
//        private String routingKey;
//
//        @Bean
//        public Queue queue() {
//            return new Queue(queueName, true); // Durable queue
//        }
//
//        @Bean
//        public DirectExchange exchange() {
//            return new DirectExchange(exchangeName);
//        }
//
//        @Bean
//        public Binding binding(Queue queue, DirectExchange exchange) {
//            return BindingBuilder.bind(queue).to(exchange).with(routingKey);
//        }
//
//        @Bean
//        public MessageConverter jsonMessageConverter() {
//            return new JacksonJsonMessageConverter();
//        }
//
//        @Bean
//        public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
//            final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//            rabbitTemplate.setMessageConverter(jsonMessageConverter());
//            return rabbitTemplate;
//        }

}
