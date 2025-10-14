package com.megaeletronicos.backend.specification;

import com.megaeletronicos.backend.entity.Empresa;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmpresaSpecifications {
    
    public static Specification<Empresa> comFiltros(
            String razaoSocial, String nomeFantasia, String cnpj, String email,
            String cidade, String estado, String status, 
            LocalDateTime dataInicio, LocalDateTime dataFim) {
        
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (razaoSocial != null && !razaoSocial.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("razaoSocial")), "%" + razaoSocial.toLowerCase() + "%"));
            }
            if (nomeFantasia != null && !nomeFantasia.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nomeFantasia")), "%" + nomeFantasia.toLowerCase() + "%"));
            }
            if (cnpj != null && !cnpj.isEmpty()) {
                predicates.add(cb.like(root.get("cnpj"), "%" + cnpj + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (cidade != null && !cidade.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("cidade")), "%" + cidade.toLowerCase() + "%"));
            }
            if (estado != null && !estado.isEmpty()) {
                predicates.add(cb.equal(root.get("estado"), estado));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataCadastro"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataCadastro"), dataFim));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}