package application.commonClasses.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import application.ServiceLocator;
import application.abstractClasses.Model;
import application.commonClasses.messages.ChangePasswordMsg;
import application.commonClasses.messages.CreateToDoMsg;
import application.commonClasses.messages.DeleteToDoMsg;
import application.commonClasses.messages.GetToDoMsg;
import application.commonClasses.messages.ListToDosMsg;
import application.commonClasses.messages.LoginMsg;
import application.commonClasses.messages.LogoutMsg;
import application.commonClasses.messages.Message;
import application.commonClasses.messages.PingMsg;
import application.commonClasses.messages.RegisterMsg;

public class Client_Model extends Model{
	ServiceLocator serviceLocator;

	private Logger logger = Logger.getLogger("");
	private Socket socket;
	private String name;
	private String token;
	private boolean login = false;

	private String ipAddress;
	private Integer port;

	public Client_Model() {
		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Client model initalized");
	}
	
	public void init(String ipAddress, Integer port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	private Socket connect() {
		logger.info("Connect");
		Socket socket = null;
		try {
			socket = new Socket(ipAddress, port);
		} catch (Exception e) {
			logger.warning(e.toString());
		}
		return socket;
	}

	public void disconnect() {
		logger.info("Disconnect");
		if (socket != null)
			try {
				socket.close();
			} catch (IOException e) {

			}
	}

	public String sendPing() {
		String result = null;
		Socket socket = connect(); // connect to server
		if (socket != null) {
			PingMsg msgOut = new PingMsg(); // create new message
			msgOut.setLogin(login);
			msgOut.setToken(this.token);
			// msgOut.setClient(name); // initialize message
			try {
				msgOut.send(socket); // send message
				Message msgIn = Message.receive(socket); // receive reply
				result = msgIn.toString(); // save in a string
				String[] split = result.split("\\n");
				result = split[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString()); // if error log it
			}
			disconnect(); // disconnect from server
		}
		return result;
	}

	public String register(String name, String password) {
		String result = null;
		Socket socket = connect(); // connect to server
		if (socket != null) {
			RegisterMsg msgOut = new RegisterMsg(); // create new message
			msgOut.setName(name); // initialize message
			msgOut.setPassword(password); // -||-
			try {
				msgOut.send(socket); // send message
				Message msgIn = Message.receive(socket); // receive reply
				result = msgIn.toString(); // save in string
				String[] split = result.split("\\n");
				result = split[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect(); // disconnect from server
		}
		return result;
	}

	public String login(String name, String password) {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			LoginMsg msgOut = new LoginMsg();
			msgOut.setName(name);
			msgOut.setPassword(password);
			try {
				msgOut.send(socket);
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
				String[] split2 = result.split("\\|");
				if (split2[0].equals("error")) {
					this.token = null;
				} else {
					this.token = split2[2];
				}
				logger.info(token);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;
	}
	
	public String changePassword(String name, String password, String newPassword) {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			ChangePasswordMsg msgOut = new ChangePasswordMsg();
			msgOut.setToken(this.token);
			msgOut.setName(name);
			msgOut.setPassword(password);
			msgOut.setNewPassword(newPassword);
			try {
				msgOut.send(socket);
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
				String[] split2 = result.split("\\|");
				if (split2[0].equals("error")) {
					this.token = null;
				} else {
					this.token = split2[2];
				}
				logger.info(token);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;
	}

	public String createToDo(String titel, String priority, String description) {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			CreateToDoMsg msgOut = new CreateToDoMsg();
			msgOut.setToken(this.token);
			msgOut.setTitel(titel);
			msgOut.setPriority(priority);
			msgOut.setDescription(description);
			try {
				msgOut.send(socket);
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;
	}

	public String listToDo() {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			ListToDosMsg msgOut = new ListToDosMsg();
			msgOut.setToken(this.token);
			try {
				msgOut.send(socket);
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;
	}

	public String getToDo(String id) {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			GetToDoMsg msgOut = new GetToDoMsg();
			msgOut.setToken(token);
			msgOut.setTodoId(id);
			try {
				msgOut.send(socket);
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;
	}
	
	public String deleteToDo(String id) {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			DeleteToDoMsg msgOut = new DeleteToDoMsg();
			msgOut.setToken(token);
			msgOut.setTodoId(id);
			try {
				msgOut.send(socket);
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;
	}

	public String logout() {
		String result = null;
		Socket socket = connect();
		if (socket != null) {
			LogoutMsg msgOut = new LogoutMsg();
			msgOut.setToken(this.token);
			try {
				msgOut.send(socket);
				this.token = null;
				Message msgIn = Message.receive(socket);
				result = msgIn.toString();
				String[] split1 = result.split("\\n");
				result = split1[3];
				logger.info(result);
			} catch (Exception e) {
				logger.warning(e.toString());
			}
			disconnect();
		}
		return result;

	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
