package org.saurav.request;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.saurav.constant.DiscountType;
import org.saurav.model.SkuPromotion;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Saurav Singh
 **/
@Data
public class AddPromotion implements Serializable {

    private static final long serialVersionUID = 3814591761626443118L;

    @NotBlank(message = "Promotion name can't be empty.")
    private String name;

    @NotNull(message = "Promotion can't be null.")
    @Valid
    private Set<SkuPromotion> promotions;

    @NotNull(message = "Promotion type can't be empty.")
    private DiscountType discountType;

    @NotNull(message = "Promotion active indicator can't be empty.")
    private Boolean active;

    @NotNull(message = "Promotional value can't be empty.")
    private Double value;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
