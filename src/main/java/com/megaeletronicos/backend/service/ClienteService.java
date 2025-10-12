
package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.dto.EstatisticasDTO;
import com.megaeletronicos.backend.entity.Cliente;
import com.megaeletronicos.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageImpl;

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
            LocalDateTime dataInicio, 
            LocalDateTime dataFim, 
            Pageable pageable) {
        
        // Se não há filtros, usar busca simples
        if ((nome == null || nome.trim().isEmpty()) && 
            (email == null || email.trim().isEmpty()) && 
            (cpf == null || cpf.trim().isEmpty()) && 
            (whatsapp == null || whatsapp.trim().isEmpty()) && 
            (status == null || status.trim().isEmpty()) && 
            dataInicio == null && dataFim == null) {
            
            return clienteRepository.buscarComFiltros(
                null, null, null, null, null, null, null, pageable
            );
        }
        
        // Para filtros complexos, usar busca por especificação
        return buscarComEspecificacao(nome, email, cpf, whatsapp, status, dataInicio, dataFim, pageable);
    }
    
    private Page<Cliente> buscarComEspecificacao(String nome, String email, String cpf, 
                                                String whatsapp, String status, 
                                                LocalDateTime dataInicio, LocalDateTime dataFim, 
                                                Pageable pageable) {
        // Por enquanto, retornar todos os clientes e filtrar em memória (não ideal para produção)
        Page<Cliente> todosClientes = clienteRepository.findAll(pageable);
        
        List<Cliente> clientesFiltrados = todosClientes.getContent().stream()
            .filter(cliente -> nome == null || nome.trim().isEmpty() || 
                    cliente.getNome().toLowerCase().contains(nome.toLowerCase()))
            .filter(cliente -> email == null || email.trim().isEmpty() || 
                    cliente.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(cliente -> cpf == null || cpf.trim().isEmpty() || 
                    cliente.getCpf().equals(cpf))
            .filter(cliente -> whatsapp == null || whatsapp.trim().isEmpty() || 
                    cliente.getWhatsapp().equals(whatsapp))
            .filter(cliente -> status == null || status.trim().isEmpty() || 
                    cliente.getStatus().equals(status))
            .filter(cliente -> dataInicio == null || 
                    cliente.getDataCadastro().isAfter(dataInicio) || cliente.getDataCadastro().isEqual(dataInicio))
            .filter(cliente -> dataFim == null || 
                    cliente.getDataCadastro().isBefore(dataFim) || cliente.getDataCadastro().isEqual(dataFim))
            .collect(Collectors.toList());
        
        return new PageImpl<>(clientesFiltrados, pageable, clientesFiltrados.size());
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
