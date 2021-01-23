package application.appClasses;


import application.abstractClasses.Task;
import application.commonClasses.Tasks_Priority;

// Tasks without due dates...

public class Tasks extends Task {

	public Tasks(String userId, String titel, String priority, String description) {
		super(userId, titel, priority, description);
		
	}

}
