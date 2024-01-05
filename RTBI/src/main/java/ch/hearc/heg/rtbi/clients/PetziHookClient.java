package ch.hearc.heg.rtbi.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ch.hearc.heg.rtbi.services.StatisticsService;

import java.util.List;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Client REST pour l'application PetziHook.
 */
@Service
public class PetziHookClient {

    private final RestTemplate restTemplate;
    private final String petziHookUrl;
    private final ObjectMapper objectMapper;


    /**
     * Constructeur.
     *
     * @param restTemplateBuilder le builder pour créer un RestTemplate.
     * @param petziHookUrl l'URL de l'application PetziHook récupérée depuis le fichier de configuration application.properties.
     */
    public PetziHookClient(RestTemplateBuilder restTemplateBuilder,
                           @Value("${petzihook.url}") String petziHookUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.petziHookUrl = petziHookUrl;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Déclenche l'envoi des données JSON déjà enregistrées dans la DB au RTBI pour l'enregistrement en cache.
     *
     * @param statisticsService le service de statistiques.
     */
    public void triggerDataSend(StatisticsService statisticsService) {
        String jsonResponse = restTemplate.getForObject(petziHookUrl + "/petzihook/json/get/all", String.class);
        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            try {
                // Convertit la réponse en une liste de chaînes JSON
                List<String> records = objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
                // Traite chaque chaîne JSON individuellement
                for (String jsonValue : records) {
                    try {
                        // Effectue la mise à jour des statistiques pour chaque enregistrement JSON
                        statisticsService.updateAndRetrieveStatistics(jsonValue);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
