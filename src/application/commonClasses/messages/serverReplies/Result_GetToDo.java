package application.commonClasses.messages.serverReplies;

import java.util.ArrayList;

public class Result_GetToDo extends ResultMsg {

	private static final String TODO_ATTRIBUTES = "attributes";

	private String attributes;

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		super.receiveAttributes(pairs);
		String[] parts = this.getResult().split("\\|");
		this.setAttributes(parts[0] + "|" + parts[1] + "|" + parts[2] + "|" + parts[3]);
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		super.sendAttributes(pairs);
		pairs.add(new NameValue(TODO_ATTRIBUTES, this.getAttributes()));
	}
}
