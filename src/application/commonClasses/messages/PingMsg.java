package application.commonClasses.messages;

import java.util.ArrayList;
import java.util.Random;

public class PingMsg extends Message {
	//private static final String CLIENT_TOKEN = "token";
	private static final String CLIENT_LOGIN = "login";
	
	
	//private String token;
	private boolean login = false;

	public boolean getLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.token = findAttributes(pairs, CLIENT_TOKEN);
		
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {	
	}
	
}
