package com.megaeletronicos.backend.repository;

import com.megaeletronicos.backend.entity.Aparelho;
import com.megaeletronicos.backend.entity.Cliente;
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
public interface AparelhoRepository extends JpaRepository<Aparelho, Long> {
    
    Optional<Aparelho> findByImei(String imei);
    
    List<Aparelho> findByCliente(Cliente cliente);
    
    List<Aparelho> findByEmpresa(Empresa empresa);
    
    List<Aparelho> findByStatus(String status);
    
    @Query("SELECT a FROM Aparelho a WHERE LOWER(CAST(a.modelo AS text)) LIKE LOWER(CONCAT('%', :modelo, '%'))")
    List<Aparelho> findByModeloContainingIgnoreCase(@Param("modelo") String modelo);
    
    @Query("SELECT a FROM Aparelho a WHERE LOWER(CAST(a.marca AS text)) LIKE LOWER(CONCAT('%', :marca, '%'))")
    List<Aparelho> findByMarcaContainingIgnoreCase(@Param("marca") String marca);
    
    @Query("SELECT a FROM Aparelho a WHERE a.cliente.id = :clienteId")
    List<Aparelho> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT a FROM Aparelho a WHERE a.empresa.id = :empresaId")
    List<Aparelho> findByEmpresaId(@Param("empresaId") Long empresaId);
    
    // Métodos para estatísticas
    Long countByStatus(String status);
    
    @Query("SELECT COUNT(a) FROM Aparelho a WHERE a.dataCadastro >= :dataInicio")
    Long countByDataCadastroAfter(@Param("dataInicio") LocalDateTime dataInicio);
    
    @Query("SELECT SUM(a.valorTotal) FROM Aparelho a WHERE a.status = :status")
    Double sumValorTotalByStatus(@Param("status") String status);
    
    @Query("SELECT SUM(a.valorParcelado) FROM Aparelho a WHERE a.status = :status")
    Double sumValorParceladoByStatus(@Param("status") String status);
    
    // Busca com filtros e paginação
    @Query("SELECT a FROM Aparelho a " +
           "WHERE (:imei IS NULL OR CAST(a.imei AS text) LIKE CONCAT('%', :imei, '%')) " +
           "AND (:modelo IS NULL OR LOWER(CAST(a.modelo AS text)) LIKE LOWER(CONCAT('%', :modelo, '%'))) " +
           "AND (:marca IS NULL OR LOWER(CAST(a.marca AS text)) LIKE LOWER(CONCAT('%', :marca, '%'))) " +
           "AND (:clienteNome IS NULL OR LOWER(CAST(a.cliente.nome AS text)) LIKE LOWER(CONCAT('%', :clienteNome, '%'))) " +
           "AND (:empresaNome IS NULL OR LOWER(CAST(a.empresa.razaoSocial AS text)) LIKE LOWER(CONCAT('%', :empresaNome, '%'))) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "AND (:dataInicio IS NULL OR a.dataCadastro >= :dataInicio) " +
           "AND (:dataFim IS NULL OR a.dataCadastro <= :dataFim)")
    Page<Aparelho> buscarComFiltros(
        @Param("imei") String imei,
        @Param("modelo") String modelo,
        @Param("marca") String marca,
        @Param("clienteNome") String clienteNome,
        @Param("empresaNome") String empresaNome,
        @Param("status") String status,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim,
        Pageable pageable
    );
    
    // Buscar aparelhos próximos ao vencimento
    @Query("SELECT a FROM Aparelho a WHERE a.dataVencimento <= :dataLimite AND a.status = 'Ativo'")
    List<Aparelho> findAparelhosProximosVencimento(@Param("dataLimite") java.time.LocalDate dataLimite);
}