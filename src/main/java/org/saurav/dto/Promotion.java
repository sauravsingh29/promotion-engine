package org.saurav.dto;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.saurav.constant.DiscountType;
import org.saurav.constant.PromoType;
import org.saurav.model.SkuPromotion;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Saurav Singh
 **/
@Data
public class Promotion implements Serializable {

    private static final long serialVersionUID = 5268370357332566623L;

    private String name;

    private Set<SkuPromotion> promotions;

    private DiscountType discountType;

    private Boolean active;

    private Double value;

    private PromoType promoType;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
