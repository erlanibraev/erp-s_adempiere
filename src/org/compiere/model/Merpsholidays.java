package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class Merpsholidays extends X_erps_holidays {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8757711305165014221L;


	public Merpsholidays(Properties ctx, int erps_holidays_ID, String trxName) {
		super(ctx, erps_holidays_ID, trxName);
	}


	public Merpsholidays(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}


}
