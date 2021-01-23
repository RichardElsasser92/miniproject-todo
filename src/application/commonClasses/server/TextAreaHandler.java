package application.commonClasses.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class TextAreaHandler extends Handler {

	private TextArea textArea = new TextArea();
	
	public void publish(LogRecord record) {
        Platform.runLater(new Runnable() {
            @Override public void run() {        
                StringWriter text = new StringWriter();
                PrintWriter out = new PrintWriter(text);
                out.println(textArea.getText());
                out.printf("[%s] [Thread-%d]: %s.%s -> %s", record.getLevel(),
                        record.getThreadID(), record.getSourceClassName(),
                        record.getSourceMethodName(), record.getMessage());
                textArea.setText(text.toString());
            }});
	
}
	public void flush() {
        // nothing to do here
    }
	
	public void close() throws SecurityException {
        // nothing to do here
    }
	 public TextArea getTextArea() {
	        return textArea;
	    }
} 