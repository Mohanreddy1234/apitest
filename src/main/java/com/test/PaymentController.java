package com.test;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	private static final String SUCCESS_STATUS = "success";
	private static final String ERROR_STATUS = "error";
	private static final int CODE_SUCCESS = 100;
	private static final int AUTH_FAILURE = 102;

	private static BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, "ctgyp2ch3p8th334",
			"x37gfn59cynpvvhh", "cc40b83abf1cb0169594204d6bf74748");

	@RequestMapping(value = "/pay", method = RequestMethod.POST)
	public PaymentResponse pay(@RequestBody PaymentRequest paymentRequest) {
		PaymentResponse paymentResponse = new PaymentResponse();

		BigDecimal decimalAmount;
		try {
			decimalAmount = new BigDecimal(paymentRequest.getAmount());
		} catch (NumberFormatException e) {
			paymentResponse.setStatus(ERROR_STATUS);
			paymentResponse.setCode(AUTH_FAILURE);
			return paymentResponse;
		}

		TransactionRequest request = new TransactionRequest().amount(decimalAmount)
				.paymentMethodNonce(paymentRequest.getNonce()).options().submitForSettlement(true).done();

		Result<Transaction> result = gateway.transaction().sale(request);

		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();
			paymentResponse.setId(transaction.getId());
		} else if (result.getTransaction() != null) {
			Transaction transaction = result.getTransaction();
			paymentResponse.setId(transaction.getId());
		} else {
			String errorString = "";
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
				errorString += "Error: " + error.getCode() + ": " + error.getMessage() + "\n";
			}
			paymentResponse.setMessage(errorString);
		}
		return paymentResponse;
	}

	@RequestMapping(value = "/gettoken", method = RequestMethod.GET)
	public ClientToken gettoken() {
		ClientToken response = new ClientToken();

		try {
			response.setToken(gateway.clientToken().generate());
			response.setStatus(SUCCESS_STATUS);
			response.setCode(CODE_SUCCESS);
		} catch (Exception e) {
			response.setStatus(ERROR_STATUS);
			response.setCode(AUTH_FAILURE);
		}

		return response;
	}
}
