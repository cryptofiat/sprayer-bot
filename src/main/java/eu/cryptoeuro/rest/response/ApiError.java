package eu.cryptoeuro.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    String message;

    public ApiError(String message) {
        this.message = message;
    }

}
