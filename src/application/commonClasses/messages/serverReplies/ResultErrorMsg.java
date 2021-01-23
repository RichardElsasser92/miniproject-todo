package application.commonClasses.messages.serverReplies;

import java.util.ArrayList;

public class ResultErrorMsg extends ResultMsg {
	private static final String ERROR = "error";
	
	private String error;
	
	public ResultErrorMsg() {
		super();
	}

	@Override
	protected void receiveAttributes(ArrayList<NameValue> pairs) {
		this.error = findAttributes(pairs, ERROR);
	}

	@Override
	protected void sendAttributes(ArrayList<NameValue> pairs) {
		pairs.add(new NameValue(ERROR, this.error));
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
