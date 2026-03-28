package ru.yandex.practicum.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class DeviceAlreadyExistsException extends StatusRuntimeException {
    public DeviceAlreadyExistsException(String message) {
        super(Status.ALREADY_EXISTS.withDescription(message));
    }
}