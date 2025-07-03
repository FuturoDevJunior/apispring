package br.com.exemplo.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConsultaEventDTOTest {

    @Test
    void testConstructorPadrao() {
        // Given & When
        ConsultaEventDTO dto = new ConsultaEventDTO();
        
        // Then
        assertNotNull(dto.getTimestampConsulta());
        assertTrue(dto.getTimestampConsulta().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(dto.getTimestampConsulta().isAfter(LocalDateTime.now().minusSeconds(5)));
    }

    @Test
    void testConstructorCompleto() {
        // Given
        String tipoConsulta = "NUMERO_NFSE";
        String valorConsultado = "123456";
        int quantidadeResultados = 1;
        String enderecoIp = "192.168.1.1";
        String userAgent = "Mozilla/5.0";
        
        // When
        ConsultaEventDTO dto = new ConsultaEventDTO(
            tipoConsulta, valorConsultado, quantidadeResultados, enderecoIp, userAgent
        );
        
        // Then
        assertEquals(tipoConsulta, dto.getTipoConsulta());
        assertEquals(valorConsultado, dto.getValorConsultado());
        assertEquals(quantidadeResultados, dto.getQuantidadeResultados());
        assertEquals(enderecoIp, dto.getEnderecoIp());
        assertEquals(userAgent, dto.getUserAgent());
        assertNotNull(dto.getTimestampConsulta());
    }

    @Test
    void testGettersESetters() {
        // Given
        ConsultaEventDTO dto = new ConsultaEventDTO();
        String tipoConsulta = "NUMERO_CREDITO";
        String valorConsultado = "987654";
        int quantidadeResultados = 2;
        LocalDateTime timestamp = LocalDateTime.now().minusHours(1);
        String enderecoIp = "10.0.0.1";
        String userAgent = "Chrome/90.0";
        
        // When
        dto.setTipoConsulta(tipoConsulta);
        dto.setValorConsultado(valorConsultado);
        dto.setQuantidadeResultados(quantidadeResultados);
        dto.setTimestampConsulta(timestamp);
        dto.setEnderecoIp(enderecoIp);
        dto.setUserAgent(userAgent);
        
        // Then
        assertEquals(tipoConsulta, dto.getTipoConsulta());
        assertEquals(valorConsultado, dto.getValorConsultado());
        assertEquals(quantidadeResultados, dto.getQuantidadeResultados());
        assertEquals(timestamp, dto.getTimestampConsulta());
        assertEquals(enderecoIp, dto.getEnderecoIp());
        assertEquals(userAgent, dto.getUserAgent());
    }

    @Test
    void testTipoConsultaNfse() {
        // Given
        ConsultaEventDTO dto = new ConsultaEventDTO();
        
        // When
        dto.setTipoConsulta("NUMERO_NFSE");
        
        // Then
        assertEquals("NUMERO_NFSE", dto.getTipoConsulta());
    }

    @Test
    void testTipoConsultaCredito() {
        // Given
        ConsultaEventDTO dto = new ConsultaEventDTO();
        
        // When
        dto.setTipoConsulta("NUMERO_CREDITO");
        
        // Then
        assertEquals("NUMERO_CREDITO", dto.getTipoConsulta());
    }

    @Test
    void testQuantidadeResultadosZero() {
        // Given
        ConsultaEventDTO dto = new ConsultaEventDTO();
        
        // When
        dto.setQuantidadeResultados(0);
        
        // Then
        assertEquals(0, dto.getQuantidadeResultados());
    }

    @Test
    void testQuantidadeResultadosMultiplos() {
        // Given
        ConsultaEventDTO dto = new ConsultaEventDTO();
        
        // When
        dto.setQuantidadeResultados(5);
        
        // Then
        assertEquals(5, dto.getQuantidadeResultados());
    }

    @Test
    void testValoresNulos() {
        // Given
        ConsultaEventDTO dto = new ConsultaEventDTO();
        
        // When
        dto.setTipoConsulta(null);
        dto.setValorConsultado(null);
        dto.setEnderecoIp(null);
        dto.setUserAgent(null);
        dto.setTimestampConsulta(null);
        
        // Then
        assertNull(dto.getTipoConsulta());
        assertNull(dto.getValorConsultado());
        assertNull(dto.getEnderecoIp());
        assertNull(dto.getUserAgent());
        assertNull(dto.getTimestampConsulta());
    }
}
