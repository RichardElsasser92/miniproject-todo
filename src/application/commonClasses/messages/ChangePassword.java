package application.commonClasses.messages;

import java.util.ArrayList;

public class ChangePassword extends Message {
	private static final String CLIENT_NAME = "name";
	private static final String CLIENT_PASSWORD = "password";
	private static final String CLIENT_NEW_PASSWORD = "new_password";
	
	private String name;
	private String password;
	private String newPassword;

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.token = findAttributes(pairs, CLIENT_TOKEN);
		this.name = findAttributes(pairs, CLIENT_NAME);
		this.password = findAttributes(pairs, CLIENT_PASSWORD);
		this.newPassword = findAttributes(pairs, CLIENT_NEW_PASSWORD);
	}



	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(CLIENT_NAME, this.name));
		pairs.add(new NameValue(CLIENT_PASSWORD, this.password));
		pairs.add(new NameValue(CLIENT_NEW_PASSWORD, this.newPassword));
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
