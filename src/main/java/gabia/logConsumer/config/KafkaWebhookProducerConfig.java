package gabia.logConsumer.config;

import gabia.logConsumer.dto.WebhookDTO;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaWebhookProducerConfig {

    //    private String bootstrapServer = "http://182.162.142.151:9093";
    private String bootstrapServer = "http://127.0.0.1:9092";

    @Bean
    public ProducerFactory<String, WebhookDTO.Request> producerFactory() {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, WebhookDTO.Request> kafkaTemplate() {

        return new KafkaTemplate<>(producerFactory());
    }
}
