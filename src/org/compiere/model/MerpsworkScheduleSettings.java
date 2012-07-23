package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpsworkScheduleSettings extends X_erps_workScheduleSettings {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -654290798339107706L;

	public MerpsworkScheduleSettings(Properties ctx,
			int erps_workScheduleSettings_ID, String trxName) {
		super(ctx, erps_workScheduleSettings_ID, trxName);
	}

	public MerpsworkScheduleSettings(Properties ctx, ResultSet rs,
			String trxName) {
		super(ctx, rs, trxName);
	}



}
