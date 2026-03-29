package ru.yandex.practicum.service;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.configuration.KafkaConfig;
import ru.yandex.practicum.dal.entity.Action;
import ru.yandex.practicum.dal.entity.Scenario;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;

@Slf4j
@Service
public class SnapshotProcessor implements Runnable {

    private HubRouterControllerBlockingStub hubRouterClient;

    private final SnapshotAnalyzer snapshotAnalyzer;
    private final KafkaConfig.ConsumerConfig kafkaConfig;
    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;

    public SnapshotProcessor(KafkaConfig kafkaConfig, SnapshotAnalyzer snapshotAnalyzer,
                             @GrpcClient("hub-router") HubRouterControllerBlockingStub hubRouterClient) {
        this.snapshotAnalyzer = snapshotAnalyzer;
        this.kafkaConfig = kafkaConfig.getSnapshotConsumer();
        this.hubRouterClient = hubRouterClient;
        this.consumer = new KafkaConsumer<>(this.kafkaConfig.getProperties());
        this.hubRouterClient = hubRouterClient;

        Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
    }

    @Override
    public void run() {
        try {
            consumer.subscribe(List.of(kafkaConfig.getTopic()));

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(kafkaConfig.getPollTimeout());

                if (records.isEmpty()) {
                    continue;
                }

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    List<Scenario> scenarios = snapshotAnalyzer.process(record.value());
                    if (!scenarios.isEmpty()) {
                        for (Scenario scenario : scenarios) {
                            sendActions(scenario);
                        }
                    }
                }
                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки снапшота датчиков", e);
        } finally {

            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

    private void sendActions(Scenario scenario) {
        log.debug("Сработал сценарий [{}] для хаба [{}]", scenario.getName(), scenario.getHubId());
        Instant ts = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(ts.getEpochSecond())
                .setNanos(ts.getNano())
                .build();

        for (Map.Entry<String, Action> actionEntry : scenario.getActions().entrySet()) {
            Action scenarioAction = actionEntry.getValue();
            DeviceActionProto.Builder acctionBuilder = DeviceActionProto.newBuilder()
                    .setSensorId(actionEntry.getKey())
                    .setType(mapToActionTypeProto(scenarioAction.getType()));

            if (scenarioAction.getType().equals(ActionTypeAvro.SET_VALUE)) {
                acctionBuilder.setValue(scenarioAction.getValue());
            }

            try {
                hubRouterClient.handleDeviceAction(
                        DeviceActionRequest.newBuilder()
                                .setHubId(scenario.getHubId())
                                .setScenarioName(scenario.getName())
                                .setAction(acctionBuilder.build())
                                .setTimestamp(timestamp)
                                .build()
                );
            } catch (Exception e) {
                log.error("Ошибка при отправке действия для хаба");
            }
        }
    }

    private ActionTypeProto mapToActionTypeProto(ActionTypeAvro avro) {
        return switch (avro) {
            case ACTIVATE -> ActionTypeProto.ACTIVATE;
            case DEACTIVATE -> ActionTypeProto.DEACTIVATE;
            case INVERSE -> ActionTypeProto.INVERSE;
            case SET_VALUE -> ActionTypeProto.SET_VALUE;
        };
    }
}