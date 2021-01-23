package application.appClasses;

public class User {
	public static final String PROPERTY_SEPERATOR = ";";
	
	private String Id;
	private String username;
	private String password;
	
	public static int highestID = -1;
	
	protected static String getNextId() {
		++highestID;
		return "#0" + Integer.toString(highestID);
	}
	
	public User(String username, String password) {
		Id = getNextId();
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return String.join(PROPERTY_SEPERATOR, Id, username, password);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return Id;
	}
	public void setId(String id) {
		this.Id = id;
	}
	
	public static void setHighestId(int id) {
		highestID = id;
	}
}
