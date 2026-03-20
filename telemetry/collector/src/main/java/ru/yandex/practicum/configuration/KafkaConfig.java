package ru.yandex.practicum.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@ToString
@ConfigurationProperties("collector.kafka")
public class KafkaConfig {

    private ProducerConfig producer;

    @Getter
    @Setter
    @ToString
    public static class ProducerConfig {

        private Properties properties;
        private Map<String, String> topics;
    }
}
