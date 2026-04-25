package ru.yandex.practicum.pricing;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.model.WarehouseAddress;

import java.math.BigDecimal;

@Component
public class DeliveryCostCalculator {

    private static final BigDecimal BASE_COST = BigDecimal.valueOf(5);
    private static final BigDecimal FRAGILE_MULTIPLIER = BigDecimal.valueOf(1.2);
    private static final BigDecimal DISTANCE_MULTIPLIER = BigDecimal.valueOf(1.2);

    private static final BigDecimal WEIGHT_RATE = BigDecimal.valueOf(0.3);
    private static final BigDecimal VOLUME_RATE = BigDecimal.valueOf(0.2);

    public BigDecimal calculateDeliveryCost(String fromStreet,
                                            String toStreet,
                                            double deliveryWeight,
                                            double deliveryVolume,
                                            boolean isFragile) {

        BigDecimal cost = BASE_COST;

        if (fromStreet.equals(WarehouseAddress.ADDRESS_1.toString())) {
            cost = cost.add(BASE_COST);
        } else if (fromStreet.equals(WarehouseAddress.ADDRESS_2.toString())) {
            cost = cost.add(BASE_COST.multiply(BigDecimal.valueOf(2)));
        }

        if (isFragile) {
            cost = cost.multiply(FRAGILE_MULTIPLIER);
        }

        cost = cost.add(BigDecimal.valueOf(deliveryWeight).multiply(WEIGHT_RATE));
        cost = cost.add(BigDecimal.valueOf(deliveryVolume).multiply(VOLUME_RATE));

        if (!toStreet.equals(fromStreet)) {
            cost = cost.multiply(DISTANCE_MULTIPLIER);
        }

        return cost;
    }
}
