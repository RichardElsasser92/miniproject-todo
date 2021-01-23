package application.commonClasses.messages.serverReplies;

import java.util.ArrayList;

public class Result_Error extends ResultMsg {
	private static final String ERROR = "error";
	
	private String error;
	
	public Result_Error() {
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
