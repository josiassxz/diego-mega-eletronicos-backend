
package com.megaeletronicos.backend.repository;

import com.megaeletronicos.backend.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByCpf(String cpf);
    
    @Query("SELECT c FROM Cliente c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    @Query("SELECT c FROM Cliente c WHERE c.cidade = :cidade")
    List<Cliente> findByCidade(@Param("cidade") String cidade);
    
    @Query("SELECT c FROM Cliente c WHERE c.estado = :estado")
    List<Cliente> findByEstado(@Param("estado") String estado);
    
    // Novos métodos para estatísticas e filtros
    Long countByStatus(String status);
    
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.dataCadastro >= :dataInicio")
    Long countByDataCadastroAfter(@Param("dataInicio") LocalDateTime dataInicio);
    

}
