package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpsworkSchedule extends X_erps_workSchedule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901350177263541435L;

	public MerpsworkSchedule(Properties ctx, int erps_workSchedule_ID,
			String trxName) {
		super(ctx, erps_workSchedule_ID, trxName);
	}

	public MerpsworkSchedule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}


}
