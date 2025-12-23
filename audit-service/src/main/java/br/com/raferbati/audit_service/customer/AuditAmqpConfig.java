package br.com.raferbati.audit_service.customer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AuditAmqpConfig {

    public static final String EXCHANGE_CUSTOMERS = "customers.exchange";
    public static final String QUEUE_AUDIT_CUSTOMER_EVENTS = "audit.customer.events.q";

    // (Opcional, mas recomendado) declarar também no audit-service (idempotente)
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
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    
    // ✅ Faz o @RabbitListener usar JSON
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        return factory;
    }
}
