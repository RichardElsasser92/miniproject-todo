package application.commonClasses.messages.serverReplies;

import application.commonClasses.messages.Message;

public enum Result_MessageTypes {
	ResultPing, ResultRegister, ResultLogin, ResultChangePassword, ResultCreateTodo, ResultListToDo, ResultGetToDo, ResultDeleteToDo, ResultLogout, ResultError;

	public static Result_MessageTypes parseType(String typeName) {
		Result_MessageTypes type = Result_MessageTypes.ResultError;
		for (Result_MessageTypes value : Result_MessageTypes.values()) {
			if (value.toString().equals(typeName))
				type = value;
		}
		return type;
	}

	public static Result_MessageTypes getType(Message msg) {
		Result_MessageTypes type = Result_MessageTypes.ResultError;
		if (msg instanceof Result_Ping)
			type = ResultPing;
		else if (msg instanceof Result_Register)
			type = ResultRegister;
		else if (msg instanceof Result_Login)
			type = ResultLogin;
		else if (msg instanceof Result_ChangePassword)
			type = ResultChangePassword;
		else if (msg instanceof Result_CreateToDo)
			type = ResultCreateTodo;
		else if (msg instanceof Result_GetToDo)
			type = ResultGetToDo;
		else if (msg instanceof Result_ListToDo)
			type = ResultListToDo;
		else if (msg instanceof Result_DeleteToDo)
			type = ResultDeleteToDo;
		else if (msg instanceof Result_Logout)
			type = ResultLogout;
		return type;

	}
}