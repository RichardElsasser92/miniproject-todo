package application.commonClasses.messages;

import java.util.ArrayList;

public class DeleteToDo extends Message {
	private static final String CLIENT_TODO = "todo";
	
	private String todoId;

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.token = findAttributes(pairs, CLIENT_TOKEN);
		this.todoId = findAttributes(pairs, CLIENT_TODO);
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(CLIENT_TODO, this.todoId));
		
	}

	public String getTodoId() {
		return todoId;
	}

	public void setTodoId(String todoId) {
		this.todoId = todoId;
	}

}
