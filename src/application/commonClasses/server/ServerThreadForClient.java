package application.commonClasses.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import application.abstractClasses.Task;
import application.appClasses.Tasks;
import application.appClasses.User;
import application.commonClasses.fileManager.File_Manager;
import application.commonClasses.fileManager.Tasks_FM;
import application.commonClasses.fileManager.User_FM;
import application.commonClasses.messages.LoginMsg;
import application.commonClasses.messages.Message;
import application.commonClasses.messages.MessageType;
import application.commonClasses.messages.serverReplies.ResultChangePasswordMsg;
import application.commonClasses.messages.serverReplies.ResultCreateToDoMsg;
import application.commonClasses.messages.serverReplies.ResultDeleteToDoMsg;
import application.commonClasses.messages.serverReplies.ResultErrorMsg;
import application.commonClasses.messages.serverReplies.ResultGetToDoMsg;
import application.commonClasses.messages.serverReplies.ResultListToDoMsg;
import application.commonClasses.messages.serverReplies.ResultLoginMsg;
import application.commonClasses.messages.serverReplies.ResultLogoutMsg;
import application.commonClasses.messages.serverReplies.ResultPingMsg;
import application.commonClasses.messages.serverReplies.ResultRegisterMsg;

public class ServerThreadForClient extends Thread {
	private final Logger logger = Logger.getLogger("");
	private Socket clientSocket;

	public ServerThreadForClient(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		logger.info(" Request from client " + clientSocket.getInetAddress().toString() + " for server "
				+ clientSocket.getLocalAddress().toString());

		try {
			Message msgIn = Message.receive(clientSocket);
			Message msgOut = processMessage(msgIn);
			logger.info("Message sent: " + msgOut.toString());
			msgOut.send(clientSocket);
		} catch (Exception e) {
			logger.severe(e.toString());
		} finally {
			try {
				if (clientSocket != null)
					clientSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private Message processMessage(Message msgIn) {
		logger.info("Message received from client: " + msgIn.toString());

		Message msgOut = null;
		switch (MessageType.getType(msgIn)) {
		case Ping:
			ResultPingMsg repMsg = new ResultPingMsg();
			String messageIn = msgIn.toString();
			if (msgIn.getToken().equals("null")) {
				repMsg.setReply("true");
				repMsg.setRep();
				msgOut = repMsg;
			} else {
				boolean valid = ServerController.validToken(msgIn.getToken());
				if (valid) {
					repMsg.setToken(msgIn.getToken());
					repMsg.setReply("true");
					repMsg.setRep();
					msgOut = repMsg;
				} else {
					ResultErrorMsg errMsg = new ResultErrorMsg();
					errMsg.setError("Token is not valid!");
					msgOut = errMsg;
				}
			}
			break;
		case Register:
			ArrayList<User> users = new ArrayList<User>();
			users = getAllUsers();
			int numOfUsers = users.size();
			User.setHighestId(numOfUsers);
			ResultRegisterMsg regMsg = new ResultRegisterMsg();
			regMsg.setResult("true");
			String regMessage = msgIn.toString();
			String[] regSplit = regMessage.split("\\n");
			String[] n = regSplit[2].split("\\|");
			String name = n[1];
			String[] pa = regSplit[3].split("\\|");
			String password = pa[1];
			User user = new User(name, password);

			boolean exists = checkIfUserAlreadyExists(user);
			if (!exists) {
				saveUser(user, true);
				msgOut = regMsg;
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("User already exists! Please chose a new name or password.");
				msgOut = errMsg;
			}
			break;
		case Login:
			ResultLoginMsg logMsg = new ResultLoginMsg();
			regMessage = msgIn.toString();
			regSplit = regMessage.split("\\n");
			n = regSplit[2].split("\\|");
			name = n[1];
			pa = regSplit[3].split("\\|");
			password = pa[1];
			user = new User(name, password);

			exists = checkIfUserAlreadyExists(user);
			String token = null;
			String login = null;
			if (exists) {
				token = ServerController.login((LoginMsg) msgIn);
				login = "true";
				logMsg.setToken(token);
				logMsg.setReply(login);
				logMsg.setRep();
				msgOut = logMsg;
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("User with username: " + name + " and password: " + password
						+ " does not exist. Please register!");
				msgOut = errMsg;
			}
			break;
		case ChangePassword:
			if (!msgIn.getToken().equals("null")) {
				ResultChangePasswordMsg cPMsg = new ResultChangePasswordMsg();
				regMessage = msgIn.toString();
				regSplit = regMessage.split("\\n");
				n = regSplit[2].split("\\|");
				name = n[1];
				pa = regSplit[3].split("\\|");
				password = pa[1];
				String[] paN = regSplit[4].split("\\|");
				String passwordN = paN[1];
				String uid = ServerController.getUserId(msgIn);
				user = new User(name, passwordN);

				boolean check = changePassword(uid, name, password, passwordN);
				if (check) {
					cPMsg.setResult("true");
					user.setId(uid);
					saveUser(user, false);
					msgOut = cPMsg;
				} else {
					ResultErrorMsg errMsg = new ResultErrorMsg();
					errMsg.setError("Password could not be changed. Please check username and password and try again.");
					msgOut = errMsg;
				}
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("Token not valid. Please login again");
				msgOut = errMsg;
			}
			break;
		case CreateToDo:
			if (!msgIn.getToken().equals("null")) {
				ResultCreateToDoMsg tdMsg = new ResultCreateToDoMsg();
				String toDomessage = msgIn.toString();
				String[] split = toDomessage.split("\\n");
				String[] t = split[2].split("\\|");
				String titel = t[1];
				String[] p = split[3].split("\\|");
				String priority = p[1];
				String[] d = split[4].split("\\|");
				String description = d[1];
				String userId = ServerController.getUserId(msgIn);
				Task toDo = new Tasks(userId, titel, priority, description);
				saveTask(toDo);
				String id = toDo.getId();
				tdMsg.setToken(msgIn.getToken());
				tdMsg.setId(id);
				tdMsg.setResult("true" + "|" + id);
				msgOut = tdMsg;
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("Token not valid. Please login again");
				msgOut = errMsg;
			}
			break;
		case GetToDo:
			if (!msgIn.getToken().equals("null")) {
				ResultGetToDoMsg gTdMsg = new ResultGetToDoMsg();
				String[] split = msgIn.toString().split("\\n");
				String[] tdId = split[2].split("\\|");
				String todoId = tdId[1];
				String[] todo = getToDoById(todoId);
				gTdMsg.setResult("true" + "|" + String.join("|", todo));
				msgOut = gTdMsg;
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("Token not valid. Please login again");
				msgOut = errMsg;
			}
			break;
		case DeleteToDo:
			if (!msgIn.getToken().equals("null")) {
				ResultDeleteToDoMsg dMsg = new ResultDeleteToDoMsg();
				String[] split = msgIn.toString().split("\\n");
				String[] tdId = split[2].split("\\|");
				String todoId = tdId[1];
				boolean success = deleteToDoById(todoId);
				if (success) {
					dMsg.setResult("true");
					msgOut = dMsg;
				} else {
					ResultErrorMsg errMsg = new ResultErrorMsg();
					errMsg.setError("ToDo could not be deleted");
					msgOut = errMsg;
				}
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("Token not valid. Please login again");
				msgOut = errMsg;
			}
			break;
		case ListToDos:
			if (!msgIn.getToken().equals("null")) {
				ResultListToDoMsg ltMsg = new ResultListToDoMsg();
				String[] split = msgIn.toString().split("\\n");
				String[] ids = getUserToDoIds(msgIn);
				ltMsg.setResult("true" + "|" + String.join("|", ids));
				msgOut = ltMsg;
			} else {
				ResultErrorMsg errMsg = new ResultErrorMsg();
				errMsg.setError("Token not valid. Please login again");
				msgOut = errMsg;
			}
			break;
		case Logout:
			ResultLogoutMsg loMsg = new ResultLogoutMsg();
			ServerController.removeToken(msgIn.getToken());
			loMsg.setResult("true");
			msgOut = loMsg;
			break;
		default:
			ResultErrorMsg err = new ResultErrorMsg();
			err.setError("We're sorry something went wrong...");
			msgOut = err;
			break;

		}
		return msgOut;

	}

	private boolean changePassword(String id, String name, String password, String passwordN) {
		boolean changed = false;
		List<User> users = getAllUsers();

		for (User u : users) {
			if (u.getId().equals(id) && u.getUsername().equals(name) && u.getPassword().equals(password)) {
				changed = true;
				break;
			}
		}
		return changed;

	}

	private boolean checkIfUserAlreadyExists(User user) {
		boolean exists = false;
		List<User> users = getAllUsers();

		for (User u : users) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				exists = true;
			}
		}
		return exists;
	}

	private String[] getUserToDoIds(Message msg) {
		List<Task> todos = getAllToDos();
		List<String> userTodoIds = new ArrayList<>();

		String uid = ServerController.getUserId(msg);

		for (Task todo : todos) {
			if (todo.getUserId().equals(uid)) {
				userTodoIds.add(todo.getId());
			}
		}

		return userTodoIds.toArray(new String[0]);
	}

	public String[] getToDoById(String id) {
		List<Task> todos = getAllToDos();
		List<String> attributes = new ArrayList<>();

		for (Task todo : todos) {
			if (todo.getId().equals(id)) {
				attributes.add(todo.getId());
				attributes.add(todo.getTitel());
				attributes.add(todo.getPriority());
				attributes.add(todo.getDescription());
				break;
			}
		}
		return attributes.toArray(new String[0]);
	}

	private boolean deleteToDoById(String todoId) {
		boolean deleted = false;
		Tasks_FM fileManager = new Tasks_FM();
		List<Task> todos = getAllToDos();
		Task todo = null;

		for (Task t : todos) {
			if (t.getId().equals(todoId)) {
				todo = t;
				deleted = true;
				break;
			}
		}

		fileManager.delete(todo);
		return deleted;
	}

	public ArrayList<User> getAllUsers() {
		User_FM fileManager = new User_FM();
		return (ArrayList<User>) fileManager.getAll();
	}

	public ArrayList<Task> getAllToDos() {
		Tasks_FM fileManager = new Tasks_FM();
		return (ArrayList<Task>) fileManager.getAll();
	}

	public void saveUser(User user, boolean isNew) {
		User_FM fileManager = new User_FM();
		fileManager.save(user, isNew);
	}

	public void saveTask(Task task) {
		File_Manager fileManager = new Tasks_FM();

		fileManager.save(task);
	}

	public void deleteTask(Task task) {
		File_Manager fileManager = new Tasks_FM();
		fileManager.delete(task);
	}

	/**
	 * public SimpleObjectProperty<Task> getProperty() { return property; }
	 * 
	 * public SimpleObjectProperty<String> getUserIdProperty() { return
	 * userIdProperty; }
	 * 
	 * public SimpleObjectProperty<String> getTitelProperty() { return
	 * titelProperty; }
	 * 
	 * public SimpleObjectProperty<String> getPriorityProperty() { return
	 * priorityProperty; }
	 * 
	 * public SimpleObjectProperty<String> getDescriptionProperty() { return
	 * descriptionProperty; }
	 * 
	 * public void setProperty(Task task) { property.set(task); }
	 * 
	 * public void setUserIdProperty(String id) { userIdProperty.set(id); }
	 * 
	 * public void setTitelProperty(String titel) { titelProperty.set(titel); }
	 * 
	 * public void setPriorityProperty(String priority) {
	 * priorityProperty.set(priority); }
	 * 
	 * public void setDescriptionProperty(String description) {
	 * descriptionProperty.set(description); }
	 **/

}
