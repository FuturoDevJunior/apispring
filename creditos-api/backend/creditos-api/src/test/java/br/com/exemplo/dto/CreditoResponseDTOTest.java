package br.com.exemplo.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditoResponseDTOTest {

    @Test
    void testConstructorPadrao() {
        // Given & When
        CreditoResponseDTO dto = new CreditoResponseDTO();
        
        // Then
        assertNull(dto.getNumeroCredito());
        assertNull(dto.getNumeroNfse());
        assertNull(dto.getDataConstituicao());
        assertNull(dto.getValorIssqn());
        assertNull(dto.getTipoCredito());
        assertNull(dto.getSimplesNacional());
        assertNull(dto.getAliquota());
        assertNull(dto.getValorFaturado());
        assertNull(dto.getValorDeducao());
        assertNull(dto.getBaseCalculo());
    }

    @Test
    void testConstructorCompleto() {
        // Given
        String numeroCredito = "123456";
        String numeroNfse = "789012";
        LocalDate dataConstituicao = LocalDate.of(2024, 1, 15);
        BigDecimal valorIssqn = new BigDecimal("1500.50");
        String tipoCredito = "ISSQN";
        boolean simplesNacional = true;
        BigDecimal aliquota = new BigDecimal("5.0");
        BigDecimal valorFaturado = new BigDecimal("30000.00");
        BigDecimal valorDeducao = new BigDecimal("5000.00");
        BigDecimal baseCalculo = new BigDecimal("25000.00");
        
        // When
        CreditoResponseDTO dto = new CreditoResponseDTO(
            numeroCredito, numeroNfse, dataConstituicao, valorIssqn, 
            tipoCredito, simplesNacional, aliquota, valorFaturado, 
            valorDeducao, baseCalculo
        );
        
        // Then
        assertEquals(numeroCredito, dto.getNumeroCredito());
        assertEquals(numeroNfse, dto.getNumeroNfse());
        assertEquals(dataConstituicao, dto.getDataConstituicao());
        assertEquals(valorIssqn, dto.getValorIssqn());
        assertEquals(tipoCredito, dto.getTipoCredito());
        assertEquals("Sim", dto.getSimplesNacional());
        assertEquals(aliquota, dto.getAliquota());
        assertEquals(valorFaturado, dto.getValorFaturado());
        assertEquals(valorDeducao, dto.getValorDeducao());
        assertEquals(baseCalculo, dto.getBaseCalculo());
    }

    @Test
    void testGettersESetters() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        String numeroCredito = "654321";
        String numeroNfse = "210987";
        LocalDate dataConstituicao = LocalDate.of(2023, 12, 31);
        BigDecimal valorIssqn = new BigDecimal("2500.75");
        String tipoCredito = "ISS";
        String simplesNacional = "Não";
        BigDecimal aliquota = new BigDecimal("3.5");
        BigDecimal valorFaturado = new BigDecimal("50000.00");
        BigDecimal valorDeducao = new BigDecimal("7500.00");
        BigDecimal baseCalculo = new BigDecimal("42500.00");
        
        // When
        dto.setNumeroCredito(numeroCredito);
        dto.setNumeroNfse(numeroNfse);
        dto.setDataConstituicao(dataConstituicao);
        dto.setValorIssqn(valorIssqn);
        dto.setTipoCredito(tipoCredito);
        dto.setSimplesNacional(simplesNacional);
        dto.setAliquota(aliquota);
        dto.setValorFaturado(valorFaturado);
        dto.setValorDeducao(valorDeducao);
        dto.setBaseCalculo(baseCalculo);
        
        // Then
        assertEquals(numeroCredito, dto.getNumeroCredito());
        assertEquals(numeroNfse, dto.getNumeroNfse());
        assertEquals(dataConstituicao, dto.getDataConstituicao());
        assertEquals(valorIssqn, dto.getValorIssqn());
        assertEquals(tipoCredito, dto.getTipoCredito());
        assertEquals(simplesNacional, dto.getSimplesNacional());
        assertEquals(aliquota, dto.getAliquota());
        assertEquals(valorFaturado, dto.getValorFaturado());
        assertEquals(valorDeducao, dto.getValorDeducao());
        assertEquals(baseCalculo, dto.getBaseCalculo());
    }

    @Test
    void testSimplesNacionalTrue() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        
        // When
        dto.setSimplesNacional("Sim");
        
        // Then
        assertEquals("Sim", dto.getSimplesNacional());
    }

    @Test
    void testSimplesNacionalFalse() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        
        // When
        dto.setSimplesNacional("Não");
        
        // Then
        assertEquals("Não", dto.getSimplesNacional());
    }

    @Test
    void testValoresBigDecimalZero() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        BigDecimal zero = BigDecimal.ZERO;
        
        // When
        dto.setValorIssqn(zero);
        dto.setAliquota(zero);
        dto.setValorFaturado(zero);
        dto.setValorDeducao(zero);
        dto.setBaseCalculo(zero);
        
        // Then
        assertEquals(zero, dto.getValorIssqn());
        assertEquals(zero, dto.getAliquota());
        assertEquals(zero, dto.getValorFaturado());
        assertEquals(zero, dto.getValorDeducao());
        assertEquals(zero, dto.getBaseCalculo());
    }

    @Test
    void testValoresNegativos() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        BigDecimal valorNegativo = new BigDecimal("-100.00");
        
        // When
        dto.setValorIssqn(valorNegativo);
        dto.setValorDeducao(valorNegativo);
        
        // Then
        assertEquals(valorNegativo, dto.getValorIssqn());
        assertEquals(valorNegativo, dto.getValorDeducao());
    }

    @Test
    void testTiposCreditoComuns() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        
        // When & Then - ISSQN
        dto.setTipoCredito("ISSQN");
        assertEquals("ISSQN", dto.getTipoCredito());
        
        // When & Then - ISS
        dto.setTipoCredito("ISS");
        assertEquals("ISS", dto.getTipoCredito());
        
        // When & Then - Outros
        dto.setTipoCredito("OUTROS");
        assertEquals("OUTROS", dto.getTipoCredito());
    }

    @Test
    void testDatasFuturas() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        LocalDate dataFutura = LocalDate.now().plusYears(1);
        
        // When
        dto.setDataConstituicao(dataFutura);
        
        // Then
        assertEquals(dataFutura, dto.getDataConstituicao());
    }

    @Test
    void testDatasPassadas() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        LocalDate dataPassada = LocalDate.of(2020, 1, 1);
        
        // When
        dto.setDataConstituicao(dataPassada);
        
        // Then
        assertEquals(dataPassada, dto.getDataConstituicao());
    }

    @Test
    void testValoresPrecisao() {
        // Given
        CreditoResponseDTO dto = new CreditoResponseDTO();
        BigDecimal valorPreciso = new BigDecimal("1234.5678");
        
        // When
        dto.setValorIssqn(valorPreciso);
        dto.setAliquota(valorPreciso);
        
        // Then
        assertEquals(valorPreciso, dto.getValorIssqn());
        assertEquals(valorPreciso, dto.getAliquota());
    }
}
