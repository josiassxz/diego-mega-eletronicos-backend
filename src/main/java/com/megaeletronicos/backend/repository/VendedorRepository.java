package com.megaeletronicos.backend.repository;

import com.megaeletronicos.backend.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long>, JpaSpecificationExecutor<Vendedor> {
    
    Optional<Vendedor> findByCpf(String cpf);
    
    @Query("SELECT v FROM Vendedor v WHERE LOWER(v.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Vendedor> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    @Query("SELECT v FROM Vendedor v WHERE v.status = :status")
    List<Vendedor> findByStatus(@Param("status") String status);
    
    // Métodos para estatísticas
    Long countByStatus(String status);
    
    @Query("SELECT COUNT(v) FROM Vendedor v WHERE v.dataCadastro >= :dataInicio")
    Long countByDataCadastroAfter(@Param("dataInicio") LocalDateTime dataInicio);
    
    // Verificar se CPF já existe (excluindo um ID específico para edição)
    @Query("SELECT COUNT(v) FROM Vendedor v WHERE v.cpf = :cpf AND v.id != :id")
    Long countByCpfAndIdNot(@Param("cpf") String cpf, @Param("id") Long id);
}