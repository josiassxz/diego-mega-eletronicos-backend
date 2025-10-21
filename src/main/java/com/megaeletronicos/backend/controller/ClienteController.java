
package com.megaeletronicos.backend.controller;

import com.megaeletronicos.backend.dto.AtualizarStatusDTO;
import com.megaeletronicos.backend.dto.ClienteResponseDTO;
import com.megaeletronicos.backend.dto.EstatisticasDTO;
import com.megaeletronicos.backend.entity.Cliente;
import com.megaeletronicos.backend.service.ClienteService;
import com.megaeletronicos.backend.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.buscarPorId(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    @Operation(summary = "Buscar clientes por filtros")
    public ResponseEntity<List<Cliente>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado) {
        
        List<Cliente> clientes;
        
        if (email != null) {
            Optional<Cliente> cliente = clienteService.buscarPorEmail(email);
            clientes = cliente.map(List::of).orElse(List.of());
        } else if (cpf != null) {
            Optional<Cliente> cliente = clienteService.buscarPorCpf(cpf);
            clientes = cliente.map(List::of).orElse(List.of());
        } else if (nome != null) {
            clientes = clienteService.buscarPorNome(nome);
        } else if (cidade != null) {
            clientes = clienteService.buscarPorCidade(cidade);
        } else if (estado != null) {
            clientes = clienteService.buscarPorEstado(estado);
        } else {
            clientes = clienteService.listarTodos();
        }
        
        return ResponseEntity.ok(clientes);
    }
    
    @PostMapping
    @Operation(summary = "Criar novo cliente")
    public ResponseEntity<?> criar(@Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = clienteService.salvar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Criar novo cliente com fotos")
    public ResponseEntity<?> criarComFotos(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("cpf") String cpf,
            @RequestParam("rg") String rg,
            @RequestParam("telefone") String telefone,
            @RequestParam("whatsapp") String whatsapp,
            @RequestParam("cep") String cep,
            @RequestParam("rua") String rua,
            @RequestParam("numero") String numero,
            @RequestParam(value = "complemento", required = false) String complemento,
            @RequestParam("bairro") String bairro,
            @RequestParam("cidade") String cidade,
            @RequestParam("estado") String estado,
            @RequestParam("nomeMae") String nomeMae,
            @RequestParam("dataNascimento") String dataNascimento,
            @RequestParam("sexo") String sexo,
            @RequestParam("estadoCivil") String estadoCivil,
            @RequestParam("naturezaOcupacao") String naturezaOcupacao,
            @RequestParam("profissao") String profissao,
            @RequestParam(value = "nomeEmpresa", required = false) String nomeEmpresa,
            @RequestParam("rendaMensal") Double rendaMensal,
            @RequestParam(value = "comprovacaoRenda", required = false) String comprovacaoRenda,
            @RequestParam(value = "possuiCarteiraAssinadaOuAposentado", required = false) String possuiCarteiraAssinadaOuAposentado,
            // Primeira Referência
            @RequestParam(value = "referencia1Nome", required = false) String referencia1Nome,
            @RequestParam(value = "referencia1Relacao", required = false) String referencia1Relacao,
            @RequestParam(value = "referencia1Whatsapp", required = false) String referencia1Whatsapp,
            @RequestParam(value = "referencia1Conhece", required = false) Boolean referencia1Conhece,
            // Segunda Referência
            @RequestParam(value = "referencia2Nome", required = false) String referencia2Nome,
            @RequestParam(value = "referencia2Relacao", required = false) String referencia2Relacao,
            @RequestParam(value = "referencia2Whatsapp", required = false) String referencia2Whatsapp,
            @RequestParam(value = "referencia2Conhece", required = false) Boolean referencia2Conhece,
            // Terceira Referência
            @RequestParam(value = "referencia3Nome", required = false) String referencia3Nome,
            @RequestParam(value = "referencia3Relacao", required = false) String referencia3Relacao,
            @RequestParam(value = "referencia3Whatsapp", required = false) String referencia3Whatsapp,
            @RequestParam(value = "referencia3Conhece", required = false) Boolean referencia3Conhece,
            @RequestParam(value = "observacao", required = false) String observacao,
            @RequestParam(value = "fotoDocumento", required = false) MultipartFile fotoDocumento,
            @RequestParam(value = "fotoSelfie", required = false) MultipartFile fotoSelfie) {
        
        try {
            // Criar objeto Cliente
            Cliente cliente = new Cliente();
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setCpf(cpf);
            cliente.setRg(rg);
            cliente.setTelefone(telefone);
            cliente.setWhatsapp(whatsapp);
            cliente.setCep(cep);
            cliente.setRua(rua);
            cliente.setNumero(numero);
            cliente.setComplemento(complemento);
            cliente.setBairro(bairro);
            cliente.setCidade(cidade);
            cliente.setEstado(estado);
            cliente.setNomeMae(nomeMae);
            cliente.setDataNascimento(java.time.LocalDate.parse(dataNascimento));
            cliente.setSexo(sexo);
            cliente.setEstadoCivil(estadoCivil);
            cliente.setNaturezaOcupacao(naturezaOcupacao);
            cliente.setProfissao(profissao);
            cliente.setNomeEmpresa(nomeEmpresa);
            cliente.setRendaMensal(rendaMensal);
            cliente.setComprovacaoRenda(comprovacaoRenda);
            cliente.setPossuiCarteiraAssinadaOuAposentado(possuiCarteiraAssinadaOuAposentado);
            
            // Setar referências expandidas
            cliente.setReferencia1Nome(referencia1Nome);
            cliente.setReferencia1Relacao(referencia1Relacao);
            cliente.setReferencia1Whatsapp(referencia1Whatsapp);
            cliente.setReferencia1Conhece(referencia1Conhece);
            
            cliente.setReferencia2Nome(referencia2Nome);
            cliente.setReferencia2Relacao(referencia2Relacao);
            cliente.setReferencia2Whatsapp(referencia2Whatsapp);
            cliente.setReferencia2Conhece(referencia2Conhece);
            
            cliente.setReferencia3Nome(referencia3Nome);
            cliente.setReferencia3Relacao(referencia3Relacao);
            cliente.setReferencia3Whatsapp(referencia3Whatsapp);
            cliente.setReferencia3Conhece(referencia3Conhece);
            
            cliente.setObservacao(observacao);

            // Salvar cliente primeiro
            Cliente clienteSalvo = clienteService.salvar(cliente);

            // Salvar fotos se fornecidas
            if (fotoDocumento != null && !fotoDocumento.isEmpty()) {
                String nomeArquivoDocumento = fileStorageService.salvarArquivo(fotoDocumento);
                clienteSalvo.setFotoDocumento(nomeArquivoDocumento);
            }

            if (fotoSelfie != null && !fotoSelfie.isEmpty()) {
                String nomeArquivoSelfie = fileStorageService.salvarArquivo(fotoSelfie);
                clienteSalvo.setFotoSelfie(nomeArquivoSelfie);
            }

            // Atualizar cliente com os nomes dos arquivos
            if ((fotoDocumento != null && !fotoDocumento.isEmpty()) || 
                (fotoSelfie != null && !fotoSelfie.isEmpty())) {
                clienteSalvo = clienteService.salvar(clienteSalvo);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
            
        } catch (IOException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao processar dados: " + e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Atualizar cliente com fotos")
    public ResponseEntity<?> atualizarComFotos(
            @PathVariable Long id,
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("cpf") String cpf,
            @RequestParam("rg") String rg,
            @RequestParam("telefone") String telefone,
            @RequestParam("whatsapp") String whatsapp,
            @RequestParam("cep") String cep,
            @RequestParam("rua") String rua,
            @RequestParam("numero") String numero,
            @RequestParam(value = "complemento", required = false) String complemento,
            @RequestParam("bairro") String bairro,
            @RequestParam("cidade") String cidade,
            @RequestParam("estado") String estado,
            @RequestParam("nomeMae") String nomeMae,
            @RequestParam("dataNascimento") String dataNascimento,
            @RequestParam("sexo") String sexo,
            @RequestParam("estadoCivil") String estadoCivil,
            @RequestParam("naturezaOcupacao") String naturezaOcupacao,
            @RequestParam("profissao") String profissao,
            @RequestParam(value = "nomeEmpresa", required = false) String nomeEmpresa,
            @RequestParam("rendaMensal") Double rendaMensal,
            @RequestParam(value = "comprovacaoRenda", required = false) String comprovacaoRenda,
            @RequestParam(value = "possuiCarteiraAssinadaOuAposentado", required = false) String possuiCarteiraAssinadaOuAposentado,
            // Primeira Referência
            @RequestParam(value = "referencia1Nome", required = false) String referencia1Nome,
            @RequestParam(value = "referencia1Relacao", required = false) String referencia1Relacao,
            @RequestParam(value = "referencia1Whatsapp", required = false) String referencia1Whatsapp,
            @RequestParam(value = "referencia1Conhece", required = false) Boolean referencia1Conhece,
            // Segunda Referência
            @RequestParam(value = "referencia2Nome", required = false) String referencia2Nome,
            @RequestParam(value = "referencia2Relacao", required = false) String referencia2Relacao,
            @RequestParam(value = "referencia2Whatsapp", required = false) String referencia2Whatsapp,
            @RequestParam(value = "referencia2Conhece", required = false) Boolean referencia2Conhece,
            // Terceira Referência
            @RequestParam(value = "referencia3Nome", required = false) String referencia3Nome,
            @RequestParam(value = "referencia3Relacao", required = false) String referencia3Relacao,
            @RequestParam(value = "referencia3Whatsapp", required = false) String referencia3Whatsapp,
            @RequestParam(value = "referencia3Conhece", required = false) Boolean referencia3Conhece,
            @RequestParam(value = "observacao", required = false) String observacao,
            @RequestParam(value = "fotoDocumento", required = false) MultipartFile fotoDocumento,
            @RequestParam(value = "fotoSelfie", required = false) MultipartFile fotoSelfie) {
        
        try {
            // Verificar se o cliente existe
            Optional<Cliente> clienteExistente = clienteService.buscarPorId(id);
            if (clienteExistente.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteExistente.get();
            
            // Atualizar dados do cliente
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setCpf(cpf);
            cliente.setRg(rg);
            cliente.setTelefone(telefone);
            cliente.setWhatsapp(whatsapp);
            cliente.setCep(cep);
            cliente.setRua(rua);
            cliente.setNumero(numero);
            cliente.setComplemento(complemento);
            cliente.setBairro(bairro);
            cliente.setCidade(cidade);
            cliente.setEstado(estado);
            cliente.setNomeMae(nomeMae);
            cliente.setDataNascimento(java.time.LocalDate.parse(dataNascimento));
            cliente.setSexo(sexo);
            cliente.setEstadoCivil(estadoCivil);
            cliente.setNaturezaOcupacao(naturezaOcupacao);
            cliente.setProfissao(profissao);
            cliente.setNomeEmpresa(nomeEmpresa);
            cliente.setRendaMensal(rendaMensal);
            cliente.setComprovacaoRenda(comprovacaoRenda);
            cliente.setPossuiCarteiraAssinadaOuAposentado(possuiCarteiraAssinadaOuAposentado);
            // Primeira Referência
            cliente.setReferencia1Nome(referencia1Nome);
            cliente.setReferencia1Relacao(referencia1Relacao);
            cliente.setReferencia1Whatsapp(referencia1Whatsapp);
            cliente.setReferencia1Conhece(referencia1Conhece);
            // Segunda Referência
            cliente.setReferencia2Nome(referencia2Nome);
            cliente.setReferencia2Relacao(referencia2Relacao);
            cliente.setReferencia2Whatsapp(referencia2Whatsapp);
            cliente.setReferencia2Conhece(referencia2Conhece);
            // Terceira Referência
            cliente.setReferencia3Nome(referencia3Nome);
            cliente.setReferencia3Relacao(referencia3Relacao);
            cliente.setReferencia3Whatsapp(referencia3Whatsapp);
            cliente.setReferencia3Conhece(referencia3Conhece);
            
            cliente.setObservacao(observacao);

            // Atualizar foto do documento se fornecida
            if (fotoDocumento != null && !fotoDocumento.isEmpty()) {
                // Deletar foto anterior se existir
                if (cliente.getFotoDocumento() != null) {
                    try {
                        fileStorageService.deletarArquivo(cliente.getFotoDocumento());
                    } catch (IOException e) {
                        System.err.println("Erro ao deletar foto anterior do documento: " + e.getMessage());
                    }
                }
                // Salvar nova foto
                String nomeArquivoDocumento = fileStorageService.salvarArquivo(fotoDocumento);
                cliente.setFotoDocumento(nomeArquivoDocumento);
            }

            // Atualizar foto selfie se fornecida
            if (fotoSelfie != null && !fotoSelfie.isEmpty()) {
                // Deletar foto anterior se existir
                if (cliente.getFotoSelfie() != null) {
                    try {
                        fileStorageService.deletarArquivo(cliente.getFotoSelfie());
                    } catch (IOException e) {
                        System.err.println("Erro ao deletar foto anterior selfie: " + e.getMessage());
                    }
                }
                // Salvar nova foto
                String nomeArquivoSelfie = fileStorageService.salvarArquivo(fotoSelfie);
                cliente.setFotoSelfie(nomeArquivoSelfie);
            }

            // Salvar cliente atualizado
            Cliente clienteAtualizado = clienteService.salvar(cliente);
            return ResponseEntity.ok(clienteAtualizado);
            
        } catch (IOException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao processar dados: " + e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            // Buscar cliente para deletar fotos associadas
            Optional<Cliente> cliente = clienteService.buscarPorId(id);
            if (cliente.isPresent()) {
                try {
                    if (cliente.get().getFotoDocumento() != null) {
                        fileStorageService.deletarArquivo(cliente.get().getFotoDocumento());
                    }
                    if (cliente.get().getFotoSelfie() != null) {
                        fileStorageService.deletarArquivo(cliente.get().getFotoSelfie());
                    }
                } catch (IOException e) {
                    // Log do erro, mas não impede a exclusão do cliente
                    System.err.println("Erro ao deletar arquivos: " + e.getMessage());
                }
            }
            
            clienteService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PostMapping("/{id}/foto-documento")
    @Operation(summary = "Upload da foto do documento")
    public ResponseEntity<?> uploadFotoDocumento(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            
            // Deletar foto anterior se existir
            if (cliente.getFotoDocumento() != null) {
                try {
                    fileStorageService.deletarArquivo(cliente.getFotoDocumento());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar foto anterior: " + e.getMessage());
                }
            }
            
            // Salvar nova foto
            String nomeArquivo = fileStorageService.salvarArquivo(arquivo);
            cliente.setFotoDocumento(nomeArquivo);
            clienteService.salvar(cliente);
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Foto do documento enviada com sucesso");
            resposta.put("arquivo", nomeArquivo);
            return ResponseEntity.ok(resposta);
            
        } catch (IOException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @PostMapping("/{id}/foto-selfie")
    @Operation(summary = "Upload da foto selfie")
    public ResponseEntity<?> uploadFotoSelfie(@PathVariable Long id, @RequestParam("arquivo") MultipartFile arquivo) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            
            // Deletar foto anterior se existir
            if (cliente.getFotoSelfie() != null) {
                try {
                    fileStorageService.deletarArquivo(cliente.getFotoSelfie());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar foto anterior: " + e.getMessage());
                }
            }
            
            // Salvar nova foto
            String nomeArquivo = fileStorageService.salvarArquivo(arquivo);
            cliente.setFotoSelfie(nomeArquivo);
            clienteService.salvar(cliente);
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Foto selfie enviada com sucesso");
            resposta.put("arquivo", nomeArquivo);
            return ResponseEntity.ok(resposta);
            
        } catch (IOException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao salvar arquivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @GetMapping("/uploads/{nomeArquivo}")
    @Operation(summary = "Visualizar arquivo enviado")
    public ResponseEntity<Resource> visualizarArquivo(@PathVariable String nomeArquivo) {
        try {
            Path caminhoArquivo = fileStorageService.obterCaminhoArquivo(nomeArquivo);
            Resource resource = new UrlResource(caminhoArquivo.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/fotos")
    @Operation(summary = "Obter fotos do cliente em base64")
    public ResponseEntity<?> obterFotosBase64(@PathVariable Long id) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            Map<String, String> fotos = new HashMap<>();
            
            // Obter foto do documento em base64
            if (cliente.getFotoDocumento() != null) {
                try {
                    String fotoDocumentoBase64 = fileStorageService.obterArquivoBase64(cliente.getFotoDocumento());
                    if (fotoDocumentoBase64 != null) {
                        fotos.put("fotoDocumento", fotoDocumentoBase64);
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao converter foto do documento para base64: " + e.getMessage());
                }
            }
            
            // Obter foto selfie em base64
            if (cliente.getFotoSelfie() != null) {
                try {
                    String fotoSelfieBase64 = fileStorageService.obterArquivoBase64(cliente.getFotoSelfie());
                    if (fotoSelfieBase64 != null) {
                        fotos.put("fotoSelfie", fotoSelfieBase64);
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao converter foto selfie para base64: " + e.getMessage());
                }
            }
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("clienteId", id);
            resposta.put("fotos", fotos);
            
            return ResponseEntity.ok(resposta);
            
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    // Novos endpoints para Dashboard
    
    @GetMapping("/estatisticas")
    @Operation(summary = "Obter estatísticas do dashboard")
    public ResponseEntity<EstatisticasDTO> obterEstatisticas() {
        try {
            EstatisticasDTO estatisticas = clienteService.obterEstatisticas();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/paginado")
    @Operation(summary = "Listar clientes com paginação e filtros")
    public ResponseEntity<?> listarPaginado(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String whatsapp,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dataCadastro") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                       Sort.by(sortBy).ascending() : 
                       Sort.by(sortBy).descending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Cliente> clientes = clienteService.buscarComFiltros(
                nome, email, cpf, whatsapp, status, dataInicio, dataFim, pageable
            );
            
            Page<ClienteResponseDTO> response = clientes.map(ClienteResponseDTO::new);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do cliente")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id, 
            @Valid @RequestBody AtualizarStatusDTO statusDTO) {
        try {
            Cliente clienteAtualizado = clienteService.atualizarStatus(id, statusDTO.getStatus());
            return ResponseEntity.ok(new ClienteResponseDTO(clienteAtualizado));
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @GetMapping("/export/csv")
    @Operation(summary = "Exportar clientes para CSV")
    public void exportarCSV(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String whatsapp,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            HttpServletResponse response) throws IOException {
        
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, 
                          "attachment; filename=\"clientes_" + 
                          LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + 
                          ".csv\"");
        
        // Buscar todos os clientes com os filtros (sem paginação)
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<Cliente> clientes = clienteService.buscarComFiltros(
            nome, email, cpf, whatsapp, status, dataInicio, dataFim, pageable
        );
        
        PrintWriter writer = response.getWriter();
        
        // Cabeçalho do CSV
        writer.println("ID,Nome,CPF,Email,WhatsApp,Estado Civil,Data Nascimento,Status,Data Cadastro,Cidade,Estado");
        
        // Dados
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        for (Cliente cliente : clientes.getContent()) {
            writer.println(String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getWhatsapp(),
                cliente.getEstadoCivil(),
                cliente.getDataNascimento() != null ? cliente.getDataNascimento().format(dateFormatter) : "",
                cliente.getStatus(),
                cliente.getDataCadastro() != null ? cliente.getDataCadastro().format(dateTimeFormatter) : "",
                cliente.getCidade() != null ? cliente.getCidade() : "",
                cliente.getEstado() != null ? cliente.getEstado() : ""
            ));
        }
        
        writer.flush();
    }
    
    @DeleteMapping("/{id}/foto-documento")
    @Operation(summary = "Deletar foto do documento")
    public ResponseEntity<?> deletarFotoDocumento(@PathVariable Long id) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            
            // Deletar foto do documento se existir
            if (cliente.getFotoDocumento() != null) {
                try {
                    fileStorageService.deletarArquivo(cliente.getFotoDocumento());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar foto do documento: " + e.getMessage());
                }
                
                // Remover referência do banco de dados
                cliente.setFotoDocumento(null);
                clienteService.salvar(cliente);
            }
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Foto do documento deletada com sucesso");
            return ResponseEntity.ok(resposta);
            
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @DeleteMapping("/{id}/foto-selfie")
    @Operation(summary = "Deletar foto selfie")
    public ResponseEntity<?> deletarFotoSelfie(@PathVariable Long id) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Cliente não encontrado");
                return ResponseEntity.notFound().build();
            }
            
            Cliente cliente = clienteOpt.get();
            
            // Deletar foto selfie se existir
            if (cliente.getFotoSelfie() != null) {
                try {
                    fileStorageService.deletarArquivo(cliente.getFotoSelfie());
                } catch (IOException e) {
                    System.err.println("Erro ao deletar foto selfie: " + e.getMessage());
                }
                
                // Remover referência do banco de dados
                cliente.setFotoSelfie(null);
                clienteService.salvar(cliente);
            }
            
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Foto selfie deletada com sucesso");
            return ResponseEntity.ok(resposta);
            
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
