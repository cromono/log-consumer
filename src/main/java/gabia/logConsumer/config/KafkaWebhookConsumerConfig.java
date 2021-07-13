package gabia.logConsumer.config;

import gabia.logConsumer.dto.WebhookDTO;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaWebhookConsumerConfig {

    //    private String bootstrapServer = "http://182.162.142.151:9093";
    private String bootstrapServer = "http://127.0.0.1:9092";

    @Bean
    public ConsumerFactory<String, WebhookDTO.Request> kafkaWebhookConsumer() {

        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        return new DefaultKafkaConsumerFactory<>(
            configs,
            new StringDeserializer(),
            new JsonDeserializer<>(WebhookDTO.Request.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WebhookDTO.Request> webhookListener() {

        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(kafkaWebhookConsumer());
        factory.setErrorHandler(new SeekToCurrentErrorHandler());
        return factory;
    }

}
