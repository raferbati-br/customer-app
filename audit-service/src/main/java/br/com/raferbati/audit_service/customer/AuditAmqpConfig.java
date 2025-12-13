package br.com.raferbati.audit_service.customer;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditAmqpConfig {

    public static final String QUEUE_AUDIT_CUSTOMER_EVENTS = "audit.customer.events.q";

    @Bean
    public Queue auditCustomerQueue() {
        return new Queue(QUEUE_AUDIT_CUSTOMER_EVENTS, true);
    }
}
