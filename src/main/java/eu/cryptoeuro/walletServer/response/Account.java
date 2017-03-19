package eu.cryptoeuro.walletServer.response;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

    @NotNull
    private String address;
    @NotNull
    private boolean approved;
    @NotNull
    private boolean closed;
    @NotNull
    private boolean frozen;
    @NotNull
    private Long nonce;
    @NotNull
    private Long balance;

}
