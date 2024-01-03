package ch.hearc.petzi.repositories;

import ch.hearc.petzi.persistence.JsonRecord;
import org.springframework.data.repository.CrudRepository;

public interface IJsonRecordRepository extends CrudRepository<JsonRecord, Long> {
    // Pas besoin d'ajouter d'autres méthodes
}
