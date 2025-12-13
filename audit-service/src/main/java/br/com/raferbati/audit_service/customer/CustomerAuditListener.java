package br.com.raferbati.audit_service.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuditListener {

    private static final Logger log = LoggerFactory.getLogger(CustomerAuditListener.class);

    @RabbitListener(queues = AuditAmqpConfig.QUEUE_AUDIT_CUSTOMER_EVENTS)
    public void onCustomerEvent(CustomerEvent event) {
        log.info(
                "AUDIT LOG: type={} id={} name={} email={} document={} timestamp={}",
                event.getEventType(),
                event.getCustomerId(),
                event.getName(),
                event.getEmail(),
                event.getDocument(),
                event.getTimestamp()
        );
    }
}
