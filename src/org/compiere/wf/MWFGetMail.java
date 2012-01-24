package org.compiere.wf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import org.compiere.util.DB;

public class MWFGetMail {

	public static List<String> getMail(int ad_wf_node_id){
		List<String> emails = new ArrayList<String>();
		
		//protected CLogger log = CLogger.getCLogger(getClass());
		String role = null;
		String SQl = 
				"	Select	r.responsibletype														"+
				"		from ad_wf_responsible r													"+
				"		inner join ad_wf_node n on n.ad_wf_responsible_id = r.ad_wf_responsible_id	"+
				"			where																	"+
				"				r.isactive = 'Y'													"+
				"			and n.isactive = 'Y'													"+
				"			and n.ad_wf_node_id = ?													";
		
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try
				{
					pstmt = DB.prepareStatement(SQl, null);
					pstmt.setInt(1, ad_wf_node_id);
					rs = pstmt.executeQuery();
					if (rs.next())
					{
						role = new String(rs.getString(1));
					}
				}
				catch (SQLException e)
				{
					System.out.println(e.toString());
					//log.log(Level.SEVERE, "error", e);
				}
				finally
				{
					DB.close(rs, pstmt);
					rs = null; pstmt = null;
				}
				//	get email ---------------
				if(role == null)
					return null;		
		
				if (role.equals("H")){
				SQl = //" Select name from ad_user where ad_user_id = ? ";//'1000110'";
						"	Select u.email 																"+
						"	from ad_wf_responsible r													"+
						"	inner join ad_wf_node n on n.ad_wf_responsible_id = r.ad_wf_responsible_id	"+
						"	inner join ad_user u on u.ad_user_id = r.ad_user_id							"+
						"	where																		"+
						"				r.isactive = 'Y'												"+
						"			and u.isactive = 'Y'												"+
						"			and n.isactive = 'Y'												"+						
						"			and n.ad_wf_node_id = ?												";
					//
					pstmt = null;
					rs = null;
					try
					{
						pstmt = DB.prepareStatement(SQl, null);
						pstmt.setInt(1, ad_wf_node_id);
						rs = pstmt.executeQuery();
						if (rs.next())
						{
							String name = new String(rs.getString(1));
							emails.add(name);
							return emails;
						}
					}
					catch (SQLException e)
					{
						System.out.println(e.toString());
						//log.log(Level.SEVERE, "error", e);
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}
				}	
						
		
				if (role.equals("R")){
				SQl = 
					"	Select	u.email																"+
				
					"	from ad_wf_responsible r													"+	
					"inner join ad_wf_node n on n.ad_wf_responsible_id = r.ad_wf_responsible_id		"+
					"inner join ad_user_roles ur on ur.ad_role_id = r.ad_role_id					"+
					"inner join ad_user u on u.ad_user_id = ur.ad_user_id							"+
					"where 																			"+
					"	r.isactive = 'Y'															"+
					"	and u.isactive = 'Y'														"+
					"	and ur.isactive = 'Y'														"+		
					"	and n.ad_wf_node_id = ?														";
						 
						//
					pstmt = null;
					rs = null;
					try
					{
						pstmt = DB.prepareStatement(SQl, null);
						pstmt.setInt(1, ad_wf_node_id);
						rs = pstmt.executeQuery();
						while (rs.next())
						{
							String name = new String(rs.getString(1));
							emails.add(name);
						}
						return emails;
					}
					catch (SQLException e)
					{
						System.out.println(e.toString());
						//log.log(Level.SEVERE, "error", e);
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}
				}
				
		return null;
				
	}
	
}
