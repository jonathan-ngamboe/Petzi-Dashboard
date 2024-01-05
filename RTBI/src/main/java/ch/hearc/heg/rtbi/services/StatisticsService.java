package ch.hearc.heg.rtbi.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashMap;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatisticsService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private ConcurrentHashMap<String, Double> dailyRevenue = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> dailySalesCount = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Double> eventRevenue = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> eventSalesCount = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Map<String, Object>> customerInfo = new ConcurrentHashMap<>();

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
        String customerName = buyer.get("firstName") + " " + buyer.get("lastName");
        String event = ticket.get("event").toString();
        String postcode = buyer.get("postcode").toString();

        // Mise à jour des statistiques quotidiennes
        dailyRevenue.compute(createdAt, (date, revenue) -> revenue == null ? amount : revenue + amount);
        dailySalesCount.compute(createdAt, (date, count) -> count == null ? 1 : count + 1);

        // Mise à jour des statistiques par événement
        eventRevenue.compute(event, (evt, revenue) -> revenue == null ? amount : revenue + amount);
        eventSalesCount.compute(event, (evt, count) -> count == null ? 1 : count + 1);

        // Mise à jour des dépenses par client
        customerInfo.compute(customerName, (name, info) -> {
            if (info == null) {
                info = new HashMap<>();
                info.put("totalSpending", amount);
                info.put("postcode", postcode);
            } else {
                double totalSpending = (double) info.get("totalSpending") + amount;
                info.put("totalSpending", totalSpending);
            }
            return info;
        });

        return formatStatisticsAsJson();
    }

    private String formatStatisticsAsJson() throws JsonProcessingException {
        Map<String, Object> stats = new HashMap<>();
        stats.put("dailySales", dailySalesCount);
        stats.put("dailyRevenue", dailyRevenue);
        stats.put("eventRevenue", eventRevenue);
        stats.put("eventSalesCount", eventSalesCount);
        stats.put("customers", customerInfo);

        return objectMapper.writeValueAsString(stats);
    }
}
