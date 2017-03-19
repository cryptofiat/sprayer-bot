package eu.cryptoeuro.rest;

import com.google.common.base.Throwables;
import eu.cryptoeuro.rest.exception.ValidationException;
import eu.cryptoeuro.rest.response.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ApiExceptionHandlerAdvice {
    /**
     * Catch validation exception and return correct HTTP return code
     */
    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ApiError exception(Exception exception, WebRequest request) {
        return new ApiError(Throwables.getRootCause(exception).getMessage());
    }
}
