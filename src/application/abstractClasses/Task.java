package application.abstractClasses;

import java.util.UUID;

import application.commonClasses.fileManager.Tasks_FM;

public abstract class Task {
	
	private String id;
	private String titel;
	private String priority;
	private String description;
	private String userId;
	
	protected static String getNextId() {
		UUID uuid = java.util.UUID.randomUUID();
		return uuid.toString();
	}

	public Task(String userId, String titel, String priority, String description) {
		id = getNextId();
		this.userId = userId;
		this.titel = titel;
		this.priority = priority;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return String.join((new Tasks_FM()).getPropertySeparator(), id, userId, titel, priority, description);
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setUserId(String id) {
		this.userId = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
}
