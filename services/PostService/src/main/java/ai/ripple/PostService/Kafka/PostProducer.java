package ai.ripple.PostService.Kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PostProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, Object message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Sent message to Kafka topic " + topic + ": " + message);
    }
}
