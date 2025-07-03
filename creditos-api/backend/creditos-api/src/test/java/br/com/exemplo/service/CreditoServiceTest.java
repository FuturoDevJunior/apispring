package br.com.exemplo.service;

import br.com.exemplo.dto.CreditoResponseDTO;
import br.com.exemplo.entity.Credito;
import br.com.exemplo.messaging.ConsultaPublisher;
import br.com.exemplo.repository.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditoServiceTest {

    @Mock
    private CreditoRepository creditoRepository;

    @Mock
    private ConsultaPublisher consultaPublisher;

    @InjectMocks
    private CreditoService creditoService;

    private Credito credito1;
    private Credito credito2;

    @BeforeEach
    void setUp() {
        credito1 = new Credito("123456", "7891011", LocalDate.of(2024, 2, 25),
                new BigDecimal("1500.75"), "ISSQN", true, new BigDecimal("5.0"),
                new BigDecimal("30000.00"), new BigDecimal("5000.00"), new BigDecimal("25000.00"));
        credito1.setId(1L);

        credito2 = new Credito("789012", "7891011", LocalDate.of(2024, 2, 26),
                new BigDecimal("1200.50"), "ISSQN", false, new BigDecimal("4.5"),
                new BigDecimal("25000.00"), new BigDecimal("4000.00"), new BigDecimal("21000.00"));
        credito2.setId(2L);
    }

    @Test
    void obterCreditosPorNfse_DeveRetornarListaDeCreditos() {
        // Arrange
        String numeroNfse = "7891011";
        List<Credito> creditos = Arrays.asList(credito1, credito2);
        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(creditos);

        // Act
        List<CreditoResponseDTO> resultado = creditoService.obterCreditosPorNfse(numeroNfse);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("123456", resultado.get(0).getNumeroCredito());
        assertEquals("789012", resultado.get(1).getNumeroCredito());
        assertEquals("Sim", resultado.get(0).getSimplesNacional());
        assertEquals("Não", resultado.get(1).getSimplesNacional());

        verify(creditoRepository).findByNumeroNfse(numeroNfse);
        verify(consultaPublisher).publishConsultaEvent(any());
    }

    @Test
    void obterCreditosPorNfse_DeveRetornarListaVaziaQuandoNaoEncontrar() {
        // Arrange
        String numeroNfse = "inexistente";
        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(Arrays.asList());

        // Act
        List<CreditoResponseDTO> resultado = creditoService.obterCreditosPorNfse(numeroNfse);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(creditoRepository).findByNumeroNfse(numeroNfse);
        verify(consultaPublisher).publishConsultaEvent(any());
    }

    @Test
    void obterCreditoPorNumero_DeveRetornarCredito() {
        // Arrange
        String numeroCredito = "123456";
        when(creditoRepository.findByNumeroCredito(numeroCredito)).thenReturn(Optional.of(credito1));

        // Act
        CreditoResponseDTO resultado = creditoService.obterCreditoPorNumero(numeroCredito);

        // Assert
        assertNotNull(resultado);
        assertEquals("123456", resultado.getNumeroCredito());
        assertEquals("7891011", resultado.getNumeroNfse());
        assertEquals(LocalDate.of(2024, 2, 25), resultado.getDataConstituicao());
        assertEquals(new BigDecimal("1500.75"), resultado.getValorIssqn());
        assertEquals("ISSQN", resultado.getTipoCredito());
        assertEquals("Sim", resultado.getSimplesNacional());

        verify(creditoRepository).findByNumeroCredito(numeroCredito);
        verify(consultaPublisher).publishConsultaEvent(any());
    }

    @Test
    void obterCreditoPorNumero_DeveLancarExcecaoQuandoNaoEncontrar() {
        // Arrange
        String numeroCredito = "inexistente";
        when(creditoRepository.findByNumeroCredito(numeroCredito)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> creditoService.obterCreditoPorNumero(numeroCredito));
        
        assertEquals("Crédito não encontrado", exception.getMessage());

        verify(creditoRepository).findByNumeroCredito(numeroCredito);
        verify(consultaPublisher, never()).publishConsultaEvent(any());
    }

    @Test
    void obterCreditosPorNfse_DevePublicarEventoCorretamente() {
        // Arrange
        String numeroNfse = "7891011";
        List<Credito> creditos = Arrays.asList(credito1);
        when(creditoRepository.findByNumeroNfse(numeroNfse)).thenReturn(creditos);

        // Act
        creditoService.obterCreditosPorNfse(numeroNfse);

        // Assert
        verify(consultaPublisher).publishConsultaEvent(argThat(evento -> 
            "NUMERO_NFSE".equals(evento.getTipoConsulta()) &&
            numeroNfse.equals(evento.getValorConsultado()) &&
            evento.getQuantidadeResultados() == 1
        ));
    }
}
