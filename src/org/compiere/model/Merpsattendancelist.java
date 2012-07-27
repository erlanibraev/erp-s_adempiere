package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class Merpsattendancelist extends X_erps_attendancelist {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8412573029358779567L;

	public Merpsattendancelist(Properties ctx, int erps_attendancelist_ID,
			String trxName) {
		super(ctx, erps_attendancelist_ID, trxName);
	}

	public Merpsattendancelist(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}



}
