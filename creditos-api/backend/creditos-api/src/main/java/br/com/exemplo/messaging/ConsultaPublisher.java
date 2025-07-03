package br.com.exemplo.messaging;

import br.com.exemplo.dto.ConsultaEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ConsultaPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaPublisher.class);
    private static final String TOPIC_CONSULTA_CREDITOS = "consulta-creditos";

    @Autowired
    private KafkaTemplate<String, ConsultaEventDTO> kafkaTemplate;

    /**
     * Publica evento de consulta no tópico Kafka
     */
    public void publishConsultaEvent(ConsultaEventDTO evento) {
        try {
            String key = evento.getTipoConsulta() + "_" + evento.getTimestampConsulta().toString();
            
            CompletableFuture<SendResult<String, ConsultaEventDTO>> future = 
                kafkaTemplate.send(TOPIC_CONSULTA_CREDITOS, key, evento);
            
            future.thenAccept(result -> {
                logger.info("Evento de consulta enviado com sucesso: {} no offset: {}", 
                           evento.getValorConsultado(), result.getRecordMetadata().offset());
            }).exceptionally(failure -> {
                logger.error("Falha ao enviar evento de consulta: {}", evento.getValorConsultado(), failure);
                return null;
            });
            
        } catch (Exception e) {
            logger.error("Erro ao publicar evento de consulta no Kafka", e);
        }
    }
}
