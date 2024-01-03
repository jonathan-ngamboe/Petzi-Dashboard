package ch.hearc.heg.petziHook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"ch.hearc.heg.persistence"})
public class PetziHookApplication {
	public static void main(String[] args) {
		SpringApplication.run(PetziHookApplication.class, args);
	}
}
