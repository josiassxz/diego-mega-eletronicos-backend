
package com.megaeletronicos.backend.service;

import com.megaeletronicos.backend.entity.Usuario;
import com.megaeletronicos.backend.entity.Vendedor;
import com.megaeletronicos.backend.repository.UsuarioRepository;
import com.megaeletronicos.backend.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private VendedorRepository vendedorRepository;
    
    public Optional<Usuario> autenticar(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha);
    }
    
    public boolean validarCredenciais(String login, String senha) {
        return usuarioRepository.findByLoginAndSenha(login, senha).isPresent();
    }
    
    public String buscarCpfVendedor(String login) {
        // Busca o vendedor pelo email (que é o login)
        Optional<Vendedor> vendedor = vendedorRepository.findByEmail(login);
        return vendedor.map(Vendedor::getCpf).orElse(null);
    }
}
