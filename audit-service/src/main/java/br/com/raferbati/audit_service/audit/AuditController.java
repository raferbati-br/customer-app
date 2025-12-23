package br.com.raferbati.audit_service.audit;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
// CORREÇÃO AQUI: Usamos chaves {} para permitir múltiplas origens
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"}) 
public class AuditController {

    private final AuditService service;

    public AuditController(AuditService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditLog> list() {
        return service.findAll();
    }
}