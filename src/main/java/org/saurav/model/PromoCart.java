package org.saurav.model;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * @author Saurav Singh
 **/
@Data
public class PromoCart implements Serializable {

    private static final long serialVersionUID = -6142826909037504540L;

    private String sku;

    private Integer quantity;

    private Double total;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
