package application.commonClasses.messages.serverReplies;

import java.util.ArrayList;

public class Result_CreateToDo extends ResultMsg{

	private static final String TODO_ID = "id";

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		super.receiveAttributes(pairs);
		String[] parts = this.getResult().split("\\|");
		this.setId(parts[1]);
	}
	
	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		super.sendAttributes(pairs);
		pairs.add(new NameValue(TODO_ID, this.getId()));
	}
	
}
