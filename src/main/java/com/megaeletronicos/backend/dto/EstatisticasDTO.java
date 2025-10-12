
package com.megaeletronicos.backend.dto;

import java.util.Map;

public class EstatisticasDTO {
    private Long totalClientes;
    private Long clientesHoje;
    private Long clientesPendentes;
    private Long clientesAprovados;
    private Long clientesRecusados;
    private Long clientesEmAnalise;
    private Map<String, Long> distribuicaoPorStatus;
    
    public EstatisticasDTO() {}
    
    public EstatisticasDTO(Long totalClientes, Long clientesHoje, Long clientesPendentes, 
                          Long clientesAprovados, Long clientesRecusados, Long clientesEmAnalise) {
        this.totalClientes = totalClientes;
        this.clientesHoje = clientesHoje;
        this.clientesPendentes = clientesPendentes;
        this.clientesAprovados = clientesAprovados;
        this.clientesRecusados = clientesRecusados;
        this.clientesEmAnalise = clientesEmAnalise;
    }
    
    // Getters e Setters
    public Long getTotalClientes() { return totalClientes; }
    public void setTotalClientes(Long totalClientes) { this.totalClientes = totalClientes; }
    
    public Long getClientesHoje() { return clientesHoje; }
    public void setClientesHoje(Long clientesHoje) { this.clientesHoje = clientesHoje; }
    
    public Long getClientesPendentes() { return clientesPendentes; }
    public void setClientesPendentes(Long clientesPendentes) { this.clientesPendentes = clientesPendentes; }
    
    public Long getClientesAprovados() { return clientesAprovados; }
    public void setClientesAprovados(Long clientesAprovados) { this.clientesAprovados = clientesAprovados; }
    
    public Long getClientesRecusados() { return clientesRecusados; }
    public void setClientesRecusados(Long clientesRecusados) { this.clientesRecusados = clientesRecusados; }
    
    public Long getClientesEmAnalise() { return clientesEmAnalise; }
    public void setClientesEmAnalise(Long clientesEmAnalise) { this.clientesEmAnalise = clientesEmAnalise; }
    
    public Map<String, Long> getDistribuicaoPorStatus() { return distribuicaoPorStatus; }
    public void setDistribuicaoPorStatus(Map<String, Long> distribuicaoPorStatus) { 
        this.distribuicaoPorStatus = distribuicaoPorStatus; 
    }
}
