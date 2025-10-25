package com.megaeletronicos.backend.controller;

import com.megaeletronicos.backend.dto.VendedorRequestDTO;
import com.megaeletronicos.backend.dto.VendedorResponseDTO;
import com.megaeletronicos.backend.entity.Vendedor;
import com.megaeletronicos.backend.service.VendedorService;
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
@RequestMapping("/api/vendedores")
@CrossOrigin(origins = "*")
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    // Listar todos os vendedores
    @GetMapping
    public ResponseEntity<List<VendedorResponseDTO>> listarTodos() {
        List<Vendedor> vendedores = vendedorService.listarTodos();
        List<VendedorResponseDTO> vendedoresDTO = vendedores.stream()
                .map(VendedorResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vendedoresDTO);
    }

    // Buscar vendedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Vendedor> vendedor = vendedorService.buscarPorId(id);
            if (vendedor.isPresent()) {
                return ResponseEntity.ok(new VendedorResponseDTO(vendedor.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Vendedor não encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Buscar vendedor por CPF
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
        try {
            Optional<Vendedor> vendedor = vendedorService.buscarPorCpf(cpf);
            if (vendedor.isPresent()) {
                return ResponseEntity.ok(new VendedorResponseDTO(vendedor.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Vendedor não encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Criar novo vendedor
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody VendedorRequestDTO vendedorRequestDTO) {
        try {
            // Validar CPF
            if (!vendedorService.validarCpf(vendedorRequestDTO.getCpf())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("erro", "CPF inválido"));
            }

            // Verificar se CPF já existe
            if (vendedorService.existePorCpf(vendedorRequestDTO.getCpf())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "CPF já cadastrado"));
            }
            
            // Verificar se email já existe
            if (vendedorService.existePorEmail(vendedorRequestDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Email já cadastrado"));
            }

            Vendedor novoVendedor = new Vendedor();
            novoVendedor.setNome(vendedorRequestDTO.getNome());
            novoVendedor.setCpf(vendedorService.formatarCpf(vendedorRequestDTO.getCpf()));
            novoVendedor.setEmail(vendedorRequestDTO.getEmail());
            novoVendedor.setSenha(vendedorRequestDTO.getSenha());
            novoVendedor.setStatus("ATIVO");

            Vendedor vendedorSalvo = vendedorService.salvar(novoVendedor);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new VendedorResponseDTO(vendedorSalvo));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Atualizar vendedor
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody VendedorRequestDTO vendedorRequestDTO) {
        try {
            Optional<Vendedor> vendedorExistente = vendedorService.buscarPorId(id);
            if (!vendedorExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Vendedor não encontrado"));
            }

            // Validar CPF
            if (!vendedorService.validarCpf(vendedorRequestDTO.getCpf())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("erro", "CPF inválido"));
            }

            // Verificar se CPF já existe (excluindo o próprio vendedor)
            if (vendedorService.existePorCpfExcluindoId(vendedorRequestDTO.getCpf(), id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "CPF já cadastrado para outro vendedor"));
            }
            
            // Verificar se email já existe (excluindo o próprio vendedor)
            if (vendedorService.existePorEmailExcluindoId(vendedorRequestDTO.getEmail(), id)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("erro", "Email já cadastrado para outro vendedor"));
            }

            Vendedor vendedor = vendedorExistente.get();
            vendedor.setNome(vendedorRequestDTO.getNome());
            vendedor.setCpf(vendedorService.formatarCpf(vendedorRequestDTO.getCpf()));
            vendedor.setEmail(vendedorRequestDTO.getEmail());
            vendedor.setSenha(vendedorRequestDTO.getSenha());

            Vendedor vendedorAtualizado = vendedorService.salvar(vendedor);
            return ResponseEntity.ok(new VendedorResponseDTO(vendedorAtualizado));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Deletar vendedor
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (!vendedorService.existePorId(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Vendedor não encontrado"));
            }

            vendedorService.deletar(id);
            return ResponseEntity.ok(Map.of("mensagem", "Vendedor deletado com sucesso"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Atualizar status do vendedor
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> statusData) {
        try {
            String novoStatus = statusData.get("status");
            if (novoStatus == null || (!novoStatus.equals("ATIVO") && !novoStatus.equals("INATIVO"))) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("erro", "Status deve ser ATIVO ou INATIVO"));
            }

            Vendedor vendedorAtualizado = vendedorService.atualizarStatus(id, novoStatus);
            if (vendedorAtualizado != null) {
                return ResponseEntity.ok(new VendedorResponseDTO(vendedorAtualizado));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Vendedor não encontrado"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Filtrar vendedores com paginação
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Vendedor> vendedoresPage = vendedorService.filtrarVendedores(nome, cpf, status, pageable);
            
            Page<VendedorResponseDTO> vendedoresDTOPage = vendedoresPage.map(VendedorResponseDTO::new);
            
            return ResponseEntity.ok(vendedoresDTOPage);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Buscar vendedores por nome
    @GetMapping("/buscar/nome/{nome}")
    public ResponseEntity<List<VendedorResponseDTO>> buscarPorNome(@PathVariable String nome) {
        List<Vendedor> vendedores = vendedorService.buscarPorNome(nome);
        List<VendedorResponseDTO> vendedoresDTO = vendedores.stream()
                .map(VendedorResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vendedoresDTO);
    }

    // Buscar vendedores por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<VendedorResponseDTO>> buscarPorStatus(@PathVariable String status) {
        List<Vendedor> vendedores = vendedorService.buscarPorStatus(status);
        List<VendedorResponseDTO> vendedoresDTO = vendedores.stream()
                .map(VendedorResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vendedoresDTO);
    }

    // Obter estatísticas dos vendedores
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        Map<String, Object> estatisticas = vendedorService.obterEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
}