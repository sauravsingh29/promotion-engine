package org.saurav.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Saurav Singh
 **/
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError implements Serializable {

    private static final long serialVersionUID = 6842551810595779920L;

    private String source;

    private String message;

    @JsonProperty("detailed_message")
    private String detailedError;

    private HttpStatus status;

    public ApiError(HttpStatus status, String message) {
        this.message = message;
        this.status = status;
    }

    public ApiError(HttpStatus status, String message, String detailedError) {
        this.message = message;
        this.status = status;
        this.detailedError = detailedError;
    }

    public ApiError(String source, String message, String detailedError, HttpStatus status) {
        this.source = source;
        this.message = message;
        this.detailedError = detailedError;
        this.status = status;
    }

    public ApiError(String source, String message, HttpStatus status) {
        this.source = source;
        this.message = message;
        this.status = status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
