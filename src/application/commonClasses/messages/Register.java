package application.commonClasses.messages;

import java.util.ArrayList;

public class Register extends Message {
	private static final String CLIENT_PASSWORD = "password";
	private static final String CLIENT_NAME = "name";
	
	private String password;
	private String name;
	
	public Register() {
		super();
	}
	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.name = findAttributes(pairs, CLIENT_NAME);
		this.password = findAttributes(pairs, CLIENT_PASSWORD);
	}    
	
	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(CLIENT_NAME, this.name));
		pairs.add(new NameValue(CLIENT_PASSWORD, this.password));
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
}
