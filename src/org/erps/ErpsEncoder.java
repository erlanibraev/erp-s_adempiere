package org.erps;

import java.util.logging.Level;

import org.apache.commons.codec.EncoderException;
import org.compiere.util.CLogger;

public class ErpsEncoder {
	
	public static final String  ENCODING = "UTF-8";
	protected CLogger	log = 		CLogger.getCLogger (getClass());	
	public ErpsEncoder() {}
	
	public String encodeUTF8(String value) throws EncoderException {
		String returnValue = new String("");
		try {
			returnValue = new String(value.getBytes(), ENCODING);
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		}	
		return returnValue;
	}

}
