package ch.hearc.petzi.controllers;

import ch.hearc.petzi.persistence.JsonRecord;
import ch.hearc.petzi.repositories.IJsonRecordRepository;
import ch.hearc.petzi.services.SignatureService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

public class JsonRecordController {
    @Autowired
    private IJsonRecordRepository jsonRecordRepository;
    private SignatureService signatureService;

    private static final String petziDefaultVersion = "2";

    @PostMapping("json/save")
    public ResponseEntity<String> saveJson(@RequestBody String json, HttpServletRequest request) {
        System.out.println("Request received");
        String petziSignature = request.getHeader("Petzi-Signature");
        String petziVersion = request.getHeader("Petzi-Version");

        // Vérifie la version
        if (!petziDefaultVersion.equals(petziVersion)) {
            System.out.println("Version not supported");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Version non prise en charge.");
        }

        try {
            // Vérifie la signature
            if (!signatureService.isSignatureValid(json, petziSignature)) {
                // Enregistre l'erreur dans un log et retourne une réponse 200 OK
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature invalide.");
            }
            System.out.println("Signature valid");

            try {
                JsonRecord storage = new JsonRecord();
                storage.setValue(json);
                jsonRecordRepository.save(storage);
                return ResponseEntity.ok("JSON enregistré avec succès.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur lors de l'enregistrement du JSON : " + e.getMessage());
            }
        } catch (Exception e) {
            // Enregistre l'erreur dans un log et retourne une réponse 200 OK
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement du JSON : " + e.getMessage());
        }
    }

    @GetMapping("/json/retrieve/{id}")
    public ResponseEntity<String> getJson(@PathVariable Long id) {
        try {
            JsonRecord storage = jsonRecordRepository.findById(id)
                    .orElseThrow(() -> new Exception("Aucune donnée trouvée avec l'id: " + id));

            return ResponseEntity.ok()
                    .body(storage.getValue());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erreur lors de la récupération du JSON : " + e.getMessage());
        }
    }
}
