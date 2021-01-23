package application.commonClasses.client;

import java.util.ArrayList;

import application.ServiceLocator;
import application.abstractClasses.Controller;
import application.abstractClasses.Task;
import application.appClasses.Tasks;
import application.appClasses.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Client_Controller extends Controller<Client_Model, Client_View> {
	ServiceLocator serviceLocator;

	private ObservableList<Task> todos;

	private User user;
	TextField newPassword;

	private Stage stage;
	private Stage profileStage;
	private Task selectedTodo;

	String clientIp;
	int clientPort;

	private boolean clientName = false;
	private boolean clientPassword = false;

	public Client_Controller(Client_Model model, Client_View view) {
		super(model, view);

		view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
			}
		});

		todos = FXCollections.observableArrayList();

		enableDisableRegisterLoginButton();

		goToStartPage();

		view.listView.setOnMouseClicked(this::loadAttributes);

		// client side validators
		view.txtClientName.textProperty().addListener((observable, oldValue, newValue) -> {
			validateClientName(newValue);
		});

		view.txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
			validateClientPassword(newValue);
		});

		view.btnRegister.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				clientIp = ip;
				Integer port = new Integer(view.txtPort.getText());
				clientPort = port;
				String name = view.txtClientName.getText();
				String password = view.txtPassword.getText();
				model.init(ip, port);
				view.txtMessages.setText(model.register(name, password));
			}
		});

		view.btnChangePassword.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				Integer port = new Integer(view.txtPort.getText());
				String name = user.getUsername();
				String password = user.getPassword();
				model.init(ip, port);
				view.txtMessages.setText(model.changePassword(name, password, newPassword.getText()));
				profileStage.close();
				fillList();
			}
		});

		view.menuPing.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				Integer port = new Integer(view.txtPort.getText());
				model.init(ip, port);
				view.txtMessages.setText(model.sendPing());
			}
		});

		view.btnLogin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				clientIp = ip;
				Integer port = new Integer(view.txtPort.getText());
				clientPort = port;
				String name = view.txtClientName.getText();
				String password = view.txtPassword.getText();
				user = new User(name, password);
				model.init(ip, port);
				String reply = model.login(name, password);
				view.txtMessages.setText(reply);
				String[] rep = reply.split("\\|");
				reply = rep[0];
				loginSuccesfull(reply);
			}
		});

		view.btnCreateToDo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				Integer port = new Integer(view.txtPort.getText());
				String titel = view.txtToDoTitel.getText();
				String priority = view.cmbToDoPriority.getValue().toString();
				String description = view.txtToDoDesc.getText();
				model.init(ip, port);
				view.txtMessages.setText(model.createToDo(titel, priority, description));
				fillList();
			}
		});

		// not used, todos received when clicked on list
		view.btnGetTodo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				Integer port = new Integer(view.txtPort.getText());
				String id = "#" + view.txtToDoId.getText();
				model.init(ip, port);
				view.txtMessages.setText(model.getToDo(id));
			}
		});

		// not used, list view gathers tasks of user
		view.btnListToDos.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				Integer port = new Integer(view.txtPort.getText());
				model.init(ip, port);
				view.txtMessages.setText(model.listToDo());
			}
		});

		view.menuLogout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String ip = view.txtIP.getText();
				Integer port = new Integer(view.txtPort.getText());
				model.init(ip, port);
				view.txtMessages.setText(model.logout());
				goToStartPage();
			}
		});

		view.menuProfile.setOnAction(this::userProfile);

	}

	public void userProfile(ActionEvent event) {
		GridPane root = new GridPane();
		
		Label name;
		Label password;
		
		if (!user.equals(null)) {
			name = new Label(user.getUsername());
			password = new Label(user.getPassword());
			newPassword = new TextField("");

			root.add(view.n, 0, 0);
			root.add(name, 1, 0);
			root.add(view.p, 0, 1);
			root.add(password, 1, 1);
			root.add(view.np, 0, 2);
			root.add(newPassword, 1, 2);
			root.add(view.btnChangePassword, 1, 3);

			profileStage = new Stage();
			profileStage.setTitle(view.pT.getText());
			Scene scene = new Scene(root, 350, 200);
			scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			profileStage.setScene(scene);

			profileStage.show();

		}

	}

	public void deleteTodo(ActionEvent event) {
		String id = selectedTodo.getId();
		model.init(clientIp, clientPort);
		view.txtMessages.setText(model.deleteToDo(id));
		stage.close();
		fillList();
	}

	private void validateClientPassword(String newValue) {
		boolean valid = isValidClientPassword(newValue);

		view.txtPassword.getStyleClass().remove("valid");
		view.txtPassword.getStyleClass().remove("invalid");

		if (valid) {
			view.txtPassword.getStyleClass().add("valid");
		} else {
			view.txtPassword.getStyleClass().add("invalid");
		}
		clientPassword = valid;

		enableDisableRegisterLoginButton();

	}
	

	private boolean isValidClientPassword(String newValue) {
		boolean valid = false;

		if (newValue.length() >= 3 && newValue.length() <= 20) {
			valid = true;
		}

		return valid;
	}
	

	private void validateClientName(String newValue) {
		boolean valid = isValidClientName(newValue);

		view.txtClientName.getStyleClass().remove("valid");
		view.txtClientName.getStyleClass().remove("invalid");

		if (valid) {
			view.txtClientName.getStyleClass().add("valid");
		} else {
			view.txtClientName.getStyleClass().add("invalid");
		}
		clientName = valid;

		enableDisableRegisterLoginButton();
	}
	

	private boolean isValidClientName(String newValue) {
		boolean valid = false;

		try {
			String[] nameParts = newValue.split("@");
			if (nameParts.length == 2 && !nameParts[0].isEmpty() && !nameParts[1].isEmpty()) {

			}
			if (nameParts[1].charAt(nameParts[1].length() - 1) != '.') {
				String[] domainParts = nameParts[1].split("\\.");
				if (domainParts.length >= 2) {
					valid = true;
					for (String s : domainParts) {
						if (s.length() < 2)
							valid = false;
					}
				}
			}
		} catch (Exception e) {
			return valid;
		}
		return valid;
	}
	

	private void enableDisableRegisterLoginButton() {
		if (clientName && clientPassword) {
			view.btnLogin.setDisable(false);
			view.btnRegister.setDisable(false);
		} else {
			view.btnLogin.setDisable(true);
			view.btnRegister.setDisable(true);
		}

	}
	

	private void loginSuccesfull(String reply) {
		if (reply.equals("Result")) {
			toDoManagement();
		}
	}
	

	public void fillList() {
		model.init(clientIp, clientPort); // setup model
		String msg = model.listToDo(); // get all task ids of current user
		// process message
		String[] rep = msg.split("\\|");
		ArrayList<String> t = new ArrayList<String>();

		for (String s : rep) {
			t.add(s);
		}
		t.remove(0);
		t.remove(0);

		// create array of all todos
		ArrayList<Task> td = new ArrayList<Task>();
		for (String s : t) { // for every Todo Id received
			model.init(clientIp, clientPort); // set up model
			String result = model.getToDo(s); // get Todo
			// process message
			String[] parts = result.split("\\|");
			String id = parts[2];
			String titel = parts[3];
			String priority = parts[4];
			String description = parts[5];
			
			// test data
			Task todo = new Tasks("00", titel, priority, description); 
			todo.setId(id);
			todo.setTitel(titel);
			todo.setPriority(priority);
			todo.setDescription(description);
			td.add(todo); // ad to list of todos
		}
		todos.clear(); // clear observable list of todos
		todos.addAll(td); // add all todos to observable list
		view.listView.setItems(todos); // fill list view
	}

	public void loadAttributes(MouseEvent e) {
		GridPane root = new GridPane();

		Label titel;
		Label prio;
		Label desc;

		selectedTodo = view.listView.getSelectionModel().getSelectedItem();

		if (!selectedTodo.equals(null)) {
			titel = new Label(selectedTodo.getTitel());
			prio = new Label(selectedTodo.getPriority());
			desc = new Label(selectedTodo.getDescription());

			titel.getStyleClass().add("display");
			prio.getStyleClass().add("display");
			desc.getStyleClass().add("display");

			view.btnDeleteToDo.setOnAction(this::deleteTodo);

			root.add(view.ti, 0, 0);
			root.add(titel, 1, 0);
			root.add(view.pr, 0, 1);
			root.add(prio, 1, 1);
			root.add(view.d, 0, 2);
			root.add(desc, 1, 2);
			root.add(view.btnDeleteToDo, 1, 3);

			stage = new Stage();
			stage.setTitle(view.tT.getText());
			Scene scene = new Scene(root, 500, 500);
			scene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
			stage.setScene(scene);

			stage.show();
		}
	}

	private void goToStartPage() {
		view.lblSceneTitel.setText("Welcome!!");
		
		view.txtToDoTitel.clear();
		view.txtToDoDesc.clear();
		view.cmbToDoPriority.getSelectionModel().clearSelection();

		view.reg.getChildren().clear();
		view.td.getChildren().clear();

		view.regBox.add(view.sp, 0, 5, 2, 1);
		view.reg.getChildren().add(view.register);

		view.getStage().sizeToScene();

	}

	private void toDoManagement() {
		view.lblSceneTitel.setText("Task Manager");

		view.reg.getChildren().clear();
		view.td.getChildren().clear();

		view.todoMiddle.add(view.sp, 0, 4, 2, 1);

		view.td.getChildren().add(view.toDo);

		view.getStage().sizeToScene();

		view.listView.setCellFactory(view.getListViewCellFactory());

		fillList();
	}

}
