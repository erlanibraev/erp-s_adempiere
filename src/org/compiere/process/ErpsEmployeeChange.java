package org.compiere.process;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.erps.ErpsEncoder;



public class ErpsEmployeeChange extends SvrProcess {

	private int	p_erp_C_Order_ID;
	private String	p_erp_toJob_ID;
	private String	p_erp_toCategory_Val;
	private int	p_erp_C_BPartner_ID;
	private String	p_erp_OrderType;
	private String	p_erp_OrderNum;
	private Timestamp	p_erp_DateFrom;
	private String  p_contrDesc;
	private String finalMsg;
	private ErpsEncoder encoder = new ErpsEncoder();
	
	public static final String  ENCODING = "UTF-8";
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)	{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null); 
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		// called from order window w/o parameters
//		if ( getTable_ID() == MOrder.Table_ID && getRecord_ID() > 0 )
		p_erp_C_Order_ID = getRecord_ID();

	}

	@Override
	protected String doIt() throws Exception {
		//	Order Lines with a Product which has a current vendor 
		String sql_main_rec = "SELECT * "
			+ "FROM erps_order "
			+ "WHERE erps_order_id =" + p_erp_C_Order_ID;
		String sql_empl_rec = "SELECT c_bpartner_id "
				+ "FROM erps_employee "
				+ "WHERE erps_order_id =" + p_erp_C_Order_ID;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql_main_rec, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				p_erp_toJob_ID = rs.getString("erps_toJob"); 
				p_erp_toCategory_Val = rs.getString("erps_toCategory"); 
				p_erp_OrderType = rs.getString("erps_ordertype");
				p_erp_OrderNum = rs.getString("value");
				p_erp_DateFrom = rs.getTimestamp("datefrom");
			}
			pstmt = null;
			rs = null;
			pstmt = DB.prepareStatement(sql_empl_rec, get_TrxName());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				p_erp_C_BPartner_ID = rs.getInt("c_bpartner_id");
			}
			if (p_erp_OrderType != null && p_erp_OrderType.equals("2")) {
				if (p_erp_C_BPartner_ID != 0 && p_erp_toJob_ID != null 
						&& p_erp_toCategory_Val != null) {
					int p_toJob_ID = new Integer(p_erp_toJob_ID).intValue();
					String sql_empl_upd = "UPDATE hr_employee "
							+ "set hr_job_id =" + p_toJob_ID
							+ ", erps_jobcategory =" + p_erp_toCategory_Val
							+ " WHERE c_bpartner_id =" + p_erp_C_BPartner_ID;
					pstmt = null;
					pstmt = DB.prepareStatement(sql_empl_upd, get_TrxName());
					pstmt.execute();
					log.log(Level.SEVERE, p_erp_OrderType);
					finalMsg = encoder.encodeUTF8("Бизнес партнер № " + new Integer(p_erp_C_BPartner_ID).toString() + " изменён...");
				}		
			} else if (p_erp_OrderType != null && p_erp_OrderType.equals("4")) {
				if (p_erp_C_BPartner_ID != 0) {
					String sql_contr_rec = "SELECT description "
							+ "FROM hr_contract "
							+ "WHERE c_bpartner_id =" + p_erp_C_BPartner_ID;
					pstmt = null;
					rs = null;
					pstmt = DB.prepareStatement(sql_contr_rec, get_TrxName());
					rs = pstmt.executeQuery();
					while (rs.next()) {
						p_contrDesc = rs.getString("description");
					}					
					if (p_contrDesc != null) {
						p_contrDesc = encoder.encodeUTF8(p_contrDesc + "; Приказ № " + p_erp_OrderNum + " от " + p_erp_DateFrom.toString()); 
					} else {
						if (p_erp_DateFrom != null) {
							p_contrDesc = encoder.encodeUTF8("Приказ №" + p_erp_OrderNum + " от " + p_erp_DateFrom.toString());
						}	
					}

					String sql_contr_upd = "UPDATE hr_contract "
							+ "set description ='" + p_contrDesc + "'"
							+ ", erps_signoff ='Y', validto=?"
							+ " WHERE c_bpartner_id =" + p_erp_C_BPartner_ID;
					pstmt = null;
					pstmt = DB.prepareStatement(sql_contr_upd, get_TrxName());
					pstmt.setTimestamp(1, p_erp_DateFrom);
					pstmt.execute();
					log.log(Level.SEVERE, p_erp_OrderType);
					finalMsg = encoder.encodeUTF8("Контракт сотрудника № " + new Integer(p_erp_C_BPartner_ID).toString() + " изменён...");
				} 
				
			} else {
				finalMsg = encoder.encodeUTF8("Никаких действий не произошло...");
			}
			setProcessed(true);
		} catch (Exception e) {
			log.log(Level.SEVERE, sql_main_rec, e);
			finalMsg = encoder.encodeUTF8("Ошибка - " + e.getMessage());
			throw e;
		} finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
		}	
		return finalMsg;
	}
	
	private void setProcessed(boolean isProcessed) throws Exception {
		String strProcessed = null;
		if (isProcessed) {
			strProcessed = new String("Y");
		} else {
			strProcessed = new String("N");
		}
		String sqlForUpdOrder = "UPDATE erps_order "
				+ "set processed ='" + strProcessed + "'"
				+ " WHERE erps_order_id=" + p_erp_C_Order_ID;
		String sqlForUpdEmployee = "UPDATE erps_employee "
				+ "set processed ='" + strProcessed + "'"
				+ " WHERE erps_order_id=" + p_erp_C_Order_ID;
		String sqlForUpdSigned = "UPDATE erps_employeessigned "
				+ "set processed ='" + strProcessed + "'"
				+ " WHERE erps_order_id=" + p_erp_C_Order_ID;
		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sqlForUpdOrder, get_TrxName());
			pstmt.execute();
			pstmt = null;
			pstmt = DB.prepareStatement(sqlForUpdEmployee, get_TrxName());
			pstmt.execute();
			pstmt = null;
			pstmt = DB.prepareStatement(sqlForUpdSigned, get_TrxName());
			pstmt.execute();
			pstmt = null;
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error while updating", e);
			finalMsg = encoder.encodeUTF8("Ошибка - " + e.getMessage());
			throw e;
		}	
		
	}

}
