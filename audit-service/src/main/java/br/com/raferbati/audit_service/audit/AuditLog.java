package br.com.raferbati.audit_service.audit;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false, length = 80)
    private String eventType;

    @Column(name = "event_ts", nullable = false)
    private Instant timestamp;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "document", length = 120)
    private String document;

    @Column(name = "phone", length = 120)
    private String phone;

    public AuditLog() {}

    public Long getId() { return id; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public Long getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDocument() { return document; }
    public String getPhone() { return phone; }

    public void setId(Long id) { this.id = id; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setDocument(String document) { this.document = document; }
    public void setPhone(String phone) { this.phone = phone; }
}
