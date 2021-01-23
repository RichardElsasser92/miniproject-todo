package application.commonClasses.messages.serverReplies;

import application.commonClasses.messages.Message;

public enum ResultMessageTypes {
	ResultPing, ResultRegister, ResultLogin, ResultChangePassword, ResultCreateTodo, ResultListToDo, ResultGetToDo, ResultDeleteToDo, ResultLogout, ResultError;

	public static ResultMessageTypes parseType(String typeName) {
		ResultMessageTypes type = ResultMessageTypes.ResultError;
		for (ResultMessageTypes value : ResultMessageTypes.values()) {
			if (value.toString().equals(typeName))
				type = value;
		}
		return type;
	}

	public static ResultMessageTypes getType(Message msg) {
		ResultMessageTypes type = ResultMessageTypes.ResultError;
		if (msg instanceof ResultPingMsg)
			type = ResultPing;
		else if (msg instanceof ResultRegisterMsg)
			type = ResultRegister;
		else if (msg instanceof ResultLoginMsg)
			type = ResultLogin;
		else if (msg instanceof ResultChangePasswordMsg)
			type = ResultChangePassword;
		else if (msg instanceof ResultCreateToDoMsg)
			type = ResultCreateTodo;
		else if (msg instanceof ResultGetToDoMsg)
			type = ResultGetToDo;
		else if (msg instanceof ResultListToDoMsg)
			type = ResultListToDo;
		else if (msg instanceof ResultDeleteToDoMsg)
			type = ResultDeleteToDo;
		else if (msg instanceof ResultLogoutMsg)
			type = ResultLogout;
		return type;

	}
}