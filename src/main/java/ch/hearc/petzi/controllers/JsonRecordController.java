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

    private final SignatureService signatureService;

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
            return new ResponseEntity<>("Version non prise en charge.", HttpStatus.BAD_REQUEST);
        }

        // Vérifie la signature
        if (!signatureService.isSignatureValid(json, petziSignature)) {
            logger.error("Signature invalide");
            return new ResponseEntity<>("Signature invalide.", HttpStatus.BAD_REQUEST);
        }

        try {
            JsonRecord storage = new JsonRecord();
            storage.setValue(json);
            jsonRecordRepository.save(storage);
            return new ResponseEntity<>("JSON enregistré avec succès !", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement du JSON : ", e);
            return new ResponseEntity<>("Erreur lors de l'enregistrement du JSON : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/json/retrieve/{id}")
    public ResponseEntity<String> getJson(@PathVariable Long id) {
        try {
            JsonRecord storage = jsonRecordRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Aucune donnée trouvée avec l'id: " + id));

            return new ResponseEntity<>(storage.getValue(), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            logger.error("Erreur lors de la récupération du JSON : ", e);
            return new ResponseEntity<>("Erreur lors de la récupération du JSON : " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur interne du serveur : ", e);
            return new ResponseEntity<>("Erreur interne du serveur : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
