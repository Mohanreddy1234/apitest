package braintreeex;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "clienttoken")
public class ClientToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
