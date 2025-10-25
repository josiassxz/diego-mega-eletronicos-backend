package com.megaeletronicos.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataMigration implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se existem vendedores sem senha
        Integer countSenha = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM vendedores WHERE senha IS NULL OR senha = ''", 
            Integer.class
        );

        if (countSenha != null && countSenha > 0) {
            // Senha padrão simples (em produção deveria ser criptografada)
            String senhaDefault = "123456";
            
            // Atualiza vendedores sem senha
            int updatedSenha = jdbcTemplate.update(
                "UPDATE vendedores SET senha = ? WHERE senha IS NULL OR senha = ''", 
                senhaDefault
            );
            
            System.out.println("Migração de senha concluída: " + updatedSenha + " vendedores atualizados com senha padrão '123456'.");
        }

        // Verifica se existem vendedores sem email
        Integer countEmail = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM vendedores WHERE email IS NULL OR email = ''", 
            Integer.class
        );

        if (countEmail != null && countEmail > 0) {
            // Gera emails padrão baseados no ID do vendedor
            int updatedEmail = jdbcTemplate.update(
                "UPDATE vendedores SET email = CONCAT('vendedor', id, '@megaeletronicos.com') WHERE email IS NULL OR email = ''"
            );
            
            System.out.println("Migração de email concluída: " + updatedEmail + " vendedores atualizados com emails padrão.");
        }
    }
}