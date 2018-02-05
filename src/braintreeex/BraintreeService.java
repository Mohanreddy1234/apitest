package braintreeex;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;

@Path("/services")
public class BraintreeService {

	BraintreeDAO braintreeDAO = new BraintreeDAO();

	@GET
	@Path("/clienttoken")
	@Produces(MediaType.APPLICATION_JSON)
	public ClientToken getClientToken() {
		return braintreeDAO.getClientToken();
	}

	@POST
	@Path("/checkout")
	@Consumes("application/json")
	public TransactionData checkout(final TransactionRequestData transactionRequestData) {

		Result<Transaction> result = braintreeDAO.checkout(transactionRequestData.nounce);
		TransactionData retVal = new TransactionData();
		
		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();
			
			retVal.setId(transaction.getId());

		} else if (result.getTransaction() != null) {
			Transaction transaction = result.getTransaction();
			retVal.setId(transaction.getId());
		} else {
			String errorString = "";
		}

		return retVal;
	}

}
