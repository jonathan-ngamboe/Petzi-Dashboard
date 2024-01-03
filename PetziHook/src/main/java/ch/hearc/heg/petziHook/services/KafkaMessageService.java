package ch.hearc.heg.petziHook.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageService implements IMessageService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${spring.kafka.topic}")
    private String topicName;

    @Override
    public void sendMessage(String message) {
        kafkaTemplate.send(topicName, message);
        log.info("Évènement envoyé : \n{}", message);
    }
}
