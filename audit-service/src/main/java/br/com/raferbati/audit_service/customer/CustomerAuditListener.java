package br.com.raferbati.audit_service.customer;

import br.com.raferbati.audit_service.audit.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component; // Mudei de @Service para @Component (semântica melhor)

@Component
public class CustomerAuditListener {

    private static final Logger log = LoggerFactory.getLogger(CustomerAuditListener.class);

    private final AuditService auditService;

    public CustomerAuditListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @RabbitListener(queues = AuditAmqpConfig.QUEUE_AUDIT_CUSTOMER_EVENTS)
    public void onCustomerEvent(CustomerEvent event) {
        log.info("Received event: type={} customerId={}", event.getEventType(), event.getCustomerId());
        
        // Apenas delega. Se houver erro, o RabbitMQ vai tentar reenviar (dependendo da config)
        auditService.logEvent(event);
    }
}