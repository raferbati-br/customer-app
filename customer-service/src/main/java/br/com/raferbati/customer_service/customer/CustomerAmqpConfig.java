package br.com.raferbati.customer_service.customer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerAmqpConfig {

    public static final String EXCHANGE_CUSTOMERS = "customers.exchange";
    public static final String ROUTING_CUSTOMER_CREATED = "customer.created";
    public static final String ROUTING_CUSTOMER_UPDATED = "customer.updated";
    public static final String ROUTING_CUSTOMER_DELETED = "customer.deleted";

    public static final String QUEUE_AUDIT_CUSTOMER_EVENTS = "audit.customer.events.q";

    @Bean
    public TopicExchange customersExchange() {
        return new TopicExchange(EXCHANGE_CUSTOMERS);
    }

    @Bean
    public Queue auditCustomerQueue() {
        return new Queue(QUEUE_AUDIT_CUSTOMER_EVENTS, true);
    }

    @Bean
    public Binding auditCustomerBinding(Queue auditCustomerQueue,
                                        TopicExchange customersExchange) {
        return BindingBuilder
                .bind(auditCustomerQueue)
                .to(customersExchange)
                .with("customer.*");
    }

    // ✅ JSON converter
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // ✅ RabbitTemplate usando JSON
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
