package org.openhealthtools.openatna.audit.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class AuditLogBackupWriter {
	public static final String AUDIT_BACKUP_DIR = "audit-backup/";
	public static final String AUDIT_LOG_FILE_NAME = "ATNAAuditLogBackup-";
	public static final String AUDIT_LOG_FILE_EXT = ".log";

	private static final Logger LOG = Logger.getLogger(AuditLogBackupWriter.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static long counterValue = 0;
	
	public synchronized static void writeAuditMessageToFile(String am, String facility, String severity) {
		try {
			if(am != null) {
				String path = System.getenv("EPSOS_PROPS_PATH") + File.separatorChar + AUDIT_BACKUP_DIR + AUDIT_LOG_FILE_NAME + 
						getTimeStamp() + "-" + getCounterValue() + AUDIT_LOG_FILE_EXT;
				writeXMLToFileThrows(am, path);
				LOG.error("Error occurred while writing AuditLog to OpenATNA! AuditLog saved to: " + path);
			}
		} catch(Exception e) {
			LOG.fatal("Unable to send AuditLog to OpenATNA nor able write auditLog backup! Dumping to log: " + am, e);
		}
	}

	private static void writeXMLToFileThrows(String am, String path) throws IOException {
		BufferedWriter out = null;
		FileWriter fstream = null;
		try {
			fstream = new FileWriter(path);
			out = new BufferedWriter(fstream);
			out.write(am);
		} finally {
			try {
				if(out != null) {
					out.close();
				}
			} catch(IOException e) {
				LOG.warn("Unable to close BufferedWriter.");
			}
			
			try {
				if(fstream != null) {
					fstream.close();
				}
			} catch(IOException e) {
				LOG.warn("Unable to close FileWriter.");
			}
		}
	}

	private synchronized static long getCounterValue() {
		if(counterValue > 9999) {
			counterValue = 0;
		}
		return ++counterValue;
	}
	
	private static String getTimeStamp() {
		return sdf.format(new Date());
	}
}
