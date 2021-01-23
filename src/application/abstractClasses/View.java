package application.abstractClasses;

import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class View<M extends Model> {
    protected Stage stage;
    protected Scene scene;
    protected M model;
    
    /**
     * Set any options for the stage in the subclass constructor
     * 
     * @param stage
     * @param model
     */
    protected View(Stage stage, M model) {
        this.stage = stage;
        this.model = model;
        
        scene = create_GUI(); // Create all controls within "root"
        stage.setScene(scene);
    }

    protected abstract Scene create_GUI();

    /**
     * Display the view
     */
    public void start() {
        stage.show();
    }
    
    /**
     * Hide the view
     */
    public void stop() {
        stage.hide();
    }
    
    /**
     * Getter for the stage, so that the controller can access window events
     */
    public Stage getStage() {
        return stage;
    }
}
