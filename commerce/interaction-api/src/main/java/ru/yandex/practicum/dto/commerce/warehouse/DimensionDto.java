package ru.yandex.practicum.dto.commerce.warehouse;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DimensionDto {

    @Min(1)
    double width;

    @Min(1)
    double height;

    @Min(1)
    double depth;
}