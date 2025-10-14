package com.megaeletronicos.backend.controller;

import com.megaeletronicos.backend.dto.EmpresaRequestDTO;
import com.megaeletronicos.backend.dto.EmpresaResponseDTO;
import com.megaeletronicos.backend.entity.Empresa;
import com.megaeletronicos.backend.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    // Listar todas as empresas
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarTodas() {
        List<Empresa> empresas = empresaService.listarTodas();
        List<EmpresaResponseDTO> empresasDTO = empresas.stream()
                .map(EmpresaResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empresasDTO);
    }

    // Buscar empresa por ID
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaService.buscarPorId(id);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(new EmpresaResponseDTO(empresa.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar empresa por CNPJ
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorCnpj(@PathVariable String cnpj) {
        Optional<Empresa> empresa = empresaService.buscarPorCnpj(cnpj);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(new EmpresaResponseDTO(empresa.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar empresa por email
    @GetMapping("/email/{email}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorEmail(@PathVariable String email) {
        Optional<Empresa> empresa = empresaService.buscarPorEmail(email);
        if (empresa.isPresent()) {
            return ResponseEntity.ok(new EmpresaResponseDTO(empresa.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Criar nova empresa
    @PostMapping
    public ResponseEntity<?> criarEmpresa(@Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        try {
            // Verificar se já existe empresa com o mesmo CNPJ
            if (empresaService.buscarPorCnpj(empresaRequestDTO.getCnpj()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("erro", "Já existe uma empresa cadastrada com este CNPJ"));
            }

            // Verificar se já existe empresa com o mesmo email (se fornecido)
            if (empresaRequestDTO.getEmail() != null && !empresaRequestDTO.getEmail().isEmpty()) {
                if (empresaService.buscarPorEmail(empresaRequestDTO.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("erro", "Já existe uma empresa cadastrada com este email"));
                }
            }

            // Criar nova empresa
            Empresa novaEmpresa = new Empresa();
            novaEmpresa.setRazaoSocial(empresaRequestDTO.getRazaoSocial());
            novaEmpresa.setNomeFantasia(empresaRequestDTO.getNomeFantasia());
            novaEmpresa.setCnpj(empresaRequestDTO.getCnpj());
            novaEmpresa.setInscricaoEstadual(empresaRequestDTO.getInscricaoEstadual());
            novaEmpresa.setInscricaoMunicipal(empresaRequestDTO.getInscricaoMunicipal());
            novaEmpresa.setTelefone(empresaRequestDTO.getTelefone());
            novaEmpresa.setCelular(empresaRequestDTO.getCelular());
            novaEmpresa.setEmail(empresaRequestDTO.getEmail());
            novaEmpresa.setWebsite(empresaRequestDTO.getWebsite());
            novaEmpresa.setCep(empresaRequestDTO.getCep());
            novaEmpresa.setLogradouro(empresaRequestDTO.getLogradouro());
            novaEmpresa.setNumero(empresaRequestDTO.getNumero());
            novaEmpresa.setComplemento(empresaRequestDTO.getComplemento());
            novaEmpresa.setBairro(empresaRequestDTO.getBairro());
            novaEmpresa.setCidade(empresaRequestDTO.getCidade());
            novaEmpresa.setEstado(empresaRequestDTO.getEstado());
            novaEmpresa.setStatus("ATIVO");

            Empresa empresaSalva = empresaService.salvar(novaEmpresa);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new EmpresaResponseDTO(empresaSalva));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Atualizar empresa
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEmpresa(@PathVariable Long id, 
                                            @Valid @RequestBody EmpresaRequestDTO empresaRequestDTO) {
        try {
            Optional<Empresa> empresaExistente = empresaService.buscarPorId(id);
            if (!empresaExistente.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Empresa empresa = empresaExistente.get();

            // Verificar se o CNPJ está sendo alterado e se já existe
            if (!empresa.getCnpj().equals(empresaRequestDTO.getCnpj())) {
                if (empresaService.buscarPorCnpj(empresaRequestDTO.getCnpj()).isPresent()) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("erro", "Já existe uma empresa cadastrada com este CNPJ"));
                }
            }

            // Verificar se o email está sendo alterado e se já existe
            if (empresaRequestDTO.getEmail() != null && !empresaRequestDTO.getEmail().isEmpty()) {
                if (!empresaRequestDTO.getEmail().equals(empresa.getEmail())) {
                    if (empresaService.buscarPorEmail(empresaRequestDTO.getEmail()).isPresent()) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("erro", "Já existe uma empresa cadastrada com este email"));
                    }
                }
            }

            // Atualizar dados
            empresa.setRazaoSocial(empresaRequestDTO.getRazaoSocial());
            empresa.setNomeFantasia(empresaRequestDTO.getNomeFantasia());
            empresa.setCnpj(empresaRequestDTO.getCnpj());
            empresa.setInscricaoEstadual(empresaRequestDTO.getInscricaoEstadual());
            empresa.setInscricaoMunicipal(empresaRequestDTO.getInscricaoMunicipal());
            empresa.setTelefone(empresaRequestDTO.getTelefone());
            empresa.setCelular(empresaRequestDTO.getCelular());
            empresa.setEmail(empresaRequestDTO.getEmail());
            empresa.setWebsite(empresaRequestDTO.getWebsite());
            empresa.setCep(empresaRequestDTO.getCep());
            empresa.setLogradouro(empresaRequestDTO.getLogradouro());
            empresa.setNumero(empresaRequestDTO.getNumero());
            empresa.setComplemento(empresaRequestDTO.getComplemento());
            empresa.setBairro(empresaRequestDTO.getBairro());
            empresa.setCidade(empresaRequestDTO.getCidade());
            empresa.setEstado(empresaRequestDTO.getEstado());

            Empresa empresaAtualizada = empresaService.atualizar(id, empresa);
            return ResponseEntity.ok(new EmpresaResponseDTO(empresaAtualizada));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Deletar empresa
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Long id) {
        try {
            if (!empresaService.buscarPorId(id).isPresent()) {
                return ResponseEntity.notFound().build();
            }
            empresaService.deletar(id);
            return ResponseEntity.ok(Map.of("mensagem", "Empresa deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Atualizar status da empresa
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        try {
            String novoStatus = statusData.get("status");
            if (novoStatus == null || novoStatus.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("erro", "Status é obrigatório"));
            }

            Empresa empresaAtualizada = empresaService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(new EmpresaResponseDTO(empresaAtualizada));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Buscar empresas com filtros e paginação
    @GetMapping("/filtrar")
    public ResponseEntity<Page<EmpresaResponseDTO>> filtrarEmpresas(
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String nomeFantasia,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Empresa> empresas = empresaService.buscarComFiltros(
                razaoSocial, nomeFantasia, cnpj, email, cidade, estado, status, null, null, pageable);

        Page<EmpresaResponseDTO> empresasDTO = empresas.map(EmpresaResponseDTO::new);
        return ResponseEntity.ok(empresasDTO);
    }

    // Obter estatísticas das empresas
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        Map<String, Object> estatisticas = empresaService.obterEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }

    // Buscar empresas por cidade
    @GetMapping("/cidade/{cidade}")
    public ResponseEntity<List<EmpresaResponseDTO>> buscarPorCidade(@PathVariable String cidade) {
        List<Empresa> empresas = empresaService.buscarPorCidade(cidade);
        List<EmpresaResponseDTO> empresasDTO = empresas.stream()
                .map(EmpresaResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empresasDTO);
    }

    // Buscar empresas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EmpresaResponseDTO>> buscarPorEstado(@PathVariable String estado) {
        List<Empresa> empresas = empresaService.buscarPorEstado(estado);
        List<EmpresaResponseDTO> empresasDTO = empresas.stream()
                .map(EmpresaResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empresasDTO);
    }

    // Buscar empresas por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmpresaResponseDTO>> buscarPorStatus(@PathVariable String status) {
        List<Empresa> empresas = empresaService.buscarPorStatus(status);
        List<EmpresaResponseDTO> empresasDTO = empresas.stream()
                .map(EmpresaResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(empresasDTO);
    }
}