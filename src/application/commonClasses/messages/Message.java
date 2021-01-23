package application.commonClasses.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import application.commonClasses.messages.serverReplies.ResultChangePasswordMsg;
import application.commonClasses.messages.serverReplies.ResultCreateToDoMsg;
import application.commonClasses.messages.serverReplies.ResultDeleteToDoMsg;
import application.commonClasses.messages.serverReplies.ResultErrorMsg;
import application.commonClasses.messages.serverReplies.ResultGetToDoMsg;
import application.commonClasses.messages.serverReplies.ResultListToDoMsg;
import application.commonClasses.messages.serverReplies.ResultLoginMsg;
import application.commonClasses.messages.serverReplies.ResultLogoutMsg;
import application.commonClasses.messages.serverReplies.ResultMessageTypes;
import application.commonClasses.messages.serverReplies.ResultMsg;
import application.commonClasses.messages.serverReplies.ResultPingMsg;
import application.commonClasses.messages.serverReplies.ResultRegisterMsg;

public abstract class Message {
	private static Logger logger = Logger.getLogger("");

	protected static final String ATTR_TYPE = "type";
	protected static final String CLIENT_TOKEN = "token";
	//private static final String ATTR_CLIENT = "client";
	//private static final String ATTR_ID = "id";
	//private static final String ATTR_TIMESTAMP = "timestamp";

	protected String message;
	protected String token = null;
	//private long id;
	//private long timestamp;
	//private String client;

	private static long messageID = 0;

	//public Message() {
	//	this.id = -1;
	//	message = null;
	//}

	//private static long nextMessageID() {
	//	return messageID++;
	//}

	protected static class NameValue {
		public String name;
		public String value;

		public NameValue(String name, String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name + "|" + value;
		}
	}

	
	protected abstract void receiveAttributes(ArrayList<NameValue> attributes);

	protected abstract void sendAttributes(ArrayList<NameValue> attributes);

	protected static String findAttributes(ArrayList<NameValue> pairs, String name) {
		Iterator<NameValue> i = pairs.iterator();
		while (i.hasNext()) {
			NameValue pair = i.next();
			if (pair.name.equals(name)) {
				i.remove();
				return pair.value;
			}
		}
		return null;
	}

	public void send(Socket socket) {
		// set ID of message
		//if (this.id == -1)
		//	this.id = nextMessageID();

		//this.timestamp = System.currentTimeMillis();

		// mesage to string format
		message = this.toString();

		try {
			OutputStreamWriter out;
			out = new OutputStreamWriter(socket.getOutputStream());
			out.write(message);
			out.write("\n"); // empty line
			out.flush();
			socket.shutdownOutput(); // end output without closing socket
		} catch (IOException e) {
			logger.warning(e.toString());
		}
	}

	public static Message receive(Socket socket) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuffer buf = new StringBuffer();
		String msgIn = in.readLine();
		while (msgIn != null && msgIn.length() > 0) {
			buf.append(msgIn + "\n");
			msgIn = in.readLine();
		}
		msgIn = buf.toString();

		String[] nameValuePairs = msgIn.split("\n");
		ArrayList<NameValue> pairs = new ArrayList<>();

		for (String nvPair : nameValuePairs) {
			int equalPos = nvPair.indexOf("|");
			NameValue pair = new NameValue(nvPair.substring(0, equalPos),
					nvPair.substring(equalPos + 1, nvPair.length()));
			pairs.add(pair);
		}

		NameValue messageType = pairs.remove(0);
		Message newMessage = null;
		boolean allOk = messageType.name.equals(ATTR_TYPE);
		MessageType type = MessageType.parseType(messageType.value);
		if (!allOk) {
			ErrorMsg msg = new ErrorMsg();
			msg.setInfo("Error parsing received message");
			newMessage = msg;
		}
		if (allOk) {
			if (type == MessageType.Register)
				newMessage = new RegisterMsg();
			else if (type == MessageType.Login)
				newMessage = new LoginMsg();
			else if (type == MessageType.ChangePassword)
				newMessage = new ChangePasswordMsg();
			else if (type == MessageType.CreateToDo)
				newMessage = new CreateToDoMsg();
			else if (type == MessageType.ListToDos)
				newMessage = new ListToDosMsg();
			else if (type == MessageType.GetToDo)
				newMessage = new GetToDoMsg();
			else if (type == MessageType.DeleteToDo)
				newMessage = new DeleteToDoMsg();
			else if (type == MessageType.Logout)
				newMessage = new LogoutMsg();
			else if (type == MessageType.Ping)
				newMessage = new PingMsg();
			else if(type == MessageType.Result) {
				NameValue subMessageType = pairs.remove(0);
				boolean subOk = subMessageType.name.equals(ResultMsg.ATTR_SUBTYPE);
				ResultMessageTypes subType = ResultMessageTypes.parseType(subMessageType.value);
				if(!subOk) {
					ResultErrorMsg subMsg = new ResultErrorMsg();
					subMsg.setError("Error parsing reply.");
					newMessage = subMsg;
				}
				if(subOk) {
					if(subType == ResultMessageTypes.ResultPing) {
						ResultPingMsg msg = new ResultPingMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultRegister) {
						ResultRegisterMsg msg = new ResultRegisterMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultLogin) {
						ResultLoginMsg msg = new ResultLoginMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultChangePassword) {
						ResultChangePasswordMsg msg = new ResultChangePasswordMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultCreateTodo) {
						ResultCreateToDoMsg msg = new ResultCreateToDoMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultGetToDo) {
						ResultGetToDoMsg msg = new ResultGetToDoMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultListToDo) {
						ResultListToDoMsg msg = new ResultListToDoMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultDeleteToDo) {
						ResultDeleteToDoMsg msg = new ResultDeleteToDoMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultLogout) {
						ResultLogoutMsg msg = new ResultLogoutMsg();
						newMessage = msg;
					}
					else if(subType == ResultMessageTypes.ResultError) {
						ResultErrorMsg msg = new ResultErrorMsg();
						newMessage = msg;
					}
				}
			}

			//newMessage.setId(Long.parseLong(findAttributes(pairs, ATTR_ID)));
			//newMessage.setTimestamp(Long.parseLong(findAttributes(pairs, ATTR_TIMESTAMP)));
			//newMessage.setClient(findAttributes(pairs, ATTR_CLIENT));
		}

		newMessage.receiveAttributes(pairs);

		return newMessage;

	}

	@Override
	public String toString() {
		ArrayList<NameValue> pairs = new ArrayList<>();

		pairs.add(new NameValue(ATTR_TYPE, MessageType.getType(this).toString()));
		pairs.add(new NameValue(CLIENT_TOKEN, token));
		//pairs.add(new NameValue(ATTR_CLIENT, this.client));
		//pairs.add(new NameValue(ATTR_ID, Long.toString(this.id)));
		//pairs.add(new NameValue(ATTR_TIMESTAMP, Long.toString(this.timestamp)));

		this.sendAttributes(pairs);

		StringBuilder buf = new StringBuilder();
		for (NameValue pair : pairs) {
			buf.append(pair.toString() + "\n");
		}

		return buf.toString();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	/*public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}*/

}
