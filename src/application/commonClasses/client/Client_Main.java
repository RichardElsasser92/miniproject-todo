package application.commonClasses.client;

import javafx.application.Application;
import javafx.stage.Stage;

public class Client_Main extends Application {
	private Client_View view;
	private Client_Controller controller;
	private Client_Model model;

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		model = new Client_Model();
		view = new Client_View(primaryStage, model);
		controller = new Client_Controller(model, view);
		view.start();
		
	}
	
    @Override
    public void stop() {
        if (view != null)
            view.stop();
    }

}
