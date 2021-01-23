package application.commonClasses.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import application.appClasses.User;
import application.commonClasses.fileManager.User_FM;
import application.commonClasses.messages.Login;
import application.commonClasses.messages.Message;

public class ServerController {
	
	private static Map<String, String> sessions = new HashMap<>();
	
	public static String login(Login message) {
		
		String userId = null;
		
		String m = message.toString();
		
		String[] split = m.split("\\n");
		String[] s1 = split[2].split("\\|");
		String[] s2 = split[3].split("\\|");
		String name = s1[1];
		String password = s2[1];
		
		ArrayList<User> users = new ArrayList<User>();
		users = getAllUsers();
		
		for(User user : users) {
			if(user.getUsername().equals(name) && user.getPassword().equals(password)) {
				userId = user.getId();
				break;
			}
		}

		String token = randomToken();
		
		sessions.put(token, userId);
		
		return token;
	}
	
	/**
	 * Return userId if a session with the token of given message exists
	 * 
	 * @param message
	 * @return userId or null if no session for given token exists
	 */
	public static String getUserId(Message message) {
		String token =  message.getToken();
		
		return sessions.get(token);
	}
	
	public static void removeToken(String token) {
		String uid = sessions.remove(token);
	}
	
	public static boolean validToken(String token) {
		boolean valid = false;
		if(sessions.containsKey(token)) {
			valid = true;
		}
		return valid;
	}
	
	public static String randomToken() {
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789+*%&?!£$";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		int length = 20;

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(alphabet.length());
			char randomChar = alphabet.charAt(index);

			sb.append(randomChar);
		}

		String randomString = "#" + sb.toString();
		return randomString;
	}
	
	public static ArrayList<User> getAllUsers() {
		User_FM fileManager = new User_FM();
		return (ArrayList<User>) fileManager.getAll();
	}

}
