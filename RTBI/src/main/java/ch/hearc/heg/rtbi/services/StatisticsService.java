package ch.hearc.heg.rtbi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service pour calculer et fournir des statistiques sur les ventes de billets.
 */
@Service
public class StatisticsService {
    // Utilisation de Jackson ObjectMapper pour le parsing JSON
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Calcule et renvoie les statistiques de vente basées sur les enregistrements JSON stockés.
     *
     * @param jsonValue le contenu JSON nécessaire pour calculer les statistiques.
     * @return une chaîne de caractères formatée contenant les statistiques de vente.
     * @throws IOException si une erreur se produit lors de l'analyse des données JSON.
     */
    public String calculateSalesStatistics(String jsonValue) throws IOException {
        // Initialisation des statistiques
        int totalTicketCount = 0;
        double totalRevenue = 0.0;
        Map<String, Integer> salesByCategory = new HashMap<>();
        Map<Integer, Integer> salesByEvent = new HashMap<>();
        Map<String, Integer> salesByTicketType = new HashMap<>();

        // Désérialisation du JSON en objet Java
        Map<String, Object> root = objectMapper.readValue(jsonValue, Map.class);
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

        // Construction du message de statistiques
        StringBuilder statsMessage = new StringBuilder();
        statsMessage.append("-- Statistiques des ventes --\n");
        statsMessage.append("Total des ventes de billets : ").append(totalTicketCount).append("\n");
        statsMessage.append("Revenus totaux : ").append(String.format("%.2f CHF", totalRevenue)).append("\n");

        statsMessage.append("Ventes par catégorie : ").append("\n");
        salesByCategory.forEach((cat, count) -> // 'cat' au lieu de 'category'
                statsMessage.append(cat).append(" : ").append(count).append("\n"));

        statsMessage.append("Ventes par événement : ").append("\n");
        salesByEvent.forEach((event, count) ->
                statsMessage.append("Événement ID ").append(event).append(" : ").append(count).append("\n"));

        statsMessage.append("Ventes par type de ticket : ").append("\n");
        salesByTicketType.forEach((type, count) ->
                statsMessage.append(type).append(" : ").append(count).append("\n"));

        return statsMessage.toString();
    }
}
