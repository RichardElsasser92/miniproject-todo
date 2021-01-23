package application.commonClasses.server;

import javafx.application.Application;
import javafx.stage.Stage;

public class Server extends Application {
	private Server_View view;
	private Server_Controller controller;
	private Server_Model model; 
	

	public static void main(String[] args) {
		launch(args);

	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new Server_Model();
		model.startServerSocket(50002);
		controller = new Server_Controller();
	}

}
