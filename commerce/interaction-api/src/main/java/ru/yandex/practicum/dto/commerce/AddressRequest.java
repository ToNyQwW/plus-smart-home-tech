package ru.yandex.practicum.dto.commerce;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressRequest {

    @NotBlank
    String country;

    @NotBlank
    String city;

    @NotBlank
    String street;

    @NotBlank
    String house;

    @NotBlank
    String flat;
}
