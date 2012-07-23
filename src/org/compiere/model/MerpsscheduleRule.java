package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpsscheduleRule extends X_erps_scheduleRule {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 80942553393241011L;

	public MerpsscheduleRule(Properties ctx, int erps_scheduleRule_ID,
			String trxName) {
		super(ctx, erps_scheduleRule_ID, trxName);
	}

	public MerpsscheduleRule(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}



}
