package ru.yandex.practicum.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

@Getter
@Setter
@ToString
@ConfigurationProperties("aggregator.kafka")
public class KafkaConfig {

    private ProducerConfig producer;
    private ConsumerConfig consumer;

    @Getter
    @Setter
    @ToString
    public static class ProducerConfig {

        private String topic;
        private Properties properties;
    }

    @Getter
    @Setter
    @ToString
    public static class ConsumerConfig {

        private String topic;
        private Duration pollTimeout;
        private Properties properties;
    }
}