package application.commonClasses.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import application.ServiceLocator;
import application.abstractClasses.Model;
import application.commonClasses.messages.ChangePassword;
import application.commonClasses.messages.CreateToDo;
import application.commonClasses.messages.DeleteToDo;
import application.commonClasses.messages.GetToDo;
import application.commonClasses.messages.ListToDos;
import application.commonClasses.messages.Login;
import application.commonClasses.messages.Logout;
import application.commonClasses.messages.Message;
import application.commonClasses.messages.Ping;
import application.commonClasses.messages.Register;

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
			Ping msgOut = new Ping(); // create new message
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
			Register msgOut = new Register(); // create new message
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
			Login msgOut = new Login();
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
			ChangePassword msgOut = new ChangePassword();
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
			CreateToDo msgOut = new CreateToDo();
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
			ListToDos msgOut = new ListToDos();
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
			GetToDo msgOut = new GetToDo();
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
			DeleteToDo msgOut = new DeleteToDo();
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
			Logout msgOut = new Logout();
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
