package br.com.raferbati.customer_service.customer;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static br.com.raferbati.customer_service.customer.CustomerAmqpConfig.*;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private final CustomerRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public CustomerController(CustomerRepository repository,
                              RabbitTemplate rabbitTemplate,
                              ObjectMapper objectMapper) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public List<Customer> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody Customer customer) {
        customer.setId(null);
        Customer saved = repository.save(customer);

        CustomerEvent event = CustomerEvent.of("CUSTOMER_CREATED", saved);
        publishJsonEvent(ROUTING_CUSTOMER_CREATED, event);

        return saved;
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setDocument(customer.getDocument());
        existing.setPhone(customer.getPhone());

        Customer saved = repository.save(existing);

        CustomerEvent event = CustomerEvent.of("CUSTOMER_UPDATED", saved);
        publishJsonEvent(ROUTING_CUSTOMER_UPDATED, event);

        return saved;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        repository.delete(existing);

        CustomerEvent event = CustomerEvent.of("CUSTOMER_DELETED", existing);
        publishJsonEvent(ROUTING_CUSTOMER_DELETED, event);
    }

    /**
     * Publica o evento como JSON (String) no RabbitMQ.
     * Assim você consegue ver o payload legível no console do RabbitMQ
     * e remove acoplamento de Serializable/FQN entre serviços.
     */
    private void publishJsonEvent(String routingKey, CustomerEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);

            MessagePostProcessor mpp = msg -> {
                msg.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                msg.getMessageProperties().setContentEncoding("utf-8");
                return msg;
            };

            rabbitTemplate.convertAndSend(EXCHANGE_CUSTOMERS, routingKey, json, mpp);

        } catch (Exception e) {
            // você pode optar por só logar e não derrubar a request,
            // mas por enquanto é melhor falhar alto pra você ver o erro.
            throw new RuntimeException("Failed to publish customer event to RabbitMQ", e);
        }
    }
}
