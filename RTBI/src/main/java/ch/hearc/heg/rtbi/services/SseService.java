package ch.hearc.heg.rtbi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Service pour gérer les clients Server-Sent Events (SSE).
 * Permet d'ajouter, de supprimer des clients et d'envoyer des événements à tous les clients connectés.
 */
@Service
public class SseService {

    private static final Logger logger = LoggerFactory.getLogger(SseService.class);

    // Ensemble des clients connectés via SSE.
    private final Set<SseEmitter> clients = Collections.synchronizedSet(new HashSet<>());
    @Autowired
    private StatisticsService statisticsService;

    /**
     * Ajoute un nouveau client SSE à l'ensemble des clients connectés.
     *
     * @param emitter L'émetteur SSE représentant le client.
     */
    public void addClient(SseEmitter emitter) {
        clients.add(emitter);
        logger.info("Nouveau client SSE ajouté.");
        // Envois les données du cache au nouveau client
        try {
            String currentStats = statisticsService.getCurrentStatistics();
            if (currentStats != null && !currentStats.isEmpty()) {
                emitter.send(currentStats);
                System.out.println("Statistiques du cache envoyées au nouveau client SSE");
            }
        } catch (JsonProcessingException e) {
            logger.error("Erreur lors de l'envoi des statistiques actuelles", e);
        } catch (IOException e) {
            logger.error("Erreur lors de l'envoi des statistiques actuelles", e);
        }
    }

    /**
     * Supprime un client SSE de l'ensemble des clients connectés.
     *
     * @param emitter L'émetteur SSE à supprimer.
     */
    public void removeClient(SseEmitter emitter) {
        clients.remove(emitter);
        logger.info("Client SSE supprimé.");
    }

    /**
     * Envoie un événement SSE à tous les clients connectés.
     *
     * @param data Les données à envoyer aux clients.
     */
    public void sendToAllClients(Object data) {
        if(clients.isEmpty()) {
            logger.info("Aucun client SSE connecté.");
            logger.info("Statistiques non envoyées.");
            return;
        }
        synchronized (clients) {
            Set<SseEmitter> clientsToRemove = new HashSet<>();
            for (SseEmitter client : clients) {
                try {
                    // Envoi des données au client.
                    client.send(data);
                    logger.info("Statistiques envoyées à tous les clients SSE");
                } catch (IOException e) {
                    // En cas d'erreur, marque l'émetteur comme complété avec une erreur.
                    logger.error("Erreur lors de l'envoi des statistiques", e);
                    logger.info("Suppression du client SSE");
                    client.completeWithError(e);
                    clientsToRemove.add(client);
                }
            }
            // Supprime les clients qui ne peuvent plus recevoir d'événements.
            clientsToRemove.forEach(this::removeClient);
        }
    }
}
