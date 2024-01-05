package ch.hearc.heg.rtbi;

import ch.hearc.heg.rtbi.clients.PetziHookClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.hearc.heg.rtbi.services.StatisticsService;

/**
 * Classe principale de l'application.
 */
@SpringBootApplication
public class RtbiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RtbiApplication.class, args);
    }

    /**
     * Déclenche l'envoi des données JSON déjà enregistrées dans la DB au RTBI pour l'enregistrement en cache.
     *
     * @param statisticsService le service de statistiques.
     * @param petziHookClient le client REST pour l'application PetziHook.
     * @return un CommandLineRunner qui sera exécuté au démarrage de l'application.
     */
    @Bean
    CommandLineRunner triggerDataSend(PetziHookClient petziHookClient, StatisticsService statisticsService) {
        return args -> petziHookClient.triggerDataSend(statisticsService);
    }
}