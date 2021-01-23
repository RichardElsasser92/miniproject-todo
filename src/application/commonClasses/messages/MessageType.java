package application.commonClasses.messages;

import application.commonClasses.messages.serverReplies.Result_Message;

public enum MessageType {
	Ping, Register, Login, ChangePassword, CreateToDo, ListToDos, GetToDo, DeleteToDo, Logout, Result, Error;

	public static MessageType parseType(String typeName) {
		MessageType type = MessageType.Error;
		for (MessageType value : MessageType.values()) {
			if (value.toString().equals(typeName))
				type = value;
		}
		return type;
	}

	public static MessageType getType(Message msg) {
		MessageType type = MessageType.Error;
		if (msg instanceof Ping)
			type = Ping;
		else if (msg instanceof Register)
			type = Register;
		else if (msg instanceof Login)
			type = Login;
		else if (msg instanceof ChangePassword)
			type = ChangePassword;
		else if (msg instanceof CreateToDo)
			type = CreateToDo;
		else if (msg instanceof ListToDos)
			type = ListToDos;
		else if (msg instanceof GetToDo)
			type = GetToDo;
		else if (msg instanceof DeleteToDo)
			type = DeleteToDo;
		else if (msg instanceof Logout)
			type = Logout;
		else if (msg instanceof Result_Message)
			type = Result;
		return type;

	}

}