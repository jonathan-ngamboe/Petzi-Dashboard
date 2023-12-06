package ch.hearc.petzi.controllers;

import ch.hearc.petzi.persistence.JsonRecord;
import ch.hearc.petzi.repositories.IJsonRecordRepository;
import ch.hearc.petzi.services.SignatureService;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class JsonRecordController {

    private static final Logger logger = LoggerFactory.getLogger(JsonRecordController.class);

    @Autowired
    private IJsonRecordRepository jsonRecordRepository;

    private SignatureService signatureService;

    private static final String petziDefaultVersion = "2";

    public JsonRecordController() {
        this.signatureService = new SignatureService();
    }


    @PostMapping("json/save")
    public ResponseEntity<String> saveJson(@RequestBody String json, HttpServletRequest request) {
        logger.info("Request received");
        String petziSignature = request.getHeader("Petzi-Signature");
        String petziVersion = request.getHeader("Petzi-Version");

        // Vérifie la version
        if (!petziDefaultVersion.equals(petziVersion)) {
            logger.info("Version not supported");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Version non prise en charge.");
        }

        // Vérifie la signature
        if (!signatureService.isSignatureValid(json, petziSignature)) {
            logger.error("Signature invalide");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature invalide.");
        }

        try {
            JsonRecord storage = new JsonRecord();
            storage.setValue(json);
            jsonRecordRepository.save(storage);
            return ResponseEntity.ok("JSON enregistré avec succès.");
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du JSON : ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement du JSON : " + e.getMessage());
        }
    }


    @GetMapping("/json/retrieve/{id}")
    public ResponseEntity<String> getJson(@PathVariable Long id) {
        try {
            JsonRecord storage = jsonRecordRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Aucune donnée trouvée avec l'id: " + id));

            return ResponseEntity.ok().body(storage.getValue());
        } catch (NoSuchElementException e) {
            logger.error("Erreur lors de la récupération du JSON : ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur lors de la récupération du JSON : " + e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : ", e);
            return ResponseEntity.internalServerError()
                    .body("Erreur interne du serveur : " + e.getMessage());
        }
    }
}
