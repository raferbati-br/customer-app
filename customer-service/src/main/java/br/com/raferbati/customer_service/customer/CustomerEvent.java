package br.com.raferbati.customer_service.customer;

import java.time.Instant;

public class CustomerEvent {

    private String eventType;
    private Instant timestamp;
    private Long customerId;
    private String name;
    private String email;
    private String document;
    private String phone;

    public CustomerEvent() {}

    public static CustomerEvent of(String eventType, Customer c) {
        CustomerEvent e = new CustomerEvent();
        e.eventType = eventType;
        e.timestamp = Instant.now();
        e.customerId = c.getId();
        e.name = c.getName();
        e.email = c.getEmail();
        e.document = c.getDocument();
        e.phone = c.getPhone();
        return e;
    }

    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public Long getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDocument() { return document; }
    public String getPhone() { return phone; }

    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setDocument(String document) { this.document = document; }
    public void setPhone(String phone) { this.phone = phone; }
}
