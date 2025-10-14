package com.megaeletronicos.backend.repository;

import com.megaeletronicos.backend.entity.Empresa;
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
public interface EmpresaRepository extends JpaRepository<Empresa, Long>, JpaSpecificationExecutor<Empresa> {
    
    Optional<Empresa> findByCnpj(String cnpj);
    
    Optional<Empresa> findByEmail(String email);
    
    @Query("SELECT e FROM Empresa e WHERE e.razaoSocial LIKE CONCAT('%', :razaoSocial, '%')")
    List<Empresa> findByRazaoSocialContainingIgnoreCase(@Param("razaoSocial") String razaoSocial);
    
    @Query("SELECT e FROM Empresa e WHERE e.nomeFantasia LIKE CONCAT('%', :nomeFantasia, '%')")
    List<Empresa> findByNomeFantasiaContainingIgnoreCase(@Param("nomeFantasia") String nomeFantasia);
    
    @Query("SELECT e FROM Empresa e WHERE e.cidade = :cidade")
    List<Empresa> findByCidade(@Param("cidade") String cidade);
    
    @Query("SELECT e FROM Empresa e WHERE e.estado = :estado")
    List<Empresa> findByEstado(@Param("estado") String estado);
    
    @Query("SELECT e FROM Empresa e WHERE e.status = :status")
    List<Empresa> findByStatus(@Param("status") String status);
    
    // Métodos para estatísticas
    Long countByStatus(String status);
    
    @Query("SELECT COUNT(e) FROM Empresa e WHERE e.dataCadastro >= :dataInicio")
    Long countByDataCadastroAfter(@Param("dataInicio") LocalDateTime dataInicio);
    

    
    // Busca por CEP para empresas na mesma região
    @Query("SELECT e FROM Empresa e WHERE e.cep = :cep")
    List<Empresa> findByCep(@Param("cep") String cep);
    
    // Busca por bairro
    @Query("SELECT e FROM Empresa e WHERE e.bairro LIKE CONCAT('%', :bairro, '%')")
    List<Empresa> findByBairroContainingIgnoreCase(@Param("bairro") String bairro);
}