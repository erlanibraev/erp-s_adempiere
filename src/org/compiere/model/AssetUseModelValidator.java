package org.compiere.model;

import java.sql.PreparedStatement;
import org.erps.ErpsEncoder;
import org.python.modules.newmodule;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.math.BigDecimal;

import org.compiere.util.DB;

public class AssetUseModelValidator implements ModelValidator {

	private int m_AD_Client_ID;
	private int m_AD_Org_ID;
	private int p_a_asset_id;
	private int p_self_a_asset_id;
	private Timestamp p_created;
	private Timestamp p_updated;
	private int p_createdby;
	private int p_updatedby;
	private int p_asset_group_id;
	private X_A_Asset_Info_Lic licObj = null;
	int lic_KeyID;
	private String finalMsg = "";
	private ErpsEncoder encoder = new ErpsEncoder();	
	
	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		if (client != null) {	
			m_AD_Client_ID = client.getAD_Client_ID();
		}
		engine.addModelChange(MAssetUse.Table_Name, this);
	}

	@Override
	public int getAD_Client_ID() {
		return m_AD_Client_ID;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		   if (po.get_TableName().equals("A_Asset_Use") && 
				   (type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW))	{
			   MAssetUse assetUse = (MAssetUse)po;
			   if (assetUse != null) {
				   String desc = assetUse.get_DisplayValue("description", true);
				   if (desc != null && !"".equals(desc)) {
					   p_self_a_asset_id = assetUse.get_ValueAsInt("a_asset_id");
					   String sql_self_asset_rec = "SELECT a_asset_group_id "
							   	+ "FROM a_asset "
							   	+ "WHERE a_asset_id =" + p_self_a_asset_id;
					   PreparedStatement pstmt = null;
					   ResultSet rs = null;
					   try {
						   pstmt = DB.prepareStatement(sql_self_asset_rec, assetUse.get_TrxName());
						   rs = pstmt.executeQuery();
						   while (rs.next()) {
							   p_asset_group_id = rs.getInt("a_asset_group_id"); 
						   }
						   if (p_asset_group_id == 1000008 || p_asset_group_id == 1000007) {
							   m_AD_Client_ID = assetUse.get_ValueAsInt("ad_client_id");
							   m_AD_Org_ID = assetUse.get_ValueAsInt("ad_org_id");
							   p_created = (Timestamp)assetUse.get_Value("created");
							   p_updated = (Timestamp)assetUse.get_Value("updated");
							   p_createdby = ((Integer)assetUse.get_Value("createdby")).intValue();
							   p_updatedby = ((Integer)assetUse.get_Value("updatedby")).intValue();
							   String sql_asset_rec = "SELECT a_asset_id "
									   + "FROM a_asset "
									   + "WHERE value ='" + desc +"'";
							   pstmt = null;
							   rs = null;
							   pstmt = DB.prepareStatement(sql_asset_rec, assetUse.get_TrxName());
							   rs = pstmt.executeQuery();
							   while (rs.next()) {
								   p_a_asset_id = rs.getInt("a_asset_id"); 
							   }
							   pstmt = null;
							   rs = null;
							   if (p_a_asset_id != 0) {
								   boolean isExisting = false;
								   String sql_asset_lic_rec = "SELECT * "
										   + "FROM a_asset_info_lic "
										   + "WHERE a_asset_id =" + p_a_asset_id;
								   pstmt = DB.prepareStatement(sql_asset_lic_rec, assetUse.get_TrxName());
								   rs = pstmt.executeQuery();
								   while (rs.next()) {
									   isExisting = (p_self_a_asset_id == rs.getInt("erps_asset"));
									   if (isExisting) {
										   lic_KeyID = rs.getInt("a_asset_info_lic_id");
										   break;	
									   }
								   }
								   if (!isExisting) {
									   lic_KeyID = DB.getNextID(m_AD_Client_ID, "A_Asset_Info_Lic", assetUse.get_TrxName());
								   }
								   licObj = new X_A_Asset_Info_Lic(assetUse.getCtx(), lic_KeyID, null);
								   licObj.setA_Asset_ID(p_a_asset_id);
								   licObj.setIsActive(true);
								   licObj.setAD_Org_ID(m_AD_Org_ID);
								   licObj.setAD_Client_ID(m_AD_Client_ID);
								   licObj.set_Value("created", p_created);
								   licObj.set_Value("updated", p_updated);
								   licObj.set_Value("createdby", p_createdby);
								   licObj.set_Value("updatedby", p_updatedby);
								   licObj.set_Value("erps_asset", new Integer(p_self_a_asset_id).toString());
								   licObj.set_Value("a_license_fee", new BigDecimal(0));
								   licObj.save();
								   //finalMsg = encoder.encodeUTF8("Информация о лицензии основного средства №" + 
										   //new Integer(p_a_asset_id).toString() + " успешно изменёна...");
							   }
						   	}
					   } catch (Exception e) {
						   finalMsg = encoder.encodeUTF8("Ошибка!" + e.getMessage());
					   } finally {
						   DB.close(rs, pstmt);
						   rs = null; pstmt = null;
					   }
				   }
			   }   
		   }
		return finalMsg;
	}

	@Override
	public String docValidate(PO po, int timing) {
		// TODO Auto-generated method stub
		return null;
	}

}
