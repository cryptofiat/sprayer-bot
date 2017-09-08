package eu.cryptoeuro.rest;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Data
public class CreateSprayCommand {
    @NotNull
    @Size(min = 1, max = 256)
    private String idCode;

}
