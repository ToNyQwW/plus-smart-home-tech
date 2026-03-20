package ru.yandex.practicum.model.sensorEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class MotionSensorEvent extends SensorEvent {

    private int link_quality;

    private boolean motion;

    private int voltage;

    @Override
    public SensorEventType getSensorEventType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}