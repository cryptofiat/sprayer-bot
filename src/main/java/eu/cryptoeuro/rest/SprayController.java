package eu.cryptoeuro.rest;

import eu.cryptoeuro.domain.Spray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import eu.cryptoeuro.service.SprayService;
import eu.cryptoeuro.rest.exception.ValidationException;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(value="spray",
        description="Create spray.",
        produces = "application/json", consumes = "application/json")
@RestController
@RequestMapping("/v1/spray")
@Slf4j
@CrossOrigin(origins = "*")
public class SprayController {

    @Autowired
    private SprayService sprayService;


    @ApiOperation(value = "Create a spray event")
    @RequestMapping(method = POST)
    public Spray create(@Valid @RequestBody CreateSprayCommand createSprayCommand,
                        @ApiIgnore @Valid Errors errors) {

        if (errors.hasErrors()) {
            //log.error("Request validation failed", errors);
            throw new ValidationException(errors);
        }

        Spray spray = sprayService.spray(createSprayCommand);

        return spray;
    }


}
