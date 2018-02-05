package braintreeex;

import java.math.BigDecimal;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;

public class BraintreeDAO {

	private static BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "ctgyp2ch3p8th334",
			"x37gfn59cynpvvhh", "cc40b83abf1cb0169594204d6bf74748");

	public ClientToken getClientToken() {
		ClientToken token = new ClientToken();
		token.setToken(gateway.clientToken().generate());
		return token;
	}

	public Result<Transaction> checkout(String payment_method_nonce) {
		String nonceFromTheClient = payment_method_nonce;
		TransactionRequest request = new TransactionRequest().amount(new BigDecimal("50.00"))
				.paymentMethodNonce(nonceFromTheClient).options().submitForSettlement(true).done();

		Result<Transaction> result = gateway.transaction().sale(request);

		return result;
	}
}
