package application.commonClasses.client;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import application.ServiceLocator;
import application.abstractClasses.Controller;
import application.abstractClasses.Task;
import application.appClasses.Tasks;
import application.appClasses.User;

import java.util.Locale;
import java.util.logging.Logger;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import application.ServiceLocator;
import application.abstractClasses.Task;
import application.abstractClasses.View;
import application.commonClasses.Tasks_Priority;
import application.commonClasses.Translator;

public class Client_View extends View<Client_Model> {
	ListView<Task> listView;
	Translator t;
	
	GridPane root;
	GridPane todoMiddle;
	GridPane regBox;
	VBox sp; 
	

	protected Label lblIP;
	protected TextField txtIP;
	protected Label lblPort;
	protected TextField txtPort;
	protected Label lblClientName;
	protected TextField txtClientName;
	protected Tooltip ttName;
	protected Label lblPassword;
	protected TextField txtPassword;
	protected Tooltip ttPassword;
	protected Label lblNewPassword;
	protected TextField txtNewPassword;
	protected TextArea txtMessages;

	
	protected Label pT;
	protected Label n;
	protected Label p;
	protected Label np;

	protected Label tT;
	protected Label ti;
	protected Label pr;
	protected Label d;


	protected Label lblToDoTitel;
	protected TextField txtToDoTitel;
	protected Label lblToDoPriority;
	protected ComboBox<Tasks_Priority> cmbToDoPriority;
	protected Label lblToDoDesc;
	protected TextField txtToDoDesc;

	protected Label lblToDoId;
	protected TextField txtToDoId;
	
	Menu menuFile;
	Menu menuFileLanguage;
	MenuItem menuProfile;
	MenuItem menuPing;
	MenuItem menuLogout;


	protected Button btnRegister;
	protected Button btnLogin;
	protected Button btnCreateToDo;
	protected Button btnGetTodo;
	protected Button btnListToDos;
	protected Button btnPing;
	protected Button btnChangePassword;
	protected Button btnDeleteToDo;

	
	protected Label lblSceneTitel;
	protected Label lblFooter;

	StackPane reg;
	StackPane td;

	
	protected Group register;
	protected Group toDo;

	public Client_View(Stage stage, Client_Model model) {
		super(stage, model);
		ServiceLocator.getServiceLocator().getLogger().info("Client view initialized");

		cmbToDoPriority.getItems().addAll(Tasks_Priority.values());

		t = ServiceLocator.getServiceLocator().getTranslator();
	}

