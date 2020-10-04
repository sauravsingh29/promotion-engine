package org.saurav.api.advice;

import lombok.NonNull;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.saurav.exception.CartException;
import org.saurav.exception.ProductException;
import org.saurav.exception.PromotionException;
import org.saurav.exception.model.ApiError;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Saurav Singh
 **/
@RestControllerAdvice
public class ApiAdvice extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = String.format("Missing %s path parameter.", ex.getVariableName());
        return new ResponseEntity<>(new ApiError(BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errorMessage = String.format("Missing %s request query parameter", ex.getParameterName());
        return new ResponseEntity<>(new ApiError(BAD_REQUEST, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String error = "Malformed JSON request";
        String details = ExceptionUtils.getRootCauseMessage(ex);
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, details));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        String error = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(";"));
        return new ResponseEntity<>(new ApiError(BAD_REQUEST, error), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PromotionException.class)
    public final ResponseEntity<ApiError> handlePromotionEngineException(
            PromotionException ex, WebRequest request) {
        final ApiError apiError = ex.getApiError();
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(ProductException.class)
    public final ResponseEntity<ApiError> handleProductException(
            ProductException ex, WebRequest request) {
        final ApiError apiError = ex.getApiError();
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    @ExceptionHandler(CartException.class)
    public final ResponseEntity<ApiError> handleCartException(
            CartException ex, WebRequest request) {
        final ApiError apiError = ex.getApiError();
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(final @NonNull ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
