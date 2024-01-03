package ch.hearc.heg.petziHook.controllers;

import ch.hearc.heg.petziHook.persistence.JsonRecord;
import ch.hearc.heg.petziHook.repositories.IJsonRecordRepository;
import ch.hearc.heg.petziHook.services.SignatureService;

import ch.hearc.heg.petziHook.services.SseService;
import ch.hearc.heg.petziHook.services.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

/**
 * Contrôleur REST pour gérer les requêtes liées aux enregistrements JSON (Tickets).
 */
@RestController
public class JsonRecordController {

    @Autowired
    private IJsonRecordRepository jsonRecordRepository;
    @Autowired
    private SseService sseService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private SignatureService signatureService;
    private static final String petziDefaultVersion = "2";
    private static final Logger logger = LoggerFactory.getLogger(JsonRecordController.class);


    /**
     * Traite les requêtes POST pour enregistrer des données JSON et envoyer des notifications SSE.
     *
     * @param json le contenu JSON à enregistrer.
     * @param request la requête HTTP contenant les en-têtes et autres informations.
     * @return un ResponseEntity représentant le résultat de l'opération.
     */
    @PostMapping("json/save")
    public ResponseEntity<String> saveJson(@RequestBody String json, HttpServletRequest request) {
        logger.info("Requête reçue");
        String petziSignature = request.getHeader("Petzi-Signature");
        String petziVersion = request.getHeader("Petzi-Version");

        // Vérifie la version
        if (!petziDefaultVersion.equals(petziVersion)) {
            logger.info("Version non prise en charge");
            return new ResponseEntity<>("Version non prise en charge.", HttpStatus.BAD_REQUEST);
        }

        // Vérifie la signature
        if (!signatureService.isSignatureValid(json, petziSignature)) {
            logger.error("Signature invalide");
            return new ResponseEntity<>("Signature invalide.", HttpStatus.BAD_REQUEST);
        }

        // Enregistre le JSON
        try {
            JsonRecord storage = new JsonRecord();
            storage.setJson_value(json);
            jsonRecordRepository.save(storage);

            // Récupère les statistiques sur les tickets
            String stats = statisticsService.getStats();

            // Envoie une notification SSE aux clients qui écoutent
            sseService.sendToAllClients(stats);
            System.out.println(stats);

            // Retourne une réponse HTTP 200
            return new ResponseEntity<>("JSON enregistré avec succès !", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du JSON et de l'envoi de la notification SSE : ", e);
            return new ResponseEntity<>("Erreur lors de l'enregistrement du JSON et de l'envoi de la notification SSE : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Traite les requêtes GET pour récupérer des données JSON enregistrées par leur identifiant.
     *
     * @param id l'identifiant de l'enregistrement JSON à récupérer.
     * @return un ResponseEntity contenant le JSON ou un message d'erreur.
     */
    @GetMapping("/json/get/{id}")
    public ResponseEntity<String> getJson(@PathVariable Long id) {
        try {
            JsonRecord storage = jsonRecordRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Aucune donnée trouvée avec l'id: " + id));

            return new ResponseEntity<>(storage.getJson_value(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Erreur lors de la récupération du JSON : ", e);
            return new ResponseEntity<>("Erreur lors de la récupération du JSON : " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : ", e);
            return new ResponseEntity<>("Erreur interne du serveur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
