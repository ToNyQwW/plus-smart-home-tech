package ru.yandex.practicum.kafka.serializer;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class GeneralAvroSerializer implements Serializer<SpecificRecordBase> {

    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private BinaryEncoder encoder;

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] result = null;
            encoder = encoderFactory.binaryEncoder(outputStream, encoder);

            if (encoder != null) {
                Schema schema = data.getSchema();
                DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(schema);
                writer.write(data, encoder);
                encoder.flush();
                result = outputStream.toByteArray();
            }
            return result;
        } catch (IOException exception) {
            throw new SerializationException("Ошибка сериализации данных для топика [" + topic + "]", exception);
        }
    }
}