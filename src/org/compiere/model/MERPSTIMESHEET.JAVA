package org.compiere.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class Merpstimesheet extends X_erps_timesheet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7017574065199198335L;
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (Merpstimesheet.class);

	public Merpstimesheet(Properties ctx, int erps_timesheet_ID, String trxName) {
		super(ctx, erps_timesheet_ID, trxName);
	}
	
	public Merpstimesheet(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * Search for entries in the time sheet
	 * 
	 * @param C_BPartner_ID
	 * @param dt
	 * @return  Model time sheet
	 */
	public static Merpstimesheet getTimeSheet(int C_BPartner_ID, Timestamp dt) {
	
		//
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		Merpstimesheet timeSheet = new Merpstimesheet(Env.getCtx(), 0, null);
			
		// Employee data from the timesheets
		String sql_ = "SELECT * FROM HRM_TIMESHEET " +
						"WHERE ISACTIVE='Y' " +
						"AND AD_CLIENT_ID=? " +
						"AND C_BPARTNER_ID=? " +
						"AND DATEFROM=?";
			try
			{
				pstmt = DB.prepareStatement(sql_,null);
				pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
				pstmt.setInt(2, C_BPartner_ID);
				pstmt.setDate(3, new Date(dt.getTime()));
				rs = pstmt.executeQuery();	
				if(rs.next())
					timeSheet = new Merpstimesheet(Env.getCtx(), rs, null);
			}
			catch (SQLException e)
			{
				s_log.log(Level.SEVERE, "product", e);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}	
		return timeSheet;
		
	}

}
