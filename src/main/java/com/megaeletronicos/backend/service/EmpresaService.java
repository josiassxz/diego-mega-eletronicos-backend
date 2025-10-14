package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.entity.Empresa;
import com.megaeletronicos.backend.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmpresaService {
    
    @Autowired
    private EmpresaRepository empresaRepository;
    
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }
    
    public Optional<Empresa> buscarPorId(Long id) {
        return empresaRepository.findById(id);
    }
    
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        return empresaRepository.findByCnpj(cnpj);
    }
    
    public Optional<Empresa> buscarPorEmail(String email) {
        return empresaRepository.findByEmail(email);
    }
    
    public List<Empresa> buscarPorRazaoSocial(String razaoSocial) {
        return empresaRepository.findByRazaoSocialContainingIgnoreCase(razaoSocial);
    }
    
    public List<Empresa> buscarPorNomeFantasia(String nomeFantasia) {
        return empresaRepository.findByNomeFantasiaContainingIgnoreCase(nomeFantasia);
    }
    
    public List<Empresa> buscarPorCidade(String cidade) {
        return empresaRepository.findByCidade(cidade);
    }
    
    public List<Empresa> buscarPorEstado(String estado) {
        return empresaRepository.findByEstado(estado);
    }
    
    public List<Empresa> buscarPorStatus(String status) {
        return empresaRepository.findByStatus(status);
    }
    
    public List<Empresa> buscarPorCep(String cep) {
        return empresaRepository.findByCep(cep);
    }
    
    public List<Empresa> buscarPorBairro(String bairro) {
        return empresaRepository.findByBairroContainingIgnoreCase(bairro);
    }
    
    public Empresa salvar(Empresa empresa) {
        // Verificar se já existe empresa com mesmo CNPJ
        if (empresa.getId() == null) {
            if (empresaRepository.findByCnpj(empresa.getCnpj()).isPresent()) {
                throw new RuntimeException("Já existe uma empresa com este CNPJ");
            }
            
            // Verificar se já existe empresa com mesmo email (se fornecido)
            if (empresa.getEmail() != null && !empresa.getEmail().trim().isEmpty()) {
                if (empresaRepository.findByEmail(empresa.getEmail()).isPresent()) {
                    throw new RuntimeException("Já existe uma empresa com este email");
                }
            }
        } else {
            // Para atualizações, verificar se CNPJ não está sendo usado por outra empresa
            Optional<Empresa> empresaExistente = empresaRepository.findByCnpj(empresa.getCnpj());
            if (empresaExistente.isPresent() && !empresaExistente.get().getId().equals(empresa.getId())) {
                throw new RuntimeException("Já existe uma empresa com este CNPJ");
            }
            
            // Verificar email para atualizações
            if (empresa.getEmail() != null && !empresa.getEmail().trim().isEmpty()) {
                Optional<Empresa> empresaComEmail = empresaRepository.findByEmail(empresa.getEmail());
                if (empresaComEmail.isPresent() && !empresaComEmail.get().getId().equals(empresa.getId())) {
                    throw new RuntimeException("Já existe uma empresa com este email");
                }
            }
        }
        
        return empresaRepository.save(empresa);
    }
    
    public void deletar(Long id) {
        if (!empresaRepository.existsById(id)) {
            throw new RuntimeException("Empresa não encontrada");
        }
        empresaRepository.deleteById(id);
    }
    
    public Empresa atualizar(Long id, Empresa empresaAtualizada) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    empresaAtualizada.setId(id);
                    return salvar(empresaAtualizada);
                })
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }
    
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Total de empresas
        long totalEmpresas = empresaRepository.count();
        estatisticas.put("totalEmpresas", totalEmpresas);
        
        // Empresas por status
        long empresasAtivas = empresaRepository.countByStatus("Ativa");
        long empresasInativas = empresaRepository.countByStatus("Inativa");
        long empresasSuspensas = empresaRepository.countByStatus("Suspensa");
        
        estatisticas.put("empresasAtivas", empresasAtivas);
        estatisticas.put("empresasInativas", empresasInativas);
        estatisticas.put("empresasSuspensas", empresasSuspensas);
        
        // Empresas cadastradas no último mês
        LocalDateTime umMesAtras = LocalDateTime.now().minusMonths(1);
        long empresasUltimoMes = empresaRepository.countByDataCadastroAfter(umMesAtras);
        estatisticas.put("empresasUltimoMes", empresasUltimoMes);
        
        // Empresas cadastradas na última semana
        LocalDateTime umaSemanaAtras = LocalDateTime.now().minusWeeks(1);
        long empresasUltimaSemana = empresaRepository.countByDataCadastroAfter(umaSemanaAtras);
        estatisticas.put("empresasUltimaSemana", empresasUltimaSemana);
        
        return estatisticas;
    }
    
    public Page<Empresa> buscarComFiltros(
            String razaoSocial, 
            String nomeFantasia,
            String cnpj, 
            String email, 
            String cidade,
            String estado,
            String status,
            LocalDateTime dataInicio, 
            LocalDateTime dataFim, 
            Pageable pageable) {
        
        return empresaRepository.buscarComFiltros(
            razaoSocial, nomeFantasia, cnpj, email, cidade, estado, status, 
            dataInicio, dataFim, pageable);
    }
    
    public Empresa atualizarStatus(Long id, String novoStatus) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    empresa.setStatus(novoStatus);
                    return empresaRepository.save(empresa);
                })
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }
    
    // Método para validar CNPJ (básico)
    public boolean validarCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        
        // Verifica se tem 14 dígitos
        if (cnpjLimpo.length() != 14) {
            return false;
        }
        
        // Verifica se não são todos os dígitos iguais
        if (cnpjLimpo.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        return true;
    }
    
    // Método para formatar CNPJ
    public String formatarCnpj(String cnpj) {
        if (cnpj == null) return null;
        
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpjLimpo.length() == 14) {
            return cnpjLimpo.substring(0, 2) + "." + 
                   cnpjLimpo.substring(2, 5) + "." + 
                   cnpjLimpo.substring(5, 8) + "/" + 
                   cnpjLimpo.substring(8, 12) + "-" + 
                   cnpjLimpo.substring(12, 14);
        }
        
        return cnpj;
    }
}