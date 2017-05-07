package io.github.alivety.logging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AlivetyLogging {
	public static final int CONSOLE=0;
	public static final int LOGFILE=1;
	
	private static final SimpleDateFormat title_format=new SimpleDateFormat("yyyyMMddHmss");
	private static final SimpleDateFormat msg_format=new SimpleDateFormat("d MMMMM yyyy h:m:s a");
	private static final Formatter formatter=new Formatter(){
		@Override
		public String format(LogRecord record) {
			StringBuilder sb=new StringBuilder(msg_format.format(new Date())).append(" ").append(record.getLoggerName()).append(" ").append("[").append("Thread "+record.getThreadID()).append("]").append("\r\n");
			if (record.getThrown()==null) {
				sb.append(record.getSourceClassName()).append(" ").append(record.getSourceMethodName()).append("\r\n");
				sb.append(record.getLevel()).append(" ").append(record.getMessage()).append("\r\n\r\n");
			} else {
				Throwable thrown=record.getThrown();
				StackTraceElement[] stacktrace=thrown.getStackTrace();
				sb.append(record.getSourceClassName()).append(" ").append(record.getSourceMethodName()).append("\r\n");
				sb.append(record.getLevel()).append(" ").append(record.getMessage()).append("\r\n");
				sb.append(thrown.toString()).append("\r\n");
				for (final StackTraceElement element : stacktrace) {
					sb.append("\t" + element).append("\r\n");
				}
				if (thrown.getCause() != null) {
					sb.append("Caused by: " + thrown.getCause().toString()).append("\r\n");
					final StackTraceElement[] stackTrace1 = thrown.getCause().getStackTrace();
					for (final StackTraceElement element : stackTrace1) {
						sb.append("\t" + element).append("\r\n");
					}
				}
			}
			
			return sb.toString();
		}};
	
	public static Logger getLogger(String name,int... options) throws SecurityException, IOException {
		Date d=new Date();
		Logger log=Logger.getLogger(name);
		log.setUseParentHandlers(false);
		for(Handler handler : log.getHandlers()) {
		    log.removeHandler(handler);
		}
		
		for (int opt : options) {
			if (opt==CONSOLE) {
				Handler h=new ConsoleHandler();
				h.setFormatter(formatter);
				log.addHandler(h);
			} else if (opt==LOGFILE) {
				Handler h=new FileHandler(title_format.format(d)+"-"+log.getName());
				h.setFormatter(formatter);
				log.addHandler(h);
			}
		}
		
		return log;
	}
}
