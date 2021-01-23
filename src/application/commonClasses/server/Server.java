package application.commonClasses.server;

import javafx.application.Application;
import javafx.stage.Stage;

public class Server extends Application {
	private ServerView view;
	private ServerController controller;
	private ServerModel model; 
	

	public static void main(String[] args) {
		launch(args);

	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new ServerModel();
		model.startServerSocket(50002);
		controller = new ServerController();
	}

}
