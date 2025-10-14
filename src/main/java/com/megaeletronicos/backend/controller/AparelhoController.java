package com.megaeletronicos.backend.controller;

import com.megaeletronicos.backend.dto.AparelhoResponseDTO;
import com.megaeletronicos.backend.dto.CreateAparelhoDTO;
import com.megaeletronicos.backend.dto.UpdateAparelhoDTO;
import com.megaeletronicos.backend.entity.Aparelho;
import com.megaeletronicos.backend.service.AparelhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/aparelhos")
@CrossOrigin(origins = "*")
public class AparelhoController {
    
    @Autowired
    private AparelhoService aparelhoService;
    
    @GetMapping
    public ResponseEntity<List<AparelhoResponseDTO>> listarTodos() {
        try {
            List<Aparelho> aparelhos = aparelhoService.listarTodos();
            List<AparelhoResponseDTO> response = aparelhos.stream()
                .map(AparelhoResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AparelhoResponseDTO> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Aparelho> aparelho = aparelhoService.buscarPorId(id);
            if (aparelho.isPresent()) {
                return ResponseEntity.ok(new AparelhoResponseDTO(aparelho.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/imei/{imei}")
    public ResponseEntity<AparelhoResponseDTO> buscarPorImei(@PathVariable String imei) {
        try {
            Optional<Aparelho> aparelho = aparelhoService.buscarPorImei(imei);
            if (aparelho.isPresent()) {
                return ResponseEntity.ok(new AparelhoResponseDTO(aparelho.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<AparelhoResponseDTO>> buscarPorCliente(@PathVariable Long clienteId) {
        try {
            List<Aparelho> aparelhos = aparelhoService.buscarPorCliente(clienteId);
            List<AparelhoResponseDTO> response = aparelhos.stream()
                .map(AparelhoResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<AparelhoResponseDTO>> buscarPorEmpresa(@PathVariable Long empresaId) {
        try {
            List<Aparelho> aparelhos = aparelhoService.buscarPorEmpresa(empresaId);
            List<AparelhoResponseDTO> response = aparelhos.stream()
                .map(AparelhoResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AparelhoResponseDTO>> buscarPorStatus(@PathVariable String status) {
        try {
            List<Aparelho> aparelhos = aparelhoService.buscarPorStatus(status);
            List<AparelhoResponseDTO> response = aparelhos.stream()
                .map(AparelhoResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody CreateAparelhoDTO createDTO) {
        try {
            Aparelho aparelho = aparelhoService.criar(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AparelhoResponseDTO(aparelho));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody UpdateAparelhoDTO updateDTO) {
        try {
            Aparelho aparelho = aparelhoService.atualizar(id, updateDTO);
            return ResponseEntity.ok(new AparelhoResponseDTO(aparelho));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            aparelhoService.deletar(id);
            return ResponseEntity.ok(Map.of("mensagem", "Aparelho deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String novoStatus = request.get("status");
            if (novoStatus == null || novoStatus.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Status é obrigatório"));
            }
            
            Aparelho aparelho = aparelhoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(new AparelhoResponseDTO(aparelho));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro interno do servidor"));
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<Page<AparelhoResponseDTO>> buscarComFiltros(
            @RequestParam(required = false) String imei,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String clienteNome,
            @RequestParam(required = false) String empresaNome,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataCadastro") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Aparelho> aparelhos = aparelhoService.buscarComFiltros(
                imei, modelo, marca, clienteNome, empresaNome, 
                status, dataInicio, dataFim, pageable
            );
            
            Page<AparelhoResponseDTO> response = aparelhos.map(AparelhoResponseDTO::new);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        try {
            Map<String, Object> estatisticas = aparelhoService.obterEstatisticas();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/vencimento-proximo")
    public ResponseEntity<List<AparelhoResponseDTO>> buscarAparelhosProximosVencimento(
            @RequestParam(defaultValue = "30") int dias) {
        try {
            List<Aparelho> aparelhos = aparelhoService.buscarAparelhosProximosVencimento(dias);
            List<AparelhoResponseDTO> response = aparelhos.stream()
                .map(AparelhoResponseDTO::new)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}