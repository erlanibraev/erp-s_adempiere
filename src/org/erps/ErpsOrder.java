package org.erps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.codec.EncoderException;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.GridWindow;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class ErpsOrder extends CalloutEngine {
	
	private ErpsEncoder encoder = new ErpsEncoder();	
	private static final int orderTypeHiring = 9; 		// о приеме на работу
	private static final int orderTypeTransfer = 3; 	// о переводе
	private String msg = "";
	
	public String bpartner(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{
		
		if(value == null) return "";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int orderType = 0;
		
		// KNOW THE ORDER TYPE
		String sql_ = "select t.erps_orderType from erps_order t where t.erps_order_id=?";
		try
		{
			pstmt = DB.prepareStatement(sql_, null);
			pstmt.setInt(1, Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "erps_order_ID"));
			rs = pstmt.executeQuery();
			if (rs.next())
				orderType = rs.getInt(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "product", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}	
		
		// Transfer officer
		if(orderType == orderTypeTransfer){
			fromJob(ctx, WindowNo, mTab, mField, value);
			fromCategory(ctx, WindowNo, mTab, mField, value);
			fromSector(ctx, WindowNo, mTab, mField, value);
		}
		// Hiring officer
		if(orderType == orderTypeHiring){
			toJob(ctx, WindowNo, mTab, mField, value);
			toCategory(ctx, WindowNo, mTab, mField, value);
			toSector(ctx, WindowNo, mTab, mField, value);
		}
		
		return msg;
	}
	
	private void fromJob (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{

		job(ctx, WindowNo, mTab, mField, value, "erps_fromjob");
	}
	
	private void toJob (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{

		job(ctx, WindowNo, mTab, mField, value, "erps_tojob");
	}
	
	private void fromCategory (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{

		category(ctx, WindowNo, mTab, mField, value, "erps_fromcategory");
	}
	
	private void toCategory (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{

		category(ctx, WindowNo, mTab, mField, value, "erps_tocategory");
	}
	
	private void fromSector (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{

		sector(ctx, WindowNo, mTab, mField, value, "erps_fromsector");
	}
	
	private void toSector (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws SQLException, EncoderException{

		sector(ctx, WindowNo, mTab, mField, value, "erps_tosector");
	}
	
	/**
	 * Sets Job Officer in the table ERPS_ORDER
	 * 
	 * @param ctx	 	Context
	 * @param WindowNo	Current Window No
	 * @param mTab		Model Tab
	 * @param mField 	Model Field
	 * @param value  	The new value
	 * @return		 	Exception if there is
	 */
	private String job (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, String columnNameUpd) throws SQLException, EncoderException
	{
		// 
		if(value == null)  return "";
		
		long job_id = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// KNOW THE POST
		String sql_ = "select r.hr_job_id from c_bpartner t \n"+ 
				      " left join hr_employee r on r.c_bpartner_id=t.c_bpartner_id \n"+
				      " where t.c_bpartner_id=?";
		try
		{
			pstmt = DB.prepareStatement(sql_, null);
			pstmt.setObject(1, value);
			rs = pstmt.executeQuery();
			if (rs.next())
				job_id = rs.getLong(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "product", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}	
	
		if(job_id != 0)
			msg = updRec(ctx, WindowNo, mTab, columnNameUpd, job_id);
		
		return msg;
	}
	
	/**
	 * Sets Category Officer in the table ERPS_ORDER
	 * 
	 * @param ctx	 		 Context
	 * @param WindowNo		 Current Window No
	 * @param mTab			 Model Tab
	 * @param mField 		 Model Field
	 * @param value  		 The new value
	 * @param columnNameUpd	 Column name update
	 * @return				 Exception if there is
	 */
	private String category (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, String columnNameUpd) throws EncoderException, SQLException{
		
		// 
		if(value == null)  return "";
		
		int category_id = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// KNOW THE POST
		String sql_ = "select r.erps_jobcategory from c_bpartner t \n"+ 
				      " left join hr_employee r on r.c_bpartner_id=t.c_bpartner_id \n"+
				      " where t.c_bpartner_id=?";
		try
		{
			pstmt = DB.prepareStatement(sql_, null);
			pstmt.setObject(1, value);
			rs = pstmt.executeQuery();
			if (rs.next())
				category_id = rs.getInt(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "product", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}	
		
		if(category_id != 0) 
			msg = updRec(ctx, WindowNo, mTab, columnNameUpd, category_id);;
		
		return msg;
	}
	
	private String sector(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value, String columnNameUpd) throws EncoderException, SQLException 
	{
		// 
		if(value == null)  return "";
		
		int sector_id = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// KNOW THE POST
		String sql_ = "select r.erps_sector_id from c_bpartner t \n"+ 
				      " left join hr_employee r on r.c_bpartner_id=t.c_bpartner_id \n"+
				      " where t.c_bpartner_id=?";
		try
		{
			pstmt = DB.prepareStatement(sql_, null);
			pstmt.setObject(1, value);
			rs = pstmt.executeQuery();
			if (rs.next())
				sector_id = rs.getInt(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "product", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}			
		
		if(sector_id != 0) 
			msg = updRec(ctx, WindowNo, mTab, columnNameUpd, sector_id);
		
		return msg;
	}
	
	private String updRec(Properties ctx, int WindowNo, GridTab mTab, String columnNameUpd, Object value) throws EncoderException, SQLException{
		
		PreparedStatement pstmt = null;
		
		// HOME TAB ORDERS
		GridTab tab = bookmark(ctx,WindowNo,mTab,"erps_order");
		
		// update the field "String columnNameUpd" with a value of "Object value"
		StringBuilder sql = new StringBuilder("update ");
		sql.append(tab.getTableName()).append(" set ");
		sql.append(columnNameUpd).append("=?");
		sql.append(" where ").append(Env.getContext(ctx, WindowNo, tab.getTabNo(), "_TabInfo_KeyColumnName")+"=?");
		//
		pstmt = DB.prepareStatement(sql.toString(), null);
		pstmt.setObject(1, value);
		pstmt.setInt(2, Env.getContextAsInt(ctx, WindowNo, mTab.getTabNo(), "erps_order_ID"));
		try{
			pstmt.execute();
		}catch(SQLException e){
			msg = encoder.encodeUTF8("Error when updating order: " + e.getMessage());
		}finally{
			pstmt.close();
			pstmt = null;
		}
		return msg;
	}
	
	/*
	 * Returns a tab
	 */
	private GridTab bookmark(Properties ctx, int WindowNo, GridTab mTab, String table){
		
		//
		GridWindow win = GridWindow.get(ctx, WindowNo, mTab.getAD_Window_ID());
		GridTab tab = null;
		for(int i=0; i<win.getTabCount();i++){
			if(win.getTab(i).getTableName().equalsIgnoreCase(table)){
				tab = win.getTab(i);
				break;
			}
		}
		return tab;
	} 

}
