package application.commonClasses.messages;

import java.time.LocalDate;
import java.util.ArrayList;

import application.commonClasses.Tasks_Priority;
import application.commonClasses.messages.Message.NameValue;

public class CreateToDoMsg extends Message {
	private static final String CLIENT_TOKEN = "token";
	private static final String TODO_TITEL = "titel";
	private static final String TODO_PRIORITY = "priority";
	private static final String TODO_DESCRIPTION = "description";
	//private static final String TODO_DUE_DATE = "due_date";
	
	
	private String titel;
	private String priority;
	private String description;
	//private LocalDate dueDate;


	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.token = findAttributes(pairs, CLIENT_TOKEN);
		this.titel = findAttributes(pairs, TODO_TITEL);
		this.priority = (findAttributes(pairs, TODO_PRIORITY));
		this.description = findAttributes(pairs, TODO_DESCRIPTION);
		//this.dueDate = LocalDate.parse(findAttributes(pairs, TODO_DUE_DATE));
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(TODO_TITEL, this.titel));
		pairs.add(new NameValue(TODO_PRIORITY, this.priority));
		pairs.add(new NameValue(TODO_DESCRIPTION, this.description));
		//pairs.add(new NameValue(TODO_DUE_DATE, this.dueDate.toString()));
		
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
