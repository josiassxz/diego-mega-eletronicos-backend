
package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.entity.Usuario;
import com.megaeletronicos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }
    
    public Usuario salvar(Usuario usuario) {
        // Verificar se já existe usuário com mesmo login
        if (usuario.getId() == null) {
            if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent()) {
                throw new RuntimeException("Já existe um usuário com este login");
            }
        } else {
            // Para atualizações, verificar se login não pertence a outro usuário
            Optional<Usuario> usuarioComMesmoLogin = usuarioRepository.findByLogin(usuario.getLogin());
            if (usuarioComMesmoLogin.isPresent() && !usuarioComMesmoLogin.get().getId().equals(usuario.getId())) {
                throw new RuntimeException("Já existe outro usuário com este login");
            }
        }
        
        return usuarioRepository.save(usuario);
    }
    
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
    
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        usuarioAtualizado.setId(id);
        return salvar(usuarioAtualizado);
    }
}
