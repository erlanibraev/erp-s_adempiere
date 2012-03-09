package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MerpsrecTransDocSigned extends X_erps_recTransDocSigned {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2092270529942796319L;

	public MerpsrecTransDocSigned(Properties ctx,
			int erps_recTransDocSigned_ID, String trxName) {
		super(ctx, erps_recTransDocSigned_ID, trxName);
	}

	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MerpsrecTransDocSigned (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MerpsrecTransDocSigned
	
}
