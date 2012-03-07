package org.compiere.model;

import java.util.Properties;

public class MerpsreceptTransmissionDoc extends X_erps_receptTransmissionDoc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 521478963L;

	public MerpsreceptTransmissionDoc(Properties ctx,
			int erps_receptTransmissionDoc_ID, String trxName) {
		super(ctx, erps_receptTransmissionDoc_ID, trxName);
	}
	

	protected boolean beforeSave(boolean newRecord) {
		// 
		return super.beforeSave(newRecord);
	}

}
