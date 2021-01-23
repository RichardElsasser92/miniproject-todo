package application.commonClasses.messages.serverReplies;

import java.util.ArrayList;

public class Result_Ping extends Result_Message {
	private String reply;

	
	public void setRep() {
		if(token == null) {
			result = reply;
		}
		else if (token != null) {
			result = reply + "|" + token;
		}
		
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}
	
	
}
