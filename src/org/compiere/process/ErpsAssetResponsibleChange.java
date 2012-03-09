package org.compiere.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MerpsrecTransDocLine;
import org.compiere.model.MerpsreceptTransmissionDoc;
import org.compiere.util.DB;
import org.erps.ErpsEncoder;

public class ErpsAssetResponsibleChange extends SvrProcess {

	private int	p_erp_TransDoc_ID;	
	private int	p_erp_C_BPartner_ID = 0;	
	private Integer	p_erp_A_Asset_ID = 0;	
	private String finalMsg;
	private MerpsrecTransDocLine[] lines;
	
	private ErpsEncoder encoder = new ErpsEncoder();	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)	{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null); 
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		//
		p_erp_TransDoc_ID = getRecord_ID();
		MerpsreceptTransmissionDoc mm = new MerpsreceptTransmissionDoc(getCtx(), p_erp_TransDoc_ID, get_TrxName());
		lines = mm.getLines();

	}

	@Override
	protected String doIt() throws Exception {
		
		if(lines.length == 0)
			throw new AdempiereException(encoder.encodeUTF8("Отсутствуют строки акта..."));
		
		//  Lines
		for (int i = 0; i < lines.length; i++)
		{
			MerpsrecTransDocLine docLine = lines[i];
			
		}
		
		String sql_main_rec = "SELECT * "
				+ "FROM erps_recepttransmissiondoc "
				+ "WHERE erps_recepttransmissiondoc_id =" + p_erp_TransDoc_ID;
			String sql_mainline_doc = "SELECT a_asset_id "
					+ "FROM erps_rectransdocline "
					+ "WHERE erps_recepttransmissiondoc_id =" + p_erp_TransDoc_ID;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				pstmt = DB.prepareStatement(sql_main_rec, get_TrxName());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					p_erp_C_BPartner_ID = rs.getInt("erps_toResponsible"); 
				}
				pstmt = null;
				rs = null;
				pstmt = DB.prepareStatement(sql_mainline_doc, get_TrxName());
				rs = pstmt.executeQuery();
				while (rs.next()) {
					p_erp_A_Asset_ID = rs.getInt("a_asset_id");
				}
				if (p_erp_A_Asset_ID != 0 && p_erp_C_BPartner_ID != 0) {
					String sql_empl_upd = "UPDATE a_asset "
							+ "set c_bpartnersr_id =" + p_erp_C_BPartner_ID
							+ " WHERE a_asset_id =" + p_erp_A_Asset_ID;
					pstmt = null;
					pstmt = DB.prepareStatement(sql_empl_upd, get_TrxName());
					pstmt.execute();
					log.log(Level.INFO, p_erp_A_Asset_ID.toString());
					finalMsg = encoder.encodeUTF8("Основное средство № " + new Integer(p_erp_A_Asset_ID).toString() + " изменено...");
				} else {
					if (p_erp_A_Asset_ID == 0) {
						finalMsg = encoder.encodeUTF8("Основное средство не выбрано...");
					}
					if (p_erp_C_BPartner_ID == 0) {
						finalMsg = finalMsg + " " + encoder.encodeUTF8("Не выбран бизнес партнер...");
					}
				}
//				setProcessed(true);
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

}