	@Override
	protected Scene create_GUI() {
		lblIP = new Label("IP");
		txtIP = new TextField("127.0.0.1");
		lblPort = new Label("Port");
		txtPort = new TextField("");
		lblClientName = new Label();
		txtClientName = new TextField("");
		ttName = new Tooltip();
		txtClientName.setTooltip(ttName);
		lblPassword = new Label();
		txtPassword = new TextField("");
		ttPassword = new Tooltip();
		txtPassword.setTooltip(ttPassword);

		lblNewPassword = new Label("NewPassword");
		txtNewPassword = new TextField("");

		lblToDoTitel = new Label("Titel");
		txtToDoTitel = new TextField("");
		lblToDoPriority = new Label("Priority");
		cmbToDoPriority = new ComboBox<Tasks_Priority>();
		lblToDoDesc = new Label("Description");
		txtToDoDesc = new TextField("");

		lblToDoId = new Label("ToDoId: #");
		txtToDoId = new TextField("");

		btnRegister = new Button("Register");
		btnLogin = new Button("Login");
		btnCreateToDo = new Button("Create Task");
		btnGetTodo = new Button("Get Task");
		btnListToDos = new Button("List Task");
		btnPing = new Button("Ping");
		btnChangePassword = new Button("ChangePassword");
		btnDeleteToDo = new Button("Delete Task");

		lblSceneTitel = new Label("");
		lblFooter = new Label("");
		txtMessages = new TextArea();

		listView = new ListView<Task>();

		pT = new Label("Change Password");
		n = new Label("E-Mail: ");
		p = new Label("OldPassword: ");
		np = new Label("New password: ");

		tT = new Label("My Tasks");
		ti = new Label("Titel: ");
		pr = new Label("Priority");
		d = new Label("Description");

		reg = new StackPane();
		td = new StackPane();

		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Logger logger = sl.getLogger();

		VBox center = new VBox();

		MenuBar menuBar = new MenuBar();
		menuFile = new Menu();
		menuFile.setId("menuFile");
		menuFileLanguage = new Menu();
		menuFile.getItems().add(menuFileLanguage);

		for (Locale locale : sl.getLocales()) {
			MenuItem language = new MenuItem(locale.getLanguage());
			menuFileLanguage.getItems().add(language);
			language.setOnAction(event -> {
				sl.getConfiguration().setLocalOption("Language", locale.getLanguage());
				sl.setTranslator(new Translator(locale.getLanguage()));
				updateTexts();
			});
		}

		menuLogout = new MenuItem();
		menuProfile = new MenuItem();
		menuPing = new MenuItem();
		menuFile.getItems().add(menuProfile);
		menuFile.getItems().add(menuPing);
		menuFile.getItems().add(menuLogout);
		menuBar.getMenus().add(menuFile);

		// scene
		stage.setTitle("Task Manager");

		root = new GridPane();
		root.setStyle("-fx-padding: 40px;");


		// Server replies
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		scrollPane.setContent(txtMessages);
		txtMessages.setWrapText(true);

		sp = new VBox(scrollPane);
		sp.setVgrow(scrollPane, Priority.ALWAYS);

		
		regBox = new GridPane();
		regBox.setId("TopBox");
		regBox.add(lblIP, 0, 0);
		regBox.add(txtIP, 1, 0);
		regBox.add(lblPort, 0, 1);
		regBox.add(txtPort, 1, 1);
		regBox.add(lblClientName, 0, 2);
		regBox.add(txtClientName, 1, 2);
		regBox.add(lblPassword, 0, 3);
		regBox.add(txtPassword, 1, 3);
		regBox.add(btnRegister, 0, 4);
		regBox.add(btnLogin, 1, 4);
		txtIP.setId("IP");
		txtPort.setId("Port");

		
		VBox rightList = new VBox();
		rightList.getStyleClass().add("listView");
		rightList.getChildren().add(listView);

		
		todoMiddle = new GridPane();
		todoMiddle.add(lblToDoTitel, 0, 0);
		todoMiddle.add(txtToDoTitel, 1, 0);
		todoMiddle.add(lblToDoPriority, 0, 1);
		todoMiddle.add(cmbToDoPriority, 1, 1);
		todoMiddle.add(lblToDoDesc, 0, 2);
		todoMiddle.add(txtToDoDesc, 1, 2);
		todoMiddle.add(btnCreateToDo, 1, 3);
		todoMiddle.add(rightList, 2, 0, 2, 4);



		
		register = new Group(regBox);
		toDo = new Group(todoMiddle);


		
		center.getChildren().add(reg);
		center.getChildren().add(td);

		
		center.prefWidthProperty().bindBidirectional(root.prefWidthProperty());

		updateTexts();

		root.add(menuBar, 0, 0);
		root.add(center, 0, 2);
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("client.css").toExternalForm());
		stage.setScene(scene);
		return scene;
	}

	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();

		// The menu entries
		menuFile.setText(t.getString("program.menu.file"));
		menuFileLanguage.setText(t.getString("program.menu.file.language"));
		menuLogout.setText(t.getString("program.menu.logout"));
		menuProfile.setText(t.getString("program.menu.profile"));
		menuPing.setText(t.getString("program.menu.ping"));

		lblClientName.setText(t.getString("program.label.ClientName"));
		lblPassword.setText(t.getString("program.label.ClientPassword"));
		lblNewPassword .setText(t.getString("program.label.NewPassword"));;
		lblToDoTitel.setText(t.getString("program.label.ToDoTitel"));
		lblToDoPriority.setText(t.getString("program.label.Priority"));
		lblToDoDesc.setText(t.getString("program.label.Description"));

		btnRegister.setText(t.getString("program.button.Register"));
		btnLogin.setText(t.getString("program.button.Login"));
		btnCreateToDo.setText(t.getString("program.button.CreateToDo"));
		btnChangePassword.setText(t.getString("program.buton.ChangePassword"));
		btnDeleteToDo.setText(t.getString("program.button.DeleteToDo"));

		lblFooter.setText(t.getString("program.scene.footer"));

		pT.setText(t.getString("program.profile.page.titel"));
		n.setText(t.getString("program.profile.username"));
		p.setText(t.getString("program.profile.oldpassword"));
		np.setText(t.getString("program.profile.newpassword"));

		tT.setText(t.getString("program.todo.page.title"));
		ti.setText(t.getString("program.todo.title"));
		pr.setText(t.getString("program.todo.priority"));
		d.setText(t.getString("program.todo.description"));

		ttName.setText(t.getString("program.tooltip.username"));
		ttPassword.setText(t.getString("program.tooltip.password"));

		stage.setTitle(t.getString("program.name"));
	}

	public Pane footer() {
		Region spacer1 = new Region();
		HBox.setHgrow(spacer1, Priority.ALWAYS);

		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);

		lblFooter.getStyleClass().add("lblFooter");

		HBox footer = new HBox(spacer1, lblFooter, spacer2);
		return footer;
	}

	public Callback<ListView<Task>, ListCell<Task>> getListViewCellFactory() {
		return new Callback<ListView<Task>, ListCell<Task>>() {

			@Override
			public ListCell<Task> call(ListView<Task> param) {
				return new ListCell<Task>() {
					@Override
					protected void updateItem(Task item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null) {
							setText(null);
						} else {
							setText(item.getTitel());
						}
					}

				};
			}
		};
	}

}
