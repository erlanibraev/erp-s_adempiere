package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.compiere.util.CLogger;

public class MerpsrecTransDocLine extends X_erps_recTransDocLine {
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MerpsrecTransDocLine.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 724007600723089000L;

	public MerpsrecTransDocLine(Properties ctx, int erps_recTransDocLine_ID,
			String trxName) {
		super(ctx, erps_recTransDocLine_ID, trxName);
	}
	
	/**
	 *  Load Constructor
	 *  @param ctx context
	 *  @param rs result set record
	 *  @param trxName transaction
	 */
	public MerpsrecTransDocLine (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MerpsrecTransDocLine
	
	/**
	 * 	Parent Constructor
	 * 	@param ReceptTransmissionDoc parent
	 */
	public MerpsrecTransDocLine (MerpsreceptTransmissionDoc TransmissionDoc)
	{
		this (TransmissionDoc.getCtx(), 0, TransmissionDoc.get_TrxName());
		if (TransmissionDoc.get_ID() == 0)
			throw new IllegalArgumentException("Header not saved");
		setClientOrg(TransmissionDoc.getAD_Client_ID(), TransmissionDoc.getAD_Org_ID());
		seterps_receptTransmissionDoc_ID(TransmissionDoc.geterps_receptTransmissionDoc_ID());
	}	//	MerpsrecTransDocLine

}
