package eu.cryptoeuro.walletServer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Nonce {

    @NonNull
    private Long nonce;

}
