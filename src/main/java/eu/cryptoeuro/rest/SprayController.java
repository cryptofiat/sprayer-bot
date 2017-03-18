package eu.cryptoeuro.rest;

import eu.cryptoeuro.domain.Spray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(value="spray",
        description="Create spray.",
        produces = "application/json", consumes = "application/json")
@RestController
@RequestMapping("/v1/spray")
@Slf4j
@CrossOrigin(origins = "*")
public class SprayController {

    @ApiOperation(value = "Create a spray event")
    @RequestMapping(method = POST)
    public Spray create(@Valid @RequestBody CreateSprayCommand createSprayCommand,
                        @ApiIgnore @Valid Errors errors) {

        if (errors.hasErrors()) {
//            throw new
        }

        return null;
    }


}
