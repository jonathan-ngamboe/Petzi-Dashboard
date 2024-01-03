package ch.hearc.heg.petziHook.repositories;

import ch.hearc.heg.common.persistence.JsonRecord;
import org.springframework.data.repository.CrudRepository;

public interface IJsonRecordRepository extends CrudRepository<JsonRecord, Long> {
    // Pas besoin d'ajouter d'autres méthodes
}
