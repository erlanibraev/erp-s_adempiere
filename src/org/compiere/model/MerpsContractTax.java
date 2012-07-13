package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpsContractTax extends X_erps_ContractTax {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7180224908479650931L;

	public MerpsContractTax(Properties ctx, int erps_ContractTax_ID,
			String trxName) {
		super(ctx, erps_ContractTax_ID, trxName);
	}

	public MerpsContractTax(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}



}
