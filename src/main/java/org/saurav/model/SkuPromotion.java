package org.saurav.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Saurav Singh
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuPromotion implements Serializable {

    private static final long serialVersionUID = 6106298929156252051L;

    @NotNull(message = "Product SKU's can't be empty.")
    private String sku;

    @NotNull(message = "Product SKU's can't be empty.")
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuPromotion that = (SkuPromotion) o;
        return Objects.equals(sku, that.sku) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, quantity);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
