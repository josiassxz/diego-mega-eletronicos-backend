
package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.dto.EstatisticasDTO;
import com.megaeletronicos.backend.entity.Cliente;
import com.megaeletronicos.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }
    
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }
    
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public List<Cliente> buscarPorCidade(String cidade) {
        return clienteRepository.findByCidade(cidade);
    }
    
    public List<Cliente> buscarPorEstado(String estado) {
        return clienteRepository.findByEstado(estado);
    }
    
    public Cliente salvar(Cliente cliente) {
        // Verificar se já existe cliente com mesmo email ou CPF
        if (cliente.getId() == null) {
            if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
                throw new RuntimeException("Já existe um cliente com este email");
            }
            if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
                throw new RuntimeException("Já existe um cliente com este CPF");
            }
        } else {
            // Para atualizações, verificar se email/CPF não pertencem a outro cliente
            Optional<Cliente> clienteComMesmoEmail = clienteRepository.findByEmail(cliente.getEmail());
            if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Já existe outro cliente com este email");
            }
            
            Optional<Cliente> clienteComMesmoCpf = clienteRepository.findByCpf(cliente.getCpf());
            if (clienteComMesmoCpf.isPresent() && !clienteComMesmoCpf.get().getId().equals(cliente.getId())) {
                throw new RuntimeException("Já existe outro cliente com este CPF");
            }
        }
        
        return clienteRepository.save(cliente);
    }
    
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }
    
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        clienteAtualizado.setId(id);
        return salvar(clienteAtualizado);
    }
    
    // Novos métodos para estatísticas e filtros
    public EstatisticasDTO obterEstatisticas() {
        Long totalClientes = clienteRepository.count();
        
        // Clientes cadastrados hoje (desde 00:00 até 23:59:59)
        LocalDateTime inicioHoje = LocalDateTime.now().with(LocalTime.MIN);
        Long clientesHoje = clienteRepository.countByDataCadastroAfter(inicioHoje);
        
        // Contadores por status
        Long clientesPendentes = clienteRepository.countByStatus("Pendente");
        Long clientesAprovados = clienteRepository.countByStatus("Aprovado");
        Long clientesRecusados = clienteRepository.countByStatus("Recusado");
        Long clientesEmAnalise = clienteRepository.countByStatus("Em Análise");
        Long clientesVendidos = clienteRepository.countByStatus("Vendido");
        
        // Distribuição por status
        Map<String, Long> distribuicao = new HashMap<>();
        distribuicao.put("Pendente", clientesPendentes);
        distribuicao.put("Aprovado", clientesAprovados);
        distribuicao.put("Recusado", clientesRecusados);
        distribuicao.put("Em Análise", clientesEmAnalise);
        distribuicao.put("Vendido", clientesVendidos);
        
        EstatisticasDTO estatisticas = new EstatisticasDTO(
            totalClientes, 
            clientesHoje, 
            clientesPendentes, 
            clientesAprovados, 
            clientesRecusados, 
            clientesEmAnalise
        );
        estatisticas.setDistribuicaoPorStatus(distribuicao);
        
        return estatisticas;
    }
    
    public Page<Cliente> buscarComFiltros(
            String nome, 
            String email, 
            String cpf, 
            String whatsapp, 
            String status,
            String cpfVendedor,
            LocalDateTime dataInicio, 
            LocalDateTime dataFim, 
            Pageable pageable) {
        
        // Se não há filtros, usar busca simples
        if ((nome == null || nome.trim().isEmpty()) && 
            (email == null || email.trim().isEmpty()) && 
            (cpf == null || cpf.trim().isEmpty()) && 
            (whatsapp == null || whatsapp.trim().isEmpty()) && 
            (status == null || status.trim().isEmpty()) && 
            (cpfVendedor == null || cpfVendedor.trim().isEmpty()) &&
            dataInicio == null && dataFim == null) {
            
            return clienteRepository.findAll(pageable);
        }
        
        // Para filtros complexos, usar busca por especificação
        return buscarComEspecificacao(nome, email, cpf, whatsapp, status, cpfVendedor, dataInicio, dataFim, pageable);
    }
    
    private Page<Cliente> buscarComEspecificacao(String nome, String email, String cpf, String whatsapp, 
                                               String status, String cpfVendedor, LocalDateTime dataInicio, LocalDateTime dataFim, 
                                               Pageable pageable) {
        Specification<Cliente> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (nome != null && !nome.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nome")), 
                    "%" + nome.toLowerCase() + "%"
                ));
            }
            
            if (email != null && !email.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")), 
                    "%" + email.toLowerCase() + "%"
                ));
            }
            
            if (cpf != null && !cpf.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("cpf"), cpf));
            }
            
            if (whatsapp != null && !whatsapp.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("whatsapp"), whatsapp));
            }
            
            if (status != null && !status.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            if (cpfVendedor != null && !cpfVendedor.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("cpfVendedor"), cpfVendedor));
            }
            
            if (dataInicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataCadastro"), dataInicio));
            }
            
            if (dataFim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataCadastro"), dataFim));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return clienteRepository.findAll(spec, pageable);
    }
    
    public Cliente atualizarStatus(Long id, String novoStatus) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            throw new RuntimeException("Cliente não encontrado");
        }
        
        Cliente cliente = clienteOpt.get();
        cliente.setStatus(novoStatus);
        return clienteRepository.save(cliente);
    }
}
