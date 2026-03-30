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
@ConfigurationProperties("kafka.consumers")
public class KafkaConfig {

    private ConsumerConfig snapshotConsumer;
    private ConsumerConfig hubEventConsumer;

    @Getter
    @Setter
    @ToString
    public static class ConsumerConfig {

        private String topic;
        private Duration pollTimeout;
        private Properties properties;
    }
}