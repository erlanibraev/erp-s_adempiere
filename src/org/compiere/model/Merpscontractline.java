package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class Merpscontractline extends X_erps_contractline {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 959981802963776832L;

	public Merpscontractline(Properties ctx, int erps_contractline_ID,
			String trxName) {
		super(ctx, erps_contractline_ID, trxName);
	}

	public Merpscontractline(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}



}
