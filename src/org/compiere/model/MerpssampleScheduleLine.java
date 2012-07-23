package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpssampleScheduleLine extends X_erps_sampleScheduleLine {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6633304451303750302L;

	public MerpssampleScheduleLine(Properties ctx,
			int erps_sampleScheduleLine_ID, String trxName) {
		super(ctx, erps_sampleScheduleLine_ID, trxName);
	}

	public MerpssampleScheduleLine(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}



}
