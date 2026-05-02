package ru.yandex.practicum.pricing.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties("delivery-cost-properties")
public class DeliveryCostProperties {

    private BigDecimal baseCost;
    private BigDecimal weightRate;
    private BigDecimal volumeRate;
    private BigDecimal fragileMultiplier;
    private BigDecimal distanceMultiplier;
}
