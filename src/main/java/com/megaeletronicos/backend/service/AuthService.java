
package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.entity.Usuario;
import com.megaeletronicos.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Optional<Usuario> autenticar(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }
    
    public boolean validarCredenciais(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha).isPresent();
    }
}
