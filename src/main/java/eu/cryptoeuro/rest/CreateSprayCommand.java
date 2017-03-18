package eu.cryptoeuro.rest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.AssertTrue;

import lombok.Data;

@Data
public class CreateSprayCommand {
    @NotNull
    @Size(min = 1, max = 256)
    private String idCode;

}
