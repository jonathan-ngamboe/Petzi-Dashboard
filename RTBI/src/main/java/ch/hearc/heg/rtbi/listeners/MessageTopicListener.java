package ch.hearc.heg.rtbi.listeners;

import ch.hearc.heg.rtbi.services.SseService;
import ch.hearc.heg.rtbi.services.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "${spring.kafka.topic}")
public class MessageTopicListener {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private SseService sseService;

    @KafkaHandler
    public void handleMessage(String message) {
        log.info("Évènement reçu : \n{}", message);
        try {
            // Utilise directement le message JSON pour calculer les statistiques
            String stats = statisticsService.updateAndRetrieveStatistics(message);
            System.out.println(stats);
            // Envoie les statistiques calculées à tous les clients SSE
            sseService.sendToAllClients(stats);
        } catch (Exception e) {
            log.error("Erreur lors du calcul des statistiques", e);
        }
    }
}

