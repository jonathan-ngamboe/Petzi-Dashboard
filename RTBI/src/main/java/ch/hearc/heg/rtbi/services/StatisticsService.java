package ch.hearc.heg.rtbi.services;

import ch.hearc.heg.petziHook.controllers.JsonRecordController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatisticsService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ConcurrentHashMap<String, Double> dailyRevenue = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> dailySalesCount = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Double> categoryRevenue = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> categorySalesCount = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Double> customerSpending = new ConcurrentHashMap<>();

    /**
     * Met à jour les statistiques et retourne un message formaté.
     *
     * @param jsonValue le message JSON reçu.
     * @return un message formaté contenant les statistiques.
     * @throws IOException si une erreur survient lors de la lecture du JSON.
     */
    public String updateAndRetrieveStatistics(String jsonValue) throws IOException {
        Map<String, Object> root = objectMapper.readValue(jsonValue, Map.class);
        Map<String, Object> details = (Map<String, Object>) root.get("details");
        Map<String, Object> ticket = (Map<String, Object>) details.get("ticket");
        Map<String, Object> price = (Map<String, Object>) ticket.get("price");
        Map<String, Object> buyer = (Map<String, Object>) details.get("buyer");

        String createdAt = root.get("createdAt").toString();
        double amount = Double.parseDouble(price.get("amount").toString());
        String category = ticket.get("category").toString();
        String customerName = buyer.get("firstName") + " " + buyer.get("lastName");

        // Mise à jour des statistiques quotidiennes
        dailyRevenue.compute(createdAt, (date, revenue) -> revenue == null ? amount : revenue + amount);
        dailySalesCount.compute(createdAt, (date, count) -> count == null ? 1 : count + 1);

        // Mise à jour des statistiques par catégorie
        categoryRevenue.compute(category, (cat, revenue) -> revenue == null ? amount : revenue + amount);
        categorySalesCount.compute(category, (cat, count) -> count == null ? 1 : count + 1);

        // Mise à jour des dépenses par client
        customerSpending.compute(customerName, (name, total) -> total == null ? amount : total + amount);

        return formatStatistics();
    }

    private String formatStatistics() {
        StringBuilder statsMessage = new StringBuilder("-- Statistiques des ventes --\n");

        statsMessage.append("Ventes et revenus quotidiens :\n");
        dailySalesCount.forEach((date, count) ->
                statsMessage.append(date).append(" : ").append(count)
                        .append(" ventes, Total: ").append(dailyRevenue.get(date)).append(" CHF\n"));

        statsMessage.append("Ventes et revenus par catégorie :\n");
        categorySalesCount.forEach((cat, count) ->
                statsMessage.append(cat).append(" : ").append(count)
                        .append(" ventes, Total: ").append(categoryRevenue.get(cat)).append(" CHF\n"));

        statsMessage.append("Dépenses totales par client :\n");
        customerSpending.forEach((name, total) ->
                statsMessage.append(name).append(" : ").append(total).append(" CHF\n"));

        return statsMessage.toString();
    }
}
