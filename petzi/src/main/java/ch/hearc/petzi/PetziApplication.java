package ch.hearc.petzi;

import ch.hearc.petzi.model.JsonStorage;
import ch.hearc.petzi.repository.JsonStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@SpringBootApplication
@RestController
public class PetziApplication {

	@Autowired
	private JsonStorageRepository jsonStorageRepository;
	private static final String SECRET_KEY = "secret";
	private static final String petziDefaultVersion = "2";


	public static void main(String[] args) {
		SpringApplication.run(PetziApplication.class, args);
	}


	private boolean isSignatureValid(String payload, String receivedSignatureHeader) {
		System.out.println("Validating signature");
		try {
			String[] parts = receivedSignatureHeader.split(",");
			long timestamp = Long.parseLong(parts[0].split("=")[1]);
			String receivedSignature = parts[1].split("=")[1];

			// Vérifie que le timestamp est dans une fenêtre de temps acceptable
			long currentTime = System.currentTimeMillis() / 1000;
			if (Math.abs(currentTime - timestamp) > 30) {
				return false;
			}

			String signedPayload = timestamp + "." + payload;
			byte[] expectedSignature = calculateHMAC(signedPayload, SECRET_KEY);

			// Conversion de la signature hexadécimale reçue en tableau d'octets
			byte[] receivedSignatureBytes = HexFormat.of().parseHex(receivedSignature);

			// Compare en utilisant une fonction de comparaison en temps constant
			return MessageDigest.isEqual(expectedSignature, receivedSignatureBytes);
		} catch (Exception e) {
			System.out.println("Error while validating signature : " + e.getMessage());
			return false;
		}
	}

	private byte[] calculateHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKey);
		return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
	}

	@PostMapping("/store")
	public ResponseEntity<String> saveJson(@RequestBody String json, HttpServletRequest request) {
		System.out.println("Request received");
		String petziSignature = request.getHeader("Petzi-Signature");
		String petziVersion = request.getHeader("Petzi-Version");

		// Vérifie la version
		if (!petziDefaultVersion.equals(petziVersion)) {
			System.out.println("Version not supported");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Version non prise en charge.");
		}

		try {
			// Vérifie la signature
			if (!isSignatureValid(json, petziSignature)) {
				// Enregistre l'erreur dans un log et retourne une réponse 200 OK
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature invalide.");
			}
			System.out.println("Signature valid");

			try {
				JsonStorage storage = new JsonStorage();
				storage.setValue(json);
				jsonStorageRepository.save(storage);
				return ResponseEntity.ok("JSON enregistré avec succès.");
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Erreur lors de l'enregistrement du JSON : " + e.getMessage());
			}
		} catch (Exception e) {
			// Enregistre l'erreur dans un log et retourne une réponse 200 OK
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erreur lors de l'enregistrement du JSON : " + e.getMessage());
		}
	}

	@GetMapping("/retrieve/{id}")
	public ResponseEntity<String> getJson(@PathVariable Long id) {
		try {
			JsonStorage storage = jsonStorageRepository.findById(id)
					.orElseThrow(() -> new Exception("Aucune donnée trouvée avec l'id: " + id));

			return ResponseEntity.ok()
					.body(storage.getValue());
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("Erreur lors de la récupération du JSON : " + e.getMessage());
		}
	}
}
