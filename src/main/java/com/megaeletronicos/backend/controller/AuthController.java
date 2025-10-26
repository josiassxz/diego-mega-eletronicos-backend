
package com.megaeletronicos.backend.controller;

import com.megaeletronicos.backend.entity.Usuario;
import com.megaeletronicos.backend.service.AuthService;
import com.megaeletronicos.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "API para autenticação e gerenciamento de usuários")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    @Operation(summary = "Fazer login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String login = credenciais.get("login");
        String senha = credenciais.get("senha");
        
        if (login == null || senha == null) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Login e senha são obrigatórios");
            return ResponseEntity.badRequest().body(erro);
        }
        
        Optional<Usuario> usuario = authService.autenticar(login, senha);
        
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", user.getId());
            usuarioData.put("login", user.getLogin());
            usuarioData.put("perfil", user.getPerfil());
            
            // Se for vendedor, buscar o CPF
            if ("VENDEDOR".equalsIgnoreCase(user.getPerfil())) {
                String cpfVendedor = authService.buscarCpfVendedor(user.getLogin());
                if (cpfVendedor != null) {
                    usuarioData.put("cpf", cpfVendedor);
                }
            }
            
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Login realizado com sucesso");
            resposta.put("usuario", usuarioData);
            return ResponseEntity.ok(resposta);
        } else {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Credenciais inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
        }
    }
    
    @PostMapping("/usuarios")
    @Operation(summary = "Criar novo usuário")
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = usuarioService.salvar(usuario);
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário criado com sucesso");
            resposta.put("usuario", usuarioSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @GetMapping("/usuarios")
    @Operation(summary = "Listar todos os usuários")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
    
    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        return usuario.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário atualizado com sucesso");
            resposta.put("usuario", usuarioAtualizado);
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
    
    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Deletar usuário")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deletar(id);
            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário deletado com sucesso");
            return ResponseEntity.ok(resposta);
        } catch (RuntimeException e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }
    }
}
