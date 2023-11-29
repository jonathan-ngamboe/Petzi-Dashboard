package ch.hearc.petzi.repository;

import ch.hearc.petzi.model.JsonStorage;
import org.springframework.data.repository.CrudRepository;

public interface JsonStorageRepository extends CrudRepository<JsonStorage, Long> {
    // Pas besoin d'ajouter d'autres méthodes
}
