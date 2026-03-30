package ru.yandex.practicum.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class SensorNotFoundException extends StatusRuntimeException {

    public SensorNotFoundException(String message) {
        super(Status.NOT_FOUND.withDescription(message));
    }
}