package application.commonClasses.messages.serverReplies;

public class ResultLoginMsg extends ResultMsg {
	private String reply;

	public void setRep() {
		result = reply + "|" + token;

	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

}
