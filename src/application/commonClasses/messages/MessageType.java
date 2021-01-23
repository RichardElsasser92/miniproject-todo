package application.commonClasses.messages;

import application.commonClasses.messages.serverReplies.ResultMsg;

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
		if (msg instanceof PingMsg)
			type = Ping;
		else if (msg instanceof RegisterMsg)
			type = Register;
		else if (msg instanceof LoginMsg)
			type = Login;
		else if (msg instanceof ChangePasswordMsg)
			type = ChangePassword;
		else if (msg instanceof CreateToDoMsg)
			type = CreateToDo;
		else if (msg instanceof ListToDosMsg)
			type = ListToDos;
		else if (msg instanceof GetToDoMsg)
			type = GetToDo;
		else if (msg instanceof DeleteToDoMsg)
			type = DeleteToDo;
		else if (msg instanceof LogoutMsg)
			type = Logout;
		else if (msg instanceof ResultMsg)
			type = Result;
		return type;

	}

}