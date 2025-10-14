package com.megaeletronicos.backend.repository;

import com.megaeletronicos.backend.entity.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    
    Optional<Empresa> findByCnpj(String cnpj);
    
    Optional<Empresa> findByEmail(String email);
    
    @Query("SELECT e FROM Empresa e WHERE LOWER(e.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))")
    List<Empresa> findByRazaoSocialContainingIgnoreCase(@Param("razaoSocial") String razaoSocial);
    
    @Query("SELECT e FROM Empresa e WHERE LOWER(e.nomeFantasia) LIKE LOWER(CONCAT('%', :nomeFantasia, '%'))")
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
    
    // Método para busca com filtros e paginação
    @Query("SELECT e FROM Empresa e WHERE " +
           "(:razaoSocial IS NULL OR LOWER(e.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))) AND " +
           "(:nomeFantasia IS NULL OR LOWER(e.nomeFantasia) LIKE LOWER(CONCAT('%', :nomeFantasia, '%'))) AND " +
           "(:cnpj IS NULL OR e.cnpj LIKE CONCAT('%', :cnpj, '%')) AND " +
           "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:cidade IS NULL OR LOWER(e.cidade) LIKE LOWER(CONCAT('%', :cidade, '%'))) AND " +
           "(:estado IS NULL OR e.estado = :estado) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:dataInicio IS NULL OR e.dataCadastro >= :dataInicio) AND " +
           "(:dataFim IS NULL OR e.dataCadastro <= :dataFim)")
    Page<Empresa> buscarComFiltros(
        @Param("razaoSocial") String razaoSocial,
        @Param("nomeFantasia") String nomeFantasia,
        @Param("cnpj") String cnpj,
        @Param("email") String email,
        @Param("cidade") String cidade,
        @Param("estado") String estado,
        @Param("status") String status,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim,
        Pageable pageable
    );
    
    // Busca por CEP para empresas na mesma região
    @Query("SELECT e FROM Empresa e WHERE e.cep = :cep")
    List<Empresa> findByCep(@Param("cep") String cep);
    
    // Busca por bairro
    @Query("SELECT e FROM Empresa e WHERE LOWER(e.bairro) LIKE LOWER(CONCAT('%', :bairro, '%'))")
    List<Empresa> findByBairroContainingIgnoreCase(@Param("bairro") String bairro);
}