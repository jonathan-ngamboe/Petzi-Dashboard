package ch.hearc.heg.petziHook.services;

import ch.hearc.heg.petziHook.persistence.JsonRecord;
import ch.hearc.heg.petziHook.repositories.IJsonRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service pour calculer et fournir des statistiques sur les ventes de billets.
 */
@Service
public class StatisticsService {

    @Autowired
    private IJsonRecordRepository jsonRecordRepository;

    // Utilisation de Jackson ObjectMapper pour le parsing JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Calcule et renvoie les statistiques de vente basées sur les enregistrements JSON stockés.
     *
     * @return une chaîne de caractères formatée contenant les statistiques de vente.
     * @throws IOException si une erreur se produit lors de l'analyse des données JSON.
     */
    public String calculateSalesStatistics() throws IOException {
        Iterable<JsonRecord> recordsIterable = jsonRecordRepository.findAll();
        List<JsonRecord> records = new ArrayList<>();
        recordsIterable.forEach(records::add);

        // Statistiques à calculer
        int totalTicketCount = 0;
        double totalRevenue = 0.0;
        Map<String, Integer> salesByCategory = new HashMap<>();
        Map<Integer, Integer> salesByEvent = new HashMap<>();
        Map<String, Integer> salesByTicketType = new HashMap<>();

        for (JsonRecord record : records) {
            // Désérialisation du JSON en objet Java
            Map<String, Object> root = objectMapper.readValue(record.getJson_value(), Map.class);
            Map<String, Object> details = (Map<String, Object>) root.get("details");
            Map<String, Object> ticket = (Map<String, Object>) details.get("ticket");
            Map<String, Object> price = (Map<String, Object>) ticket.get("price");

            // Calcul des statistiques de base
            totalTicketCount++;
            totalRevenue += Double.parseDouble(price.get("amount").toString());

            // Statistiques par catégorie de ticket
            String category = ticket.get("category").toString();
            salesByCategory.put(category, salesByCategory.getOrDefault(category, 0) + 1);

            // Statistiques par événement
            Integer eventId = (Integer) ticket.get("eventId");
            salesByEvent.put(eventId, salesByEvent.getOrDefault(eventId, 0) + 1);

            // Statistiques par type de ticket
            String ticketType = ticket.get("type").toString();
            salesByTicketType.put(ticketType, salesByTicketType.getOrDefault(ticketType, 0) + 1);
        }

        // Construction du message de statistiques
        StringBuilder statsMessage = new StringBuilder();
        statsMessage.append("Statistiques des ventes :\n");
        statsMessage.append("Total des ventes de billets : ").append(totalTicketCount).append("\n");
        statsMessage.append("Revenus totaux : ").append(String.format("%.2f CHF", totalRevenue)).append("\n");

        statsMessage.append("Ventes par catégorie : ").append("\n");
        salesByCategory.forEach((category, count) ->
                statsMessage.append(category).append(" : ").append(count).append("\n"));

        statsMessage.append("Ventes par événement : ").append("\n");
        salesByEvent.forEach((event, count) ->
                statsMessage.append("Événement ID ").append(event).append(" : ").append(count).append("\n"));

        statsMessage.append("Ventes par type de ticket : ").append("\n");
        salesByTicketType.forEach((type, count) ->
                statsMessage.append(type).append(" : ").append(count).append("\n"));

        return statsMessage.toString();
    }

    /**
     * Récupère les statistiques de vente actuelles.
     *
     * @return une chaîne de caractères représentant les statistiques de vente ou un message d'erreur si le calcul échoue.
     */
    public String getStats() {
        try {
            return calculateSalesStatistics();
        } catch (IOException e) {
            return "Erreur lors du calcul des statistiques : " + e.getMessage();
        }
    }
}
