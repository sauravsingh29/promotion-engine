package org.saurav.request;

import lombok.Data;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Saurav Singh
 **/
@Data
public class ProcessCart implements Serializable {

    private static final long serialVersionUID = 5626954265141386961L;

    @NotNull(message = "Cart can't be empty.")
    private Set<Item> item;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
