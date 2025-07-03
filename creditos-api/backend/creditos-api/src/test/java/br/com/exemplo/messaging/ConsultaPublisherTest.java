package br.com.exemplo.messaging;

import br.com.exemplo.dto.ConsultaEventDTO;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaPublisherTest {

    @Mock
    private KafkaTemplate<String, ConsultaEventDTO> kafkaTemplate;

    @InjectMocks
    private ConsultaPublisher consultaPublisher;

    private ConsultaEventDTO consultaEventDTO;
    private final String TOPIC_CONSULTA_CREDITOS = "consulta-creditos";

    @BeforeEach
    void setUp() {
        consultaEventDTO = new ConsultaEventDTO();
        consultaEventDTO.setTipoConsulta("SPC");
        consultaEventDTO.setValorConsultado("1000");
        consultaEventDTO.setQuantidadeResultados(5);
        consultaEventDTO.setTimestampConsulta(LocalDateTime.of(2024, 1, 15, 10, 30, 0));
        consultaEventDTO.setEnderecoIp("192.168.1.1");
        consultaEventDTO.setUserAgent("test-agent");
    }

    @Test
    void testPublishConsultaEvent_Success() {
        // Arrange
        CompletableFuture<SendResult<String, ConsultaEventDTO>> future = new CompletableFuture<>();
        
        // Criando um RecordMetadata mock
        TopicPartition topicPartition = new TopicPartition(TOPIC_CONSULTA_CREDITOS, 0);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 0L, 0L, 0L, 0L, 0, 0);
        
        // Criando um SendResult mock
        SendResult<String, ConsultaEventDTO> sendResult = mock(SendResult.class);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        
        future.complete(sendResult);

        when(kafkaTemplate.send(eq(TOPIC_CONSULTA_CREDITOS), anyString(), eq(consultaEventDTO)))
                .thenReturn(future);

        // Act
        consultaPublisher.publishConsultaEvent(consultaEventDTO);

        // Assert
        verify(kafkaTemplate, times(1)).send(
                eq(TOPIC_CONSULTA_CREDITOS), 
                eq("SPC_2024-01-15T10:30"), 
                eq(consultaEventDTO)
        );
    }

    @Test
    void testPublishConsultaEvent_KafkaFailure() {
        // Arrange
        CompletableFuture<SendResult<String, ConsultaEventDTO>> future = new CompletableFuture<>();
        RuntimeException kafkaException = new RuntimeException("Kafka connection failed");
        future.completeExceptionally(kafkaException);

        // Usando doReturn() conforme recomendação do roteiro
        doReturn(future).when(kafkaTemplate).send(eq(TOPIC_CONSULTA_CREDITOS), anyString(), eq(consultaEventDTO));

        // Act - O método agora apenas loga, não lança exceção imediatamente
        consultaPublisher.publishConsultaEvent(consultaEventDTO);
        
        // Assert - Verifica que o kafka foi chamado
        verify(kafkaTemplate, times(1)).send(
                eq(TOPIC_CONSULTA_CREDITOS), 
                anyString(), 
                eq(consultaEventDTO)
        );
        
        // O callback de exceção é executado de forma assíncrona,
        // então só verificamos se o método foi chamado sem erro direto
    }

    @Test
    void testPublishConsultaEvent_Exception() {
        // Arrange
        when(kafkaTemplate.send(eq(TOPIC_CONSULTA_CREDITOS), anyString(), eq(consultaEventDTO)))
                .thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert - Verifica se a exceção é relançada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consultaPublisher.publishConsultaEvent(consultaEventDTO);
        });
        
        // Verifica a mensagem da exceção
        assertEquals("Kafka indisponível", exception.getMessage());

        // Assert
        verify(kafkaTemplate, times(1)).send(
                eq(TOPIC_CONSULTA_CREDITOS), 
                anyString(), 
                eq(consultaEventDTO)
        );
    }

    @Test
    void testPublishConsultaEvent_KeyGeneration() {
        // Arrange
        CompletableFuture<SendResult<String, ConsultaEventDTO>> future = new CompletableFuture<>();
        
        TopicPartition topicPartition = new TopicPartition(TOPIC_CONSULTA_CREDITOS, 0);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 0L, 0L, 0L, 0L, 0, 0);
        SendResult<String, ConsultaEventDTO> sendResult = mock(SendResult.class);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        
        future.complete(sendResult);

        when(kafkaTemplate.send(eq(TOPIC_CONSULTA_CREDITOS), anyString(), eq(consultaEventDTO)))
                .thenReturn(future);

        // Act
        consultaPublisher.publishConsultaEvent(consultaEventDTO);

        // Assert - Verificar se a chave é gerada corretamente
        String expectedKey = "SPC_2024-01-15T10:30";
        verify(kafkaTemplate, times(1)).send(
                eq(TOPIC_CONSULTA_CREDITOS), 
                eq(expectedKey), 
                eq(consultaEventDTO)
        );
    }

    @Test
    void testPublishConsultaEvent_WithDifferentEventData() {
        // Arrange
        ConsultaEventDTO differentEvent = new ConsultaEventDTO();
        differentEvent.setTipoConsulta("SERASA");
        differentEvent.setValorConsultado("2500");
        differentEvent.setQuantidadeResultados(3);
        differentEvent.setTimestampConsulta(LocalDateTime.of(2024, 2, 20, 14, 45, 30));
        differentEvent.setEnderecoIp("192.168.1.2");
        differentEvent.setUserAgent("test-agent-2");

        CompletableFuture<SendResult<String, ConsultaEventDTO>> future = new CompletableFuture<>();
        
        TopicPartition topicPartition = new TopicPartition(TOPIC_CONSULTA_CREDITOS, 0);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition, 0L, 0L, 0L, 0L, 0, 0);
        SendResult<String, ConsultaEventDTO> sendResult = mock(SendResult.class);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        
        future.complete(sendResult);

        when(kafkaTemplate.send(eq(TOPIC_CONSULTA_CREDITOS), anyString(), eq(differentEvent)))
                .thenReturn(future);

        // Act
        consultaPublisher.publishConsultaEvent(differentEvent);

        // Assert
        String expectedKey = "SERASA_2024-02-20T14:45:30";
        verify(kafkaTemplate, times(1)).send(
                eq(TOPIC_CONSULTA_CREDITOS), 
                eq(expectedKey), 
                eq(differentEvent)
        );
    }

    @Test
    void testPublishConsultaEvent_NullFieldsHandling() {
        // Arrange
        ConsultaEventDTO nullFieldEvent = new ConsultaEventDTO();
        nullFieldEvent.setTipoConsulta(null);
        nullFieldEvent.setTimestampConsulta(null);
        nullFieldEvent.setValorConsultado("1500");
        nullFieldEvent.setQuantidadeResultados(1);
        nullFieldEvent.setEnderecoIp("192.168.1.1");
        nullFieldEvent.setUserAgent("test-agent");

        // Act & Assert - O método captura exceções internamente e não as relança
        assertDoesNotThrow(() -> {
            consultaPublisher.publishConsultaEvent(nullFieldEvent);
        });
        
        // Assert - Kafka não deve ser chamado quando ocorre NullPointerException interno
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any(ConsultaEventDTO.class));
    }
}
