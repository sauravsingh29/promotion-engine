package org.saurav.response;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.saurav.model.PromoCart;

import java.util.List;

/**
 * @author Saurav Singh
 **/
@Data
public class ProcessedCart {

    private List<PromoCart> cart;

    private Double total;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
