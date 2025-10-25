package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.entity.Vendedor;
import com.megaeletronicos.backend.entity.Usuario;
import com.megaeletronicos.backend.repository.VendedorRepository;
import com.megaeletronicos.backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class VendedorService {
    
    @Autowired
    private VendedorRepository vendedorRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public List<Vendedor> listarTodos() {
        return vendedorRepository.findAll();
    }
    
    public Optional<Vendedor> buscarPorId(Long id) {
        return vendedorRepository.findById(id);
    }
    
    public Optional<Vendedor> buscarPorCpf(String cpf) {
        return vendedorRepository.findByCpf(cpf);
    }
    
    public Vendedor salvar(Vendedor vendedor) {
        // Salva o vendedor primeiro
        Vendedor vendedorSalvo = vendedorRepository.save(vendedor);
        
        try {
            // Cria automaticamente um usuário com o email e senha do vendedor
            Usuario usuario = new Usuario(vendedor.getEmail(), vendedor.getSenha(), "VENDEDOR");
            usuarioService.salvar(usuario);
        } catch (Exception e) {
            // Se falhar ao criar o usuário, pode logar o erro mas não falha a criação do vendedor
            System.err.println("Erro ao criar usuário para vendedor: " + e.getMessage());
        }
        
        return vendedorSalvo;
    }
    
    public void deletar(Long id) {
        vendedorRepository.deleteById(id);
    }
    
    public boolean existePorId(Long id) {
        return vendedorRepository.existsById(id);
    }
    
    public boolean existePorCpf(String cpf) {
        return vendedorRepository.findByCpf(cpf).isPresent();
    }
    
    public boolean existePorEmail(String email) {
        return vendedorRepository.findAll().stream()
                .anyMatch(v -> v.getEmail().equalsIgnoreCase(email));
    }
    
    public boolean existePorEmailExcluindoId(String email, Long id) {
        return vendedorRepository.findAll().stream()
                .anyMatch(v -> v.getEmail().equalsIgnoreCase(email) && !v.getId().equals(id));
    }
    
    public boolean existePorCpfExcluindoId(String cpf, Long id) {
        return vendedorRepository.countByCpfAndIdNot(cpf, id) > 0;
    }
    
    public List<Vendedor> buscarPorNome(String nome) {
        return vendedorRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Vendedor> buscarPorStatus(String status) {
        return vendedorRepository.findByStatus(status);
    }
    
    public Vendedor atualizarStatus(Long id, String status) {
        Optional<Vendedor> vendedorOpt = vendedorRepository.findById(id);
        if (vendedorOpt.isPresent()) {
            Vendedor vendedor = vendedorOpt.get();
            vendedor.setStatus(status);
            return vendedorRepository.save(vendedor);
        }
        return null;
    }
    
    // Método para busca com filtros e paginação
    public Page<Vendedor> filtrarVendedores(String nome, String cpf, String status, Pageable pageable) {
        Specification<Vendedor> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (nome != null && !nome.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nome")), 
                    "%" + nome.toLowerCase() + "%"
                ));
            }
            
            if (cpf != null && !cpf.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    root.get("cpf"), 
                    "%" + cpf + "%"
                ));
            }
            
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return vendedorRepository.findAll(spec, pageable);
    }
    
    // Método para obter estatísticas
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Total de vendedores
        long totalVendedores = vendedorRepository.count();
        estatisticas.put("totalVendedores", totalVendedores);
        
        // Vendedores ativos
        long vendedoresAtivos = vendedorRepository.countByStatus("ATIVO");
        estatisticas.put("vendedoresAtivos", vendedoresAtivos);
        
        // Vendedores inativos
        long vendedoresInativos = vendedorRepository.countByStatus("INATIVO");
        estatisticas.put("vendedoresInativos", vendedoresInativos);
        
        // Vendedores cadastrados no último mês
        LocalDateTime umMesAtras = LocalDateTime.now().minusMonths(1);
        long vendedoresUltimoMes = vendedorRepository.countByDataCadastroAfter(umMesAtras);
        estatisticas.put("vendedoresUltimoMes", vendedoresUltimoMes);
        
        return estatisticas;
    }
    
    // Validação de CPF (básica)
    public boolean validarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        return true;
    }
    
    // Formatação de CPF
    public String formatarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return cpf;
        }
        
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() == 11) {
            return cpf.substring(0, 3) + "." + 
                   cpf.substring(3, 6) + "." + 
                   cpf.substring(6, 9) + "-" + 
                   cpf.substring(9, 11);
        }
        
        return cpf;
    }
}