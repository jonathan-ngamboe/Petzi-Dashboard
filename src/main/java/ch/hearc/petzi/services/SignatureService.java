package ch.hearc.petzi.services;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignatureService {
    private static final Logger logger = LoggerFactory.getLogger(SignatureService.class);
    private static final String SECRET_KEY = "secret";

    public boolean isSignatureValid(String payload, String receivedSignatureHeader) {
        logger.info("Validation de la signature...");
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
            logger.error("Erreur lors de la validation de la signature : " + e.getMessage());
            return false;
        }
    }

    private byte[] calculateHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
