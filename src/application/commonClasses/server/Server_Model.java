package application.commonClasses.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

public class Server_Model {

	private Integer port;
	
	private final Logger logger = Logger.getLogger("");
	
	final Task<Void> serverTask = new Task<Void>() {
		
		protected Void call() throws Exception {
			ServerSocket listener = null;
			try {
				listener = new ServerSocket(port, 10, null);
				logger.info("Listening on port " + port);
				
				while (true) {
					Socket clientSocket = listener.accept();
					
					Server_ThreadForClient client = new Server_ThreadForClient(clientSocket);
					client.start();
					}
			}catch (Exception e) {
				System.err.println(e);			
		} finally {
			if (listener != null) listener.close();
		}
		return null;	
	}
	};
	
	public void startServerSocket(Integer port) {
		this.port = port;
		new Thread(serverTask).start();
		logger.info("Port is: " + port);
	}
}
