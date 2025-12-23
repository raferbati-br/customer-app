package br.com.raferbati.audit_service.audit;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "http://localhost:5173")
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLog> list() {
        // O Controller não precisa saber que a ordenação é DESC, o Service decide isso
        return service.findAll();
    }
}