package application.commonClasses.fileManager;

import application.appClasses.Tasks;

public class Tasks_FM extends File_Manager {


	@Override
	public String getPropertySeparator() {
		return ";";
	}
	
	public Tasks_FM() {
		super("Tasks.txt");
	}

	@Override
	protected Tasks fromProperties(String[] properties) {
		Tasks task = null;
		if(properties.length == 5) {
			task = new Tasks(properties[1],
					properties[2],
					properties[3],
					properties[4]);
			task.setId(properties[0]);
		}
		return task;
	}

}
