package eu.cryptoeuro.walletServer.command;

import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.AssertTrue;

import eu.cryptoeuro.walletServer.FeeConstant;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateTransferCommand implements Serializable {

    @NotNull
    @Size(min = 1, max = 256)
    private String sourceAccount;
    @NotNull
    @Size(min = 1, max = 256)
    private String targetAccount;
    @NotNull
    @Min(1)
    private Long amount;
    @Min(1)
    private Long fee;
    @NotNull
    @Min(1)
    private Long nonce;
    @NotNull
    private String signature;
}
