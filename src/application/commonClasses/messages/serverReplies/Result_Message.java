package application.commonClasses.messages.serverReplies;

import java.util.ArrayList;

import application.commonClasses.messages.Message;
import application.commonClasses.messages.MessageType;

public class Result_Message extends Message{
	protected static final String SERVER_RESPONSE = "Result";
	public static final String ATTR_SUBTYPE = "subtype";
	
	protected String result;
	
	public Result_Message() {
		super();
	}

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.setResult(findAttributes(pairs, SERVER_RESPONSE));
		
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(SERVER_RESPONSE, result));
		
	}
	
	@Override
	public String toString() {
		ArrayList<NameValue> pairs = new ArrayList<>();
		
		pairs.add(new NameValue(ATTR_TYPE, MessageType.getType(this).toString()));
		pairs.add(new NameValue(ATTR_SUBTYPE, Result_MessageTypes.getType(this).toString()));
		pairs.add(new NameValue(CLIENT_TOKEN, token));
		
		this.sendAttributes(pairs);
		
		StringBuilder buf = new StringBuilder();
		for (NameValue pair : pairs) {
			buf.append(pair.toString() + "\n");
		}
		
		return buf.toString();
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	

}
