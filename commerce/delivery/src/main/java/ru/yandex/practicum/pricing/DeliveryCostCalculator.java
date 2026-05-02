package ru.yandex.practicum.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.WarehouseAddress;
import ru.yandex.practicum.pricing.configuration.DeliveryCostProperties;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DeliveryCostCalculator {

    private final DeliveryCostProperties properties;

    public BigDecimal calculateDeliveryCost(String fromStreet,
                                            String toStreet,
                                            double deliveryWeight,
                                            double deliveryVolume,
                                            boolean isFragile) {

        BigDecimal cost = properties.getBaseCost();

        if (fromStreet.equals(WarehouseAddress.ADDRESS_1.toString())) {
            cost = cost.add(properties.getBaseCost());
        } else if (fromStreet.equals(WarehouseAddress.ADDRESS_2.toString())) {
            cost = cost.add(properties.getBaseCost().multiply(BigDecimal.valueOf(2)));
        }

        if (isFragile) {
            cost = cost.multiply(properties.getFragileMultiplier());
        }

        cost = cost.add(BigDecimal.valueOf(deliveryWeight).multiply(properties.getWeightRate()));
        cost = cost.add(BigDecimal.valueOf(deliveryVolume).multiply(properties.getVolumeRate()));

        if (!toStreet.equals(fromStreet)) {
            cost = cost.multiply(properties.getDistanceMultiplier());
        }

        return cost;
    }
}
