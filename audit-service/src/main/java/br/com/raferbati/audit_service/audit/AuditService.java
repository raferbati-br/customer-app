package br.com.raferbati.audit_service.audit;

import br.com.raferbati.audit_service.customer.CustomerEvent;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public List<AuditLog> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }

    @Transactional
    public void logEvent(CustomerEvent event) {
        AuditLog logEntry = new AuditLog();
        
        // Mapeamento (Event -> Entity)
        // Isso centraliza a lógica de conversão aqui
        logEntry.setEventType(event.getEventType());
        logEntry.setCustomerId(event.getCustomerId());
        logEntry.setName(event.getName());
        logEntry.setEmail(event.getEmail());
        logEntry.setDocument(event.getDocument());
        logEntry.setPhone(event.getPhone());
        
        // Garante que se o evento vier sem data, usamos o momento atual
        logEntry.setTimestamp(event.getTimestamp() != null ? event.getTimestamp() : java.time.Instant.now());

        repository.save(logEntry);
    }
}