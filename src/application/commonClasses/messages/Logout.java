package application.commonClasses.messages;

import java.util.ArrayList;

public class Logout extends Message {
	private static final String CLIENT_TOKEN = "token";
	

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.token = findAttributes(pairs, CLIENT_TOKEN);
		
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
	}

}
