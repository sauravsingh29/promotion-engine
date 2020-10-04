package org.saurav.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Saurav Singh
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProduct implements Serializable {

    private static final long serialVersionUID = -7370153908259970911L;

    @NotNull(message = "Product sku can't be empty.")
    private String sku;

    @NotNull(message = "Product price can't be empty.")
    private Double price;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
