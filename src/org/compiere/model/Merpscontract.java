package org.compiere.model;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.compiere.util.DB;

public class Merpscontract extends X_erps_contract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3864218080122828176L;
	/**	Cached PL			*/
	private int		m_M_PriceList_ID = 0;

	public Merpscontract(Properties ctx, int erps_contract_ID, String trxName) {
		super(ctx, erps_contract_ID, trxName);
	}

	public Merpscontract(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 	Get Price List from Price List Version
	 *	@return price list or 0
	 */
	public int getM_PriceList_ID()
	{
		if (getM_PriceList_Version_ID() == 0)
			return 0;
		if (m_M_PriceList_ID > 0)
			return m_M_PriceList_ID;
		//
		String sql = "SELECT M_PriceList_ID FROM M_PriceList_Version WHERE M_PriceList_Version_ID=?";
		m_M_PriceList_ID = DB.getSQLValue(null, sql, getM_PriceList_Version_ID());
		return m_M_PriceList_ID;
	}	//	getM_PriceList_ID

	/**
	 * 	Set PL Version
	 *	@param M_PriceList_Version_ID id
	 */
	public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
	{
		super.setM_PriceList_Version_ID(M_PriceList_Version_ID);
		m_M_PriceList_ID = 0;	//	reset
	}	//	setM_PriceList_Version_ID
	
	/**************************************************************************
	 * 	Get Contract Lines
	 *	@return Array of lines
	 */
	public Merpscontractline[] getLines()
	{
		//FR: [ 2214883 ] Remove SQL code and Replace for Query - red1
		final String whereClause = "erps_contract_ID=?";
		List <Merpscontractline> list = new Query(getCtx(), I_erps_contractline.Table_Name, whereClause, get_TrxName())
			.setParameters(geterps_contract_ID())
			.setOrderBy("erps_contractLine_ID")
			.list();
		//
		Merpscontractline[] retValue = new Merpscontractline[list.size()];
		list.toArray(retValue);
		return retValue;
	}	//	getLines

}
