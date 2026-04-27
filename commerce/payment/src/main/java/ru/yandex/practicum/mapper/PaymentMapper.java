package ru.yandex.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.dal.entity.Payment;
import ru.yandex.practicum.dto.commerce.payment.CreatePaymentRequest;
import ru.yandex.practicum.dto.commerce.payment.PaymentDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface PaymentMapper {

    @Mapping(target = "feeTotal", ignore = true)
    @Mapping(target = "totalPayment", ignore = true)
    @Mapping(target = "productTotal", source = "productPrice")
    @Mapping(target = "deliveryTotal", source = "deliveryPrice")
    Payment toPayment(CreatePaymentRequest request);

    PaymentDto toPaymentDto(Payment payment);
}
