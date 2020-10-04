package org.saurav.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Arrays.stream;

/**
 * @author Saurav Singh
 **/
public enum DiscountType {
    AMOUNT("amount"), PERCENTAGE("percentage");

    private final String value;


    DiscountType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static DiscountType fromValue(String value) {
        final Optional<DiscountType> optionalPromotionType = stream(DiscountType.values()).filter(p -> p.value.equalsIgnoreCase(value))
                .findFirst();
        if (optionalPromotionType.isPresent()) {
            return optionalPromotionType.get();
        }
        throw new IllegalArgumentException(
                "Invalid  promotion type " + value + ", Allowed promotion types are " + Arrays.toString(values()));
    }

    public String getValue() {
        return value;
    }
}
