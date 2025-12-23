package br.com.raferbati.customer_service.customer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static br.com.raferbati.customer_service.customer.CustomerAmqpConfig.*;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public CustomerService(CustomerRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Transactional
    public Customer create(Customer customer) {
        // 1. Salva no banco
        Customer saved = repository.save(customer);

        // 2. Cria o evento
        CustomerEvent event = CustomerEvent.of("CUSTOMER_CREATED", saved);

        // 3. Publica (O RabbitTemplate já converte pra JSON sozinho!)
        rabbitTemplate.convertAndSend(EXCHANGE_CUSTOMERS, ROUTING_CUSTOMER_CREATED, event);

        return saved;
    }

    @Transactional
    public Customer update(Long id, Customer customerData) {
        Customer existing = findById(id);
        
        // Atualiza dados
        existing.setName(customerData.getName());
        existing.setEmail(customerData.getEmail());
        existing.setDocument(customerData.getDocument());
        existing.setPhone(customerData.getPhone());

        Customer saved = repository.save(existing);

        CustomerEvent event = CustomerEvent.of("CUSTOMER_UPDATED", saved);
        rabbitTemplate.convertAndSend(EXCHANGE_CUSTOMERS, ROUTING_CUSTOMER_UPDATED, event);

        return saved;
    }

    @Transactional
    public void delete(Long id) {
        Customer existing = findById(id);
        repository.delete(existing);

        CustomerEvent event = CustomerEvent.of("CUSTOMER_DELETED", existing);
        rabbitTemplate.convertAndSend(EXCHANGE_CUSTOMERS, ROUTING_CUSTOMER_DELETED, event);
    }
}