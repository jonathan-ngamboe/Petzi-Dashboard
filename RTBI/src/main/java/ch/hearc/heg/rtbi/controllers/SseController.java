package ch.hearc.heg.rtbi.controllers;

import ch.hearc.heg.rtbi.services.SseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * Controller pour gérer les connexions Server-Sent Events (SSE).
 * Permet d'établir des connexions avec les clients et de maintenir une liste de ces connexions.
 */
@RestController
public class SseController {

    private static final Logger logger = LoggerFactory.getLogger(SseController.class);

    @Autowired
    private SseService sseService;

    /**
     * Endpoint pour établir une nouvelle connexion SSE.
     * Lorsqu'un client fait une requête GET à cette URL, une nouvelle connexion SSE est établie.
     *
     * @return SseEmitter pour la nouvelle connexion client.
     */
    @GetMapping("/sse")
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter();

        try {
            // Ajoute le nouvel émetteur à l'ensemble des clients.
            sseService.addClient(emitter);

            // Envoie une confirmation d'abonnement
            emitter.send("Abonnement aux notifications SSE réussi.");

            // Gestionnaire pour la complétion de la connexion. Retire l'émetteur de l'ensemble des clients.
            emitter.onCompletion(() -> {
                sseService.removeClient(emitter);
                logger.info("Connexion SSE complétée");
            });

            // Gestionnaire pour les timeouts de la connexion. Retire également l'émetteur.
            emitter.onTimeout(() -> {
                sseService.removeClient(emitter);
                logger.info("Connexion SSE expirée");
            });

            // Gestionnaire pour les erreurs. Retire l'émetteur en cas d'erreur.
            emitter.onError((e) -> {
                sseService.removeClient(emitter);
                logger.error("Erreur de connexion SSE", e);
            });
        } catch (Exception e) {
            logger.error("Erreur lors de l'établissement d'une connexion SSE", e);
            // En cas d'erreur lors de l'ajout du client, on informe le client de l'erreur.
            try {
                emitter.send("Erreur lors de l'abonnement aux notifications SSE.");
                emitter.completeWithError(e);
            } catch (IOException ioException) {
                logger.error("Erreur lors de l'envoi du message d'erreur SSE", ioException);
            }
        }

        // Retourne l'émetteur au client pour maintenir la connexion ouverte.
        return emitter;
    }
}
