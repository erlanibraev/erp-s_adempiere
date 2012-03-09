package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.util.CLogger;

/**
 * receptTransmissionDoc Model.
 * Model for working with the act of receiving the transfer
 * @author V.Sokolov, Ltd. "ERP-Service "KazTransCom"
 * @version $Id: MerpsreceptTransmissionDoc.java, v 1.0 2012/03/07
 */
public class MerpsreceptTransmissionDoc extends X_erps_receptTransmissionDoc {
	
	/**	ReceptTransmissionDoc Lines	*/
	private MerpsrecTransDocLine[] m_lines;
	/** ReceptTransmissionDoc Signed */
	private MerpsrecTransDocSigned[] m_signed;
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MerpsreceptTransmissionDoc.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -5658764569646667273L;

	/**
	 * 	receptTransmissionDoc Constructor
	 * 	@param ctx context
	 * 	@param erps_receptTransmissionDoc_ID means or 0 for new
	 * 	@param trxName trx name
	 */
	public MerpsreceptTransmissionDoc(Properties ctx, int erps_receptTransmissionDoc_ID, String trxName) {
		super(ctx, erps_receptTransmissionDoc_ID, trxName);
	}
	
	/**
	 * 	Load Cosntructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MerpsreceptTransmissionDoc (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MCashLine
	

	/**
	 * 	Get ReceptTransmissionDoc Lines of ReceptTransmissionDoc
	 * 	@param whereClause starting with AND
	 * 	@return lines
	 */
	private MerpsrecTransDocLine[] getLines (String whereClause)
	{
		String whereClauseFinal = "erps_receptTransmissionDoc_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MerpsrecTransDocLine> list = new Query(getCtx(), I_erps_recTransDocLine.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(geterps_receptTransmissionDoc_ID())
										.list();
		return list.toArray(new MerpsrecTransDocLine[list.size()]);
	}	//	getLines

	/**
	 * 	Get ReceptTransmissionDoc Lines
	 * 	@param requery
	 * 	@return lines
	 */
	public MerpsrecTransDocLine[] getLines (boolean requery)
	{
		if (m_lines == null || m_lines.length == 0 || requery)
			m_lines = getLines(null);
		set_TrxName(m_lines, get_TrxName());
		return m_lines;
	}	//	getLines

	/**
	 * 	Get Lines of ReceptTransmissionDoc
	 * 	@return lines
	 */
	public MerpsrecTransDocLine[] getLines()
	{
		return getLines(false);
	}	//	getLines
	
	/**
	 * 	Get ReceptTransmissionDoc Signed of ReceptTransmissionDoc
	 * 	@param whereClause starting with AND
	 * 	@return signed
	 */
	private MerpsrecTransDocSigned[] getSigned (String whereClause)
	{
		String whereClauseFinal = "erps_receptTransmissionDoc_ID=? ";
		if (whereClause != null)
			whereClauseFinal += whereClause;
		List<MerpsrecTransDocSigned> list = new Query(getCtx(), I_erps_recTransDocSigned.Table_Name, whereClauseFinal, get_TrxName())
										.setParameters(geterps_receptTransmissionDoc_ID())
										.list();
		return list.toArray(new MerpsrecTransDocSigned[list.size()]);
	}	//	getSigned
	
	/**
	 * 	Get ReceptTransmissionDoc Signed
	 * 	@param requery
	 * 	@return signed
	 */
	public MerpsrecTransDocSigned[] getSigned (boolean requery)
	{
		if (m_signed == null || m_signed.length == 0 || requery)
			m_signed = getSigned(null);
		set_TrxName(m_signed, get_TrxName());
		return m_signed;
	}	//	getSigned
	
	/**
	 * 	Get Lines of ReceptTransmissionDoc
	 * 	@return signed
	 */
	public MerpsrecTransDocSigned[] getSigned()
	{
		return getSigned(false);
	}	//	getSigned
	
	protected boolean beforeSave(boolean newRecord) {
		// check for a match fields
		if(geterps_fromResponsible() == geterps_toResponsible()){
			throw new AdempiereException("Field 'From' and 'Who' are the same. From = Who");
		}
		
		return true;
	}

}
