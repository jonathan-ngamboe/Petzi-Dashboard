package ch.hearc.petzi;

import ch.hearc.petzi.model.Ticket;
import ch.hearc.petzi.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@RestController
public class PetziApplication {

	@Autowired
	private TicketRepository ticketRepository;

	public static void main(String[] args) {
		SpringApplication.run(PetziApplication.class, args);
	}

	@PostMapping("/saveTicket")
	public ResponseEntity<String> saveTicket(@RequestBody Ticket ticket, HttpServletRequest request) {
		String headerValue = request.getHeader("Header");
		try {
			// Enregistre le ticket dans la base de données
			ticketRepository.save(ticket);
			return ResponseEntity.ok()
					.header("Header", headerValue)
					.body("Ticket enregistré");
		} catch (Exception e) {
			// Gestion de l'erreur d'enregistrement
			return ResponseEntity.internalServerError()
					.body("Erreur lors de l'enregistrement du ticket : " + e.getMessage());
		}
	}
}
