package org.saurav.exception;

import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.saurav.exception.model.ApiError;
import org.springframework.http.HttpStatus;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * @author Saurav Singh
 **/
@Getter
public class PromotionException extends Exception {

    private static final long serialVersionUID = 2511181762057539536L;
    public static final String UNKNOWN_ERROR_OCCURRED = "Unknown error occurred.";

    private final ApiError apiError;

    public PromotionException(String message, final @NonNull String source, final @NonNull HttpStatus statusCode) {
        super(message);
        if (isBlank(message)) {
            message = UNKNOWN_ERROR_OCCURRED;
        }
        this.apiError = new ApiError(source, message, statusCode);
    }

    public PromotionException(final @NonNull String source, String message, final @NonNull Throwable cause,
                              final @NonNull HttpStatus statusCode) {
        super(message, cause);
        if (isBlank(message)) {
            message = UNKNOWN_ERROR_OCCURRED;
        }
        String detailedMessage = getRootCauseMessage(cause);
        this.apiError = new ApiError(source, message, detailedMessage, statusCode);
    }

    public PromotionException(final @NonNull String source, String message, final @NonNull Throwable cause) {
        super(message, cause);
        if (isBlank(message)) {
            message = UNKNOWN_ERROR_OCCURRED;
        }
        String detailedMessage = getRootCauseMessage(cause);
        this.apiError = new ApiError(source, message, detailedMessage, INTERNAL_SERVER_ERROR);
    }

    public PromotionException(final @NonNull String source, final @NonNull Throwable cause) {
        String message = getRootCauseMessage(cause);
        String detailedMessage = ExceptionUtils.getMessage(cause);
        this.apiError = new ApiError(source, message, detailedMessage, INTERNAL_SERVER_ERROR);
    }
}
