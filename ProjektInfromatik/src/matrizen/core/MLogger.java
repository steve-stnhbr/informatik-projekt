package matrizen.core;

import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MLogger {
	public static final String timePrefix = "{%time%}";
	
	private String prefix;
	public Logger logger;
	
	public MLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setPrefix(String s) {
		this.prefix = s;
	}
	
	public void log(Level l, String msg) {
		logger.log(l, prefix.replace("{%time%}", new GregorianCalendar().toString()) + msg);
	}

}
