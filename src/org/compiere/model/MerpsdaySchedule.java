package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpsdaySchedule extends X_erps_daySchedule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6551140231618490174L;

	public MerpsdaySchedule(Properties ctx, int erps_daySchedule_ID,
			String trxName) {
		super(ctx, erps_daySchedule_ID, trxName);
	}

	public MerpsdaySchedule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}



}
