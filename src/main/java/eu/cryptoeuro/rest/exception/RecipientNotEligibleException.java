package eu.cryptoeuro.rest.exception;

import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RecipientNotEligibleException extends RuntimeException {

    public RecipientNotEligibleException(String exception) {
        super(exception);
    }

}
