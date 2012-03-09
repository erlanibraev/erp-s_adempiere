package org.compiere.process;

import java.util.logging.Level;
import org.compiere.model.MAsset;
import org.compiere.model.MerpsrecTransDocLine;
import org.compiere.model.MerpsrecTransDocSigned;
import org.compiere.model.MerpsreceptTransmissionDoc;
import org.compiere.util.CLogger;
import org.erps.ErpsEncoder;

public class ErpsAssetResponsibleChange extends SvrProcess {

	private String finalMsg = "OK";
	private MerpsrecTransDocLine[] lines;
	private MerpsrecTransDocSigned[] signed;
	private MerpsreceptTransmissionDoc TransmissionDoc = null;
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (ErpsAssetResponsibleChange.class);
	
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
		TransmissionDoc = new MerpsreceptTransmissionDoc(getCtx(), getRecord_ID(), get_TrxName());
		lines = TransmissionDoc.getLines();
		signed = TransmissionDoc.getSigned();

	}

	@Override
	protected String doIt() throws Exception {
		
		if(TransmissionDoc == null){
			s_log.log(Level.SEVERE, "TransmissionDoc is null ");
			return encoder.encodeUTF8("Error. TransmissionDoc is null");
		}
		
		if(lines.length == 0 || signed.length == 0){
			s_log.log(Level.SEVERE, "TransmissionDocLine or TransmissionDocSigned is null");
			return encoder.encodeUTF8("TransmissionDocLine or TransmissionDocSigned is null");
		}
		
		//  Lines
		for(MerpsrecTransDocLine dl: lines){
			MAsset as = new MAsset(getCtx(), dl.getA_Asset_ID());
			as.setC_BPartnerSR_ID(TransmissionDoc.geterps_toResponsible());
			as.saveEx();
			dl.setProcessed(true);
			dl.saveUpdate();
		}
		
		//Signed		
		for(MerpsrecTransDocSigned s: signed){
			s.setProcessed(true);
			s.saveUpdate();
		}
		
		TransmissionDoc.setProcessed(true);
		TransmissionDoc.saveUpdate();
		
		/*String sql_main_rec = "SELECT * "
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
			}	*/
			return finalMsg;
	}

}
