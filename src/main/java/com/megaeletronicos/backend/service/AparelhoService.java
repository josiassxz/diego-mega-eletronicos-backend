package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.dto.CreateAparelhoDTO;
import com.megaeletronicos.backend.dto.UpdateAparelhoDTO;
import com.megaeletronicos.backend.entity.Aparelho;
import com.megaeletronicos.backend.entity.Cliente;
import com.megaeletronicos.backend.entity.Empresa;
import com.megaeletronicos.backend.repository.AparelhoRepository;
import com.megaeletronicos.backend.repository.ClienteRepository;
import com.megaeletronicos.backend.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AparelhoService {
    
    @Autowired
    private AparelhoRepository aparelhoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    public List<Aparelho> listarTodos() {
        return aparelhoRepository.findAll();
    }
    
    public Optional<Aparelho> buscarPorId(Long id) {
        return aparelhoRepository.findById(id);
    }
    
    public Optional<Aparelho> buscarPorImei(String imei) {
        return aparelhoRepository.findByImei(imei);
    }
    
    public List<Aparelho> buscarPorCliente(Long clienteId) {
        return aparelhoRepository.findByClienteId(clienteId);
    }
    
    public List<Aparelho> buscarPorEmpresa(Long empresaId) {
        return aparelhoRepository.findByEmpresaId(empresaId);
    }
    
    public List<Aparelho> buscarPorStatus(String status) {
        return aparelhoRepository.findByStatus(status);
    }
    
    public List<Aparelho> buscarPorModelo(String modelo) {
        return aparelhoRepository.findByModeloContainingIgnoreCase(modelo);
    }
    
    public List<Aparelho> buscarPorMarca(String marca) {
        return aparelhoRepository.findByMarcaContainingIgnoreCase(marca);
    }
    
    public Aparelho criar(CreateAparelhoDTO createDTO) {
        // Verificar se já existe aparelho com mesmo IMEI
        if (aparelhoRepository.findByImei(createDTO.getImei()).isPresent()) {
            throw new RuntimeException("Já existe um aparelho com este IMEI");
        }
        
        // Buscar cliente
        Cliente cliente = clienteRepository.findById(createDTO.getClienteId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        // Buscar empresa
        Empresa empresa = empresaRepository.findById(createDTO.getEmpresaId())
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
        Aparelho aparelho = new Aparelho();
        aparelho.setImei(createDTO.getImei());
        aparelho.setModelo(createDTO.getModelo());
        aparelho.setMarca(createDTO.getMarca());
        aparelho.setCliente(cliente);
        aparelho.setEmpresa(empresa);
        aparelho.setValorParcelado(createDTO.getValorParcelado());
        aparelho.setValorTotal(createDTO.getValorTotal());
        aparelho.setParcelas(createDTO.getParcelas());
        aparelho.setValorParcela(createDTO.getValorParcela());
        aparelho.setDiasVencimento(createDTO.getDiasVencimento());
        
        // Calcular data de vencimento se não fornecida
        LocalDate dataVencimento = createDTO.getDataVencimento();
        if (dataVencimento == null) {
            dataVencimento = LocalDate.now().plusDays(createDTO.getDiasVencimento());
        }
        aparelho.setDataVencimento(dataVencimento);
        aparelho.setStatus("Ativo");
        
        return aparelhoRepository.save(aparelho);
    }
    
    public Aparelho atualizar(Long id, UpdateAparelhoDTO updateDTO) {
        Aparelho aparelho = aparelhoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aparelho não encontrado"));
        
        // Verificar se IMEI não pertence a outro aparelho
        if (updateDTO.getImei() != null && !updateDTO.getImei().equals(aparelho.getImei())) {
            Optional<Aparelho> aparelhoComMesmoImei = aparelhoRepository.findByImei(updateDTO.getImei());
            if (aparelhoComMesmoImei.isPresent() && !aparelhoComMesmoImei.get().getId().equals(id)) {
                throw new RuntimeException("Já existe outro aparelho com este IMEI");
            }
            aparelho.setImei(updateDTO.getImei());
        }
        
        if (updateDTO.getModelo() != null) {
            aparelho.setModelo(updateDTO.getModelo());
        }
        
        if (updateDTO.getMarca() != null) {
            aparelho.setMarca(updateDTO.getMarca());
        }
        
        if (updateDTO.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(updateDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            aparelho.setCliente(cliente);
        }
        
        if (updateDTO.getEmpresaId() != null) {
            Empresa empresa = empresaRepository.findById(updateDTO.getEmpresaId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
            aparelho.setEmpresa(empresa);
        }
        
        if (updateDTO.getValorParcelado() != null) {
            aparelho.setValorParcelado(updateDTO.getValorParcelado());
        }
        
        if (updateDTO.getValorTotal() != null) {
            aparelho.setValorTotal(updateDTO.getValorTotal());
        }
        
        if (updateDTO.getParcelas() != null) {
            aparelho.setParcelas(updateDTO.getParcelas());
        }
        
        if (updateDTO.getValorParcela() != null) {
            aparelho.setValorParcela(updateDTO.getValorParcela());
        }
        
        if (updateDTO.getDiasVencimento() != null) {
            aparelho.setDiasVencimento(updateDTO.getDiasVencimento());
        }
        
        if (updateDTO.getDataVencimento() != null) {
            aparelho.setDataVencimento(updateDTO.getDataVencimento());
        }
        
        if (updateDTO.getStatus() != null) {
            aparelho.setStatus(updateDTO.getStatus());
        }
        
        return aparelhoRepository.save(aparelho);
    }
    
    public void deletar(Long id) {
        if (!aparelhoRepository.existsById(id)) {
            throw new RuntimeException("Aparelho não encontrado");
        }
        aparelhoRepository.deleteById(id);
    }
    
    public Aparelho atualizarStatus(Long id, String novoStatus) {
        Aparelho aparelho = aparelhoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aparelho não encontrado"));
        
        aparelho.setStatus(novoStatus);
        return aparelhoRepository.save(aparelho);
    }
    
    public Page<Aparelho> buscarComFiltros(
            String imei,
            String modelo,
            String marca,
            String clienteNome,
            String empresaNome,
            String status,
            LocalDateTime dataInicio,
            LocalDateTime dataFim,
            Pageable pageable) {
        
        return aparelhoRepository.buscarComFiltros(
            imei, modelo, marca, clienteNome, empresaNome, 
            status, dataInicio, dataFim, pageable
        );
    }
    
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Contadores por status
        Long totalAparelhos = aparelhoRepository.count();
        Long aparelhosAtivos = aparelhoRepository.countByStatus("Ativo");
        Long aparelhosInativos = aparelhoRepository.countByStatus("Inativo");
        Long aparelhosPendentes = aparelhoRepository.countByStatus("Pendente");
        
        estatisticas.put("totalAparelhos", totalAparelhos);
        estatisticas.put("aparelhosAtivos", aparelhosAtivos);
        estatisticas.put("aparelhosInativos", aparelhosInativos);
        estatisticas.put("aparelhosPendentes", aparelhosPendentes);
        
        // Valores financeiros
        Double valorTotalAtivos = aparelhoRepository.sumValorTotalByStatus("Ativo");
        Double valorParceladoAtivos = aparelhoRepository.sumValorParceladoByStatus("Ativo");
        
        estatisticas.put("valorTotalAtivos", valorTotalAtivos != null ? valorTotalAtivos : 0.0);
        estatisticas.put("valorParceladoAtivos", valorParceladoAtivos != null ? valorParceladoAtivos : 0.0);
        
        // Aparelhos cadastrados no último mês
        LocalDateTime umMesAtras = LocalDateTime.now().minusMonths(1);
        Long aparelhosUltimoMes = aparelhoRepository.countByDataCadastroAfter(umMesAtras);
        estatisticas.put("aparelhosUltimoMes", aparelhosUltimoMes);
        
        return estatisticas;
    }
    
    public List<Aparelho> buscarAparelhosProximosVencimento(int dias) {
        LocalDate dataLimite = LocalDate.now().plusDays(dias);
        return aparelhoRepository.findAparelhosProximosVencimento(dataLimite);
    }
}