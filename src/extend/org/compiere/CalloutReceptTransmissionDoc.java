package extend.org.compiere;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAsset;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Callout to work with the module AA, table erps_receptTransmissionDoc ...
 * @author V.Sokolov
 * @version $Id: CalloutReceptTransmissionDoc.java, v 1.0 2012/03/07
 */
public class CalloutReceptTransmissionDoc extends CalloutEngine {
	
	private String msg = "";
	
	/*
	 * default Date (Sysdate)
	 */
	public void defaultValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		if(value != null)
			return;
		Date d = new Date();
		mTab.setValue("DateDoc", new Timestamp(d.getTime()));
	}
	
	/*
	 * 
	 */
	public String setDescription(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		Integer assetId = (Integer) value;
		if(assetId == null)
			return msg = "Not found the main means of";
		
		MAsset as = new MAsset(ctx, assetId, null);
		mTab.setValue("Description", as.getValue()+ ",   "+ as.getName());
		
		return msg;
	}
	
	/*
	 * 
	 */
	public void setAccountant(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		int hr_job_id = 1000010;  //Default chief accountant
		int c_bpartner_id = 0;
		String sql_ = "select c_bpartner_id from adempiere.hr_employee where hr_job_id=?"+ 
					 " and ad_client_id=? and isactive='Y' order by updated desc";
		PreparedStatement pstmt = null;
		ResultSet rsDT = null;
		try
		{
			pstmt = DB.prepareStatement(sql_, null);
			pstmt.setInt(1, hr_job_id);
			pstmt.setInt(2, Env.getAD_Client_ID(ctx));
			rsDT = pstmt.executeQuery();
			if (rsDT.next())
			{
				c_bpartner_id = rsDT.getInt(1);
			}
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql_, e);
		}
		finally
		{
			DB.close(rsDT, pstmt);
			rsDT = null; 
			pstmt = null;
		}
		if(c_bpartner_id != 0)
			mTab.setValue("erps_Accountant_ID", c_bpartner_id);
		
	}

}
