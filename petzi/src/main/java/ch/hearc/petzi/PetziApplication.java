package ch.hearc.petzi;

import ch.hearc.petzi.model.JsonStorage;
import ch.hearc.petzi.repository.JsonStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class PetziApplication {

	@Autowired
	private JsonStorageRepository jsonStorageRepository;

	public static void main(String[] args) {
		SpringApplication.run(PetziApplication.class, args);
	}

	@PostMapping("/store/{key}")
	public ResponseEntity<String> saveJson(@PathVariable String key, @RequestBody String json, HttpServletRequest request) {
		String headerValue = request.getHeader("Header");
		try {
			JsonStorage storage = new JsonStorage();
			storage.setKey(key);
			storage.setValue(json);
			jsonStorageRepository.save(storage);
			return ResponseEntity.ok()
					.header("Header", headerValue)
					.body("JSON enregistré avec la clé : " + key);
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("Erreur lors de l'enregistrement du JSON : " + e.getMessage());
		}
	}
}
