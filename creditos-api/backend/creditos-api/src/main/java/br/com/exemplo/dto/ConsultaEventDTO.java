package br.com.exemplo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class ConsultaEventDTO {

    private String tipoConsulta; // "NUMERO_NFSE" ou "NUMERO_CREDITO"
    private String valorConsultado;
    private int quantidadeResultados;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestampConsulta;
    
    private String enderecoIp;
    private String userAgent;

    // Construtor padrão
    public ConsultaEventDTO() {
        this.timestampConsulta = LocalDateTime.now();
    }

    // Construtor completo
    public ConsultaEventDTO(String tipoConsulta, String valorConsultado, int quantidadeResultados,
                           String enderecoIp, String userAgent) {
        this();
        this.tipoConsulta = tipoConsulta;
        this.valorConsultado = valorConsultado;
        this.quantidadeResultados = quantidadeResultados;
        this.enderecoIp = enderecoIp;
        this.userAgent = userAgent;
    }

    // Getters e Setters
    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    public String getValorConsultado() {
        return valorConsultado;
    }

    public void setValorConsultado(String valorConsultado) {
        this.valorConsultado = valorConsultado;
    }

    public int getQuantidadeResultados() {
        return quantidadeResultados;
    }

    public void setQuantidadeResultados(int quantidadeResultados) {
        this.quantidadeResultados = quantidadeResultados;
    }

    public LocalDateTime getTimestampConsulta() {
        return timestampConsulta;
    }

    public void setTimestampConsulta(LocalDateTime timestampConsulta) {
        this.timestampConsulta = timestampConsulta;
    }

    public String getEnderecoIp() {
        return enderecoIp;
    }

    public void setEnderecoIp(String enderecoIp) {
        this.enderecoIp = enderecoIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
