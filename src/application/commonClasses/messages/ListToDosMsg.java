package application.commonClasses.messages;

import java.util.ArrayList;
import java.util.Random;

import application.abstractClasses.Task;
import application.commonClasses.messages.Message.NameValue;

public class ListToDosMsg extends Message {

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.token = findAttributes(pairs, CLIENT_TOKEN);
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		// TODO Auto-generated method stub
		
	}
	
}
