package org.adempiere.process.rpl.imp;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;
import javax.xml.xpath.XPathExpressionException;

import org.compiere.model.MBankAccount;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MCash;
import org.compiere.model.MCashLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalBatch;
import org.compiere.model.MJournalLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.util.CLogger;
import java.util.logging.Level;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class Import1C {

	/** Instance Logger 			*/
	private CLogger Log = CLogger.getCLogger(Import1C.class);
//	private static CLogger s_log = CLogger.getCLogger(Import1C.class);
	
	/** constants */
	public static String cID = "ID"; // internal ID
	public static String cDocNum = "DocNum"; // document number
	public static String cDocDate = "DocDate"; // document date
	public static String cSettAcc = "SettlementAccount"; // Settlement Account
	public static String cBankAcc = "BankAccount"; // Settlement Account
	public static String cBPAcc = "ContractorAccount"; // Settlement Account
	public static String cBisPart = "BusinessPartner"; // Contractor
	public static String cINN = "INN"; // INN (RNN)
	public static String cRNN = "RNN"; // RNN
	public static String cCode = "Code"; // Code
	public static String cPayAmt = "PaymentSum"; // Payment Amount
	public static String cNum = "Number"; // Number
	public static String cEmpl = "Сотрудники"; // Employees
	public static String cName = "Name"; // Name
	public static String cItem = "Item"; // Item
	public static String cSalCharge = "Заработная плата"; // Salary charge
	
	/** Context						*/
	private Properties ctx = null;
	private	Integer AD_Client_ID;	
	private	Integer AD_Org_ID;
	private Integer iAPPDocTypeID;
	private Integer iAPIDocTypeID;
	private Integer iARIDocTypeID;
	private Integer iSODocTypeID;
	private Integer iPODocTypeID;
	private	ArrayList<Properties> lAttrVal = new ArrayList<Properties>(); 
	private	Map<String, List<String>> BankAcct = new HashMap<String, List<String>>();
	private	Map<String, List<String>> BPBankAcct = new HashMap<String, List<String>>();
	private	Map<String, List<String>> BP = new HashMap<String, List<String>>();
	private Map<String, List<String>> BDDS  = new HashMap<String, List<String>>();
	private Map<String, List<String>> DS  = new HashMap<String, List<String>>();
	private Map<String, List<String>> BDR  = new HashMap<String, List<String>>();
	private Map<String, List<String>> Account  = new HashMap<String, List<String>>();
	private Map<String, List<String>> Payments  = new HashMap<String, List<String>>();
	private Map<String, List<String>> VAT  = new HashMap<String, List<String>>();
	private Map<String, List<String>> Product  = new HashMap<String, List<String>>();
	private Map<String, List<String>> PurOrder  = new HashMap<String, List<String>>();
	private Map<String, List<String>> Advance  = new HashMap<String, List<String>>();
	private Map<String, List<String>> Closing  = new HashMap<String, List<String>>();
	private Map<String, List<String>> Operation  = new HashMap<String, List<String>>();
	private String sRes1 = new String(); 
	private String sRes2 = new String(); 
	private Properties pJournalBatch = new Properties();

	/** Set change PO			*/
	boolean isChanged= false;
	private Properties p_param;
	

	public Import1C(Properties ctx) throws SQLException
	{
		AD_Client_ID = Env.getAD_Client_ID(ctx);
		AD_Org_ID = Env.getAD_Org_ID(ctx);
		if (AD_Org_ID == 0) { 
			String sql = "SELECT ad_org_id FROM ad_org"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'";
			if (SingleSQLSelect(sql)) { AD_Org_ID = Integer.valueOf(sRes1);  };
		}
		// Finding AP Payment ID		
		String sql = "SELECT c_doctype_id FROM c_doctype"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y' AND docbasetype = 'APP'";
		if (SingleSQLSelect(sql)) { iAPPDocTypeID = Integer.valueOf(sRes1);};
		// Finding AP Payment ID		
		sql = "SELECT c_doctype_id FROM c_doctype"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y' AND docbasetype = 'API'";
		if (SingleSQLSelect(sql)) { iAPIDocTypeID = Integer.valueOf(sRes1);};
		// Finding AR Invoice		
		sql = "SELECT c_doctype_id FROM c_doctype"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y' AND docbasetype = 'ARI'";
		if (SingleSQLSelect(sql)) { iARIDocTypeID = Integer.valueOf(sRes1);};
		// Finding Standard Order		
		sql = "SELECT c_doctype_id FROM c_doctype"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y' AND docsubtypeso = 'SO'";
		if (SingleSQLSelect(sql)) { iSODocTypeID = Integer.valueOf(sRes1);};
		// Finding Purchase Order		
		sql = "SELECT c_doctype_id FROM c_doctype"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y' AND docbasetype = 'POO'"
			+ " AND issotrx='N' and docsubtypeso is NULL";
		if (SingleSQLSelect(sql)) { iPODocTypeID = Integer.valueOf(sRes1);};
		
		this.ctx = ctx;
	}

	private boolean SingleSQLSelect(String sSQL) throws SQLException {
		sRes1 = sRes2 = null;
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement(sSQL, null);
		try {
			ResultSet rs = null;
			if (sSQL.indexOf("SELECT")==0)	{
				rs = pstmt.executeQuery();
				while (rs.next()) {
					sRes1 = rs.getString(1);
					if (rs.getMetaData().getColumnCount()>1) 
						sRes2 = rs.getString(2); 
					break;
				
				}
			} else if (sSQL.indexOf("UPDATE")==0)
				pstmt.executeUpdate();
         	if (rs!=null) rs.close();
		} finally {
			try {
				if (pstmt != null)	pstmt.close();
			} catch (Exception e) {
			}
			pstmt = null;
		}
	if (sRes1==null) return false;
	return true;
	}

	private void findBankAcc() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cNum)==null
		   || AttrVal.getProperty(cID)==null) return;
		String sql = "SELECT c_bankaccount_id, c_currency_id FROM c_bankaccount"
  			+ " WHERE ad_client_id = " + AD_Client_ID
//  			+ " AND ad_org_id = " + AD_Org_ID
  			+ " AND isactive='Y'"  			
  			+ " AND accountno = '" + AttrVal.getProperty(cNum) + "'";
		if (SingleSQLSelect(sql)) { 
				List<String> sval = new ArrayList<String>();
				sval.add(sRes1);
				sval.add(sRes2);
				sval.add(AttrVal.getProperty(cCode));
				sval.add(AttrVal.getProperty(cName));
				BankAcct.put(AttrVal.getProperty(cID), sval);
		};
	}   

	private void findBPBankAcc() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cNum)==null
		   || AttrVal.getProperty(cID)==null) return;
		String sql = "SELECT c_bp_bankaccount_id FROM C_BP_BankAccount"
  			+ " WHERE ad_client_id = " + AD_Client_ID
//  			+ " AND ad_org_id = " + AD_Org_ID
  			+ " AND isactive='Y'"  			
  			+ " AND accountno = '" + AttrVal.getProperty("NewNumber") + "'";
		if (SingleSQLSelect(sql)) { 
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			BPBankAcct.put(AttrVal.getProperty(cID), sval);
		} else Log.log(Level.SEVERE, "BP Account not found: "
	    +" "+AttrVal.getProperty(cID)
	    +" "+AttrVal.getProperty(cCode)
	    +" "+AttrVal.getProperty(cName)
	    +" "+AttrVal.getProperty("NewNumber")); 
	}   

	private void findBP() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if (AttrVal.getProperty(cCode)==null) return;
		String sRNN = null;
		if (AttrVal.getProperty(cINN)!=null) sRNN = AttrVal.getProperty(cINN);
		if (AttrVal.getProperty(cRNN)!=null) sRNN = AttrVal.getProperty(cRNN);
		if (sRNN==null) return;
// Finding Business Partner ID		
		String sql = "SELECT c_bpartner_id FROM c_bpartner"
  			+ " WHERE ad_client_id = " + AD_Client_ID
//  			+ " AND ad_org_id = " + AD_Org_ID
  			+ " AND isactive='Y' "  			
  			+ " AND value = '" + AttrVal.getProperty(cCode) 
  			+ " "+ sRNN+"'";
		if (SingleSQLSelect(sql)) {  
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			BP.put(AttrVal.getProperty(cID), sval);
		} else {
			sql = "SELECT c_bpartner_id FROM c_bpartner"
		  			+ " WHERE ad_client_id = " + AD_Client_ID
//		  			+ " AND ad_org_id = " + AD_Org_ID
		  			+ " AND isactive='Y' "  			
		  			+ " AND value like '%"
		  			+  sRNN+"%'";
			if (SingleSQLSelect(sql)) {  
				List<String> sval = new ArrayList<String>();
			   	sval.add(sRes1);
				sval.add(AttrVal.getProperty(cCode));
				sval.add(AttrVal.getProperty(cName));
				BP.put(AttrVal.getProperty(cID), sval);
			} else	
				System.out.println("Not found Business Partner: "
					+AttrVal.getProperty(cCode)
					+ " "+ sRNN
					+ " "+ AttrVal.getProperty("FullName")
					+ " "+ AttrVal.getProperty("Name"));
		}
	}   

	private int findSalary(String AccountCode, String BDDSCode) throws SQLException
	{ 
		String sql = "SELECT c_charge_id, name FROM c_charge"
		          + "  WHERE ad_client_id = " + AD_Client_ID
				  + " AND isactive='Y' " ; 			
		if ((AccountCode==null) && (BDDSCode==null)) {
			Properties AttrVal = lAttrVal.get(0);
			Log.log(Level.SEVERE, "AccountCode and BDDSCode is NULL. "+
				AttrVal.getProperty(cID)+ " "+AttrVal.getProperty(cNum));			
			return 0;		
		}
		if ((AccountCode==null) && (BDDS.get(BDDSCode)!=null)) {
			sql += " AND name like '%" + BDDS.get(BDDSCode).get(1) +"%'";
		} else if (( AccountCode!=null ) && (Account.get(AccountCode)!=null)
	    		&& (Account.get(AccountCode).get(2)!=null)) {
			sql += " AND name like '" + Account.get(AccountCode).get(2) +"_%'";
	    }	

		if (SingleSQLSelect(sql)) return Integer.valueOf(sRes1); 
		return 0;
	}   

	private void findBDDS() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cCode)==null
			 || AttrVal.getProperty(cID)==null
			 || AttrVal.getProperty(cCode).length()<2) return;
		String sql = "SELECT erps_stati_bdds_id FROM erps_stati_bdds"
		          + "  WHERE ad_client_id = " + AD_Client_ID;
			sql += " AND stati_bdds = '" + AttrVal.getProperty(cCode) +"'";
			sql += " AND upper(description) = '" + AttrVal.getProperty(cName).toUpperCase() +"'";

		if (SingleSQLSelect(sql)) {
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			BDDS.put(AttrVal.getProperty(cID), sval);
		} else System.out.println("BDDS not found: "
						+AttrVal.getProperty(cCode)
						+ " "+ AttrVal.getProperty(cName)
						//+ " "+sql 
						);
		return;
	}   

	private void findDS() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cCode)==null
			 || AttrVal.getProperty(cID)==null
			 || AttrVal.getProperty(cCode).length()==1	) return;
		String sql = "SELECT erps_denejnie_sredstva_id FROM erps_denejnie_sredstva"
		          + "  WHERE ad_client_id = " + AD_Client_ID;
			sql += " AND denejnie_sredstva = '" + AttrVal.getProperty(cCode) +"'";
			sql += " AND description = '" + AttrVal.getProperty(cName) +"'";

		if (SingleSQLSelect(sql)) {
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			DS.put(AttrVal.getProperty(cID), sval);
		} else Log.log(Level.SEVERE, "BDDS not found: "
			    +" "+AttrVal.getProperty(cID)
			    +" "+AttrVal.getProperty(cCode)
			    +" "+AttrVal.getProperty(cName)
			    +" "+sql.toString()); 
		return;
	}   
	
	private void findBDR() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cCode)==null
				   || AttrVal.getProperty(cID)==null) return;
		String sql = "SELECT erps_stati_bdr_id FROM erps_stati_bdr"
		          + "  WHERE ad_client_id = " + AD_Client_ID;
			sql += " AND stati_bdr = '" + AttrVal.getProperty(cCode) +"'";
			sql += " AND upper(description) = '" + AttrVal.getProperty(cName).toUpperCase() +"'";

		if (SingleSQLSelect(sql))  {
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			BDR.put(AttrVal.getProperty(cID), sval);
		} /*else Log.log(Level.SEVERE, "BDR not found: "
			    +" "+AttrVal.getProperty(cID)
			    +" "+AttrVal.getProperty(cCode)
			    +" "+AttrVal.getProperty(cName)
			    +" "+sql.toString());*/
		return ;
	}   

	private void findVAT() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cName)==null
				   || AttrVal.getProperty(cID)==null) return;
		String rate = "0";
		if (AttrVal.getProperty("Rate")!=null) 
		  rate = AttrVal.getProperty("Rate");
		String sql = "SELECT c_tax_id FROM c_tax"
		          + "  WHERE ad_client_id = " + AD_Client_ID;
			sql += " AND rate = '" + rate +"'";
			sql += " AND description like '%" + AttrVal.getProperty(cCode) +"%'";

		if (SingleSQLSelect(sql))  {
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			VAT.put(AttrVal.getProperty(cID), sval);
		} /*else Log.log(Level.SEVERE, "VAT not found: "
			    +" "+AttrVal.getProperty(cID)
			    +" "+AttrVal.getProperty(cCode)
			    +" "+AttrVal.getProperty(cName)
			    +" "+sql.toString());*/
		return ;
	}   

	private void findProduct() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cID)==null 
		   || Product.get( AttrVal.getProperty(cID) )!=null) return;
		
		String sql = "SELECT a.m_product_id, a.name"
				   + " FROM m_product AS a, m_product_category as b"
		           + " WHERE a.ad_client_id = " + AD_Client_ID
		           + " AND b.ad_client_id = " + AD_Client_ID
		           + " AND a.m_product_category_id = b.m_product_category_id";
		//System.out.println(AttrVal.getProperty(cID));
		if (AttrVal.getProperty(cID).indexOf("0___9574___0")>0) {// is Asset           
               sql+=" AND b.a_asset_group_id > 0"
			       + " AND a.description = '" + AttrVal.getProperty("Description") +"'";
		} else {
            sql+=" AND b.a_asset_group_id is null"
			       + " AND a.name = '" + AttrVal.getProperty("Name") +"'";
		}
		if (SingleSQLSelect(sql)) { 
			List<String> sval = new ArrayList<String>();
			sval.add(sRes1);
			sval.add(AttrVal.getProperty(cCode));
			sval.add(AttrVal.getProperty(cName));
			Product.put(AttrVal.getProperty(cID), sval);
		};
	}   
	
	private void findPurchaseOrder() throws SQLException
	{ 
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cID)==null 
		   || Product.get( AttrVal.getProperty(cID) )!=null) return;
		
		String sql = "SELECT c_order_id"
				   + " FROM c_order"
		           + " WHERE ad_client_id = " + AD_Client_ID
		           + " AND issotrx = 'N' "
		           + " AND documentno = '"+ AttrVal.getProperty("Name")+"'";
		List<String> sval = new ArrayList<String>();
		
		if (SingleSQLSelect(sql)) sval.add(sRes1);
		else sval.add(null);
		sval.add(sRes1);
		sval.add(AttrVal.getProperty(cCode));
		sval.add(AttrVal.getProperty(cName));
		PurOrder.put(AttrVal.getProperty(cID), sval);
	}   

	private void addAccount()
	{ 
		Properties AttrVal = lAttrVal.get(0);
		List<String> sval = new ArrayList<String>();
		if (AttrVal.getProperty(cID)==null) return; 
		if (AttrVal.getProperty(cName)==null) return; 
		sval.add(AttrVal.getProperty(cID));
		sval.add(AttrVal.getProperty(cName));
		sval.add(AttrVal.getProperty(cCode));
		Account.put(AttrVal.getProperty(cID), sval);
	}   
	
	private void addAdvance()
	{ 
		Properties AttrVal = lAttrVal.get(0);
		List<String> sval = new ArrayList<String>();
		if (AttrVal.getProperty(cID)==null) return; 
		if (AttrVal.getProperty(cDocNum)==null) return; 
		if (AttrVal.getProperty("Operation")==null) return; 
		sval.add(AttrVal.getProperty(cID));
		sval.add(AttrVal.getProperty(cDocNum));
		Advance.put(AttrVal.getProperty("Operation"), sval);
	}   

	private void addClosing()
	{ 
		Properties AttrVal = lAttrVal.get(0);
		List<String> sval = new ArrayList<String>();
		if (AttrVal.getProperty(cID)==null) return; 
		if (AttrVal.getProperty(cDocNum)==null) return; 
		if (AttrVal.getProperty("Operation")==null) return; 
		sval.add(AttrVal.getProperty(cID));
		sval.add(AttrVal.getProperty(cDocNum));
		Closing.put(AttrVal.getProperty("Operation"), sval);
	}   

	private void addOperation()
	{ 
		Properties AttrVal = lAttrVal.get(0);
		List<String> sval = new ArrayList<String>();
		if (AttrVal.getProperty(cID)==null) return; 
		if (AttrVal.getProperty(cDocNum)==null) return; 
		if (AttrVal.getProperty("Operation")==null) return; 
		sval.add(AttrVal.getProperty(cID));
		sval.add(AttrVal.getProperty(cDocNum));
		Operation.put(AttrVal.getProperty("Operation"), sval);
	}   

	private int findValidCombination(String AccountCode, String BPCode) throws SQLException
	{ 
		if ((AccountCode==null) && (BPCode==null)) {
			Properties AttrVal = lAttrVal.get(0);
			Log.log(Level.SEVERE, "AccountCode and BPCode is NULL. "+
				AttrVal.getProperty(cID)+ " "+AttrVal.getProperty(cNum));			
			return 0;		
		}
		String sql ="SELECT c.c_validcombination_id FROM c_elementvalue as e"
				 +" LEFT JOIN c_validcombination AS c ON account_id = c_elementvalue_id"
				 +" WHERE e.ad_client_id = 1000000"
				 +" AND e.isactive = 'Y'"
				 +" AND e.issummary = 'N'";
		String sql2 = sql;
		if ((AccountCode!=null) && (Account.get(AccountCode)!=null)) {
			sql += " AND e.value like '"+ Account.get(AccountCode).get(2) +"%'";
			// Account 3351 validcombination 1000146
			if ("3351".equals(Account.get(AccountCode).get(2))) 
				 return 1000146;
		}
		if ((BPCode!=null ) && (BP.get(BPCode)!=null)) {
			sql += " AND c.c_bpartner_id = " + BP.get(BPCode).get(0);
	    }	
		if (SingleSQLSelect(sql)) return Integer.valueOf(sRes1);
		else
		{
			if ((AccountCode!=null) && (Account.get(AccountCode)!=null)) {
				sql2 += " AND e.value like '"+ Account.get(AccountCode).get(2) +"%'";
				sql2 += " AND c.c_bpartner_id is NULL";
				if (SingleSQLSelect(sql2)) return Integer.valueOf(sRes1);
			}
		}
		
		return 0;
	}   
	
	public void importXMLDocument(StringBuffer result, Document documentToBeImported, String trxName,
			Properties p_param) 
		throws Exception, SQLException,	XPathExpressionException 
	{
	        final int cTFc = 5; // Tags column count
	        final int cAFc = 3; // Attribute column count
            int i,j,k,n;
            this.p_param = p_param;
            ArrayList<String[]> lTagsArray = new ArrayList<String[]>();
            ArrayList<Properties> lRuEn = new ArrayList<Properties>();
            Element rootElement = documentToBeImported.getDocumentElement();
            
            if ( "Импорт".equals(rootElement.getNodeName())) System.out.println(rootElement.getNodeName());
			result.append("<br>");
			String sql = "SELECT erps_1ctag_id, name, value, tag_type, childnodes"
					+ " FROM erps_1ctag "
					+ " WHERE isactive='Y'"
					+ " ORDER BY tag_type, description";
			PreparedStatement pstmt = null;
			pstmt = DB.prepareStatement(sql, null);
			try {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
			        Properties pRuEn = new Properties();
		  		    String[] mTags = new String[cTFc];
					for (i=0;i<cTFc;i++) {	mTags[i] = rs.getString(i+1);};
		  		    
					String sql2 = "SELECT erps_1ctag_id, name, value"
		      			+ " FROM erps_1cattr "
		      			+ " WHERE isactive='Y' "
		      			+ " AND erps_1ctag_id="+rs.getString("ERPS_1CTAG_ID");
					PreparedStatement pstmt2 = null;
					pstmt2 = DB.prepareStatement(sql2, null);
					try {
						ResultSet rs2 = pstmt2.executeQuery();
						while (rs2.next()) {
				  		    String[] mAttr = new String[cAFc];
							for (i=0;i<cAFc;i++) {	mAttr[i] = rs2.getString(i+1);};
				  		    pRuEn.put(rs2.getString(cName),rs2.getString("Value"));
						}
						rs2.close();
						pstmt2.close();
						pstmt2 = null;
					} finally {
						try {
							if (pstmt2 != null) pstmt2.close();
						} catch (Exception e) {
						}
						pstmt2 = null;
					}
					lTagsArray.add(mTags);
					lRuEn.add(pRuEn);
				}
				rs.close();
				pstmt.close();
				pstmt = null;
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
				} catch (Exception e) {
				}
				pstmt = null;
				sql = null;
			}

			for (i=0;i<lTagsArray.size();i++) {
//				System.out.println(r[0]+r[1]+r[2]);
		        String[] mTags = (String[]) lTagsArray.get(i);
//				System.out.println(mTags[0]+" "+mTags[1]+" "+mTags[2]+" "+mTags[3]+" "+mTags[4]);
//	  		    mTags = (String[]) array.get(i*2);

/*		        Enumeration<?> keys = pAttr.keys();
		        while (keys.hasMoreElements()) {
		        	  System.out.println((String)keys.nextElement() + ": " + 
		        			  (String)pAttr.get((String)keys.nextElement()));
		        	}*/
//		        pRuEn.list(System.out);

  	      	  	NodeList ElemList = documentToBeImported.getElementsByTagName(mTags[1]);
  	      	  	if (!"3".equals(mTags[3]))
  	      	  		result.append(mTags[2] + " = " + ElemList.getLength() +"<br>");
  	      	  	for (j=0; j < ElemList.getLength(); j++) {
  			        Properties pRuEn = (Properties) lRuEn.get(i);
	                Element thisTag = (Element)ElemList.item(j);
    	      	  	Properties AttrVal = new Properties();
			        for (k=0; k < thisTag.getAttributes().getLength(); k++) {
			        	if (pRuEn.getProperty(thisTag.getAttributes().item(k).getNodeName()) != null) {
			        	AttrVal.setProperty(pRuEn.getProperty(thisTag.getAttributes().item(k).getNodeName()), 
			        			         thisTag.getAttributes().item(k).getNodeValue().trim());}
			        }  
    	      	  	//System.out.println(AttrVal.getProperty(cDocNum)+" "+AttrVal.getProperty(cID));
			        lAttrVal.add(AttrVal);  
			        if ( "BankAccount".equals(mTags[2])) {// Looking Bank Account
			        	findBankAcc();	
			        } else if ("BPBankAccount".equals(mTags[2])) {// Looking Bank Account
			        	findBPBankAcc(); 
			        } else if ("BussinesPartner".equals(mTags[2])) {// Looking Bank Account
			        	findBP(); 
			        } else if ("BDDS_directory".equals(mTags[2])) {
			        	findBDDS();
			        } else if ("BDR_directory".equals(mTags[2])) {
			        	findBDR();
			        } else if ("AccountDirectory".equals(mTags[2])) {
			        	addAccount();
			        } else if ("Funds".equals(mTags[2])) {
			        	findDS();
			        } else if ("VAT".equals(mTags[2])) {
			        	findVAT();
			        } else if ("Assets".equals(mTags[2]) || "Materials".equals(mTags[2])) {
			        	findProduct();
			        } else if ("Contract".equals(mTags[2])) {
			        	findPurchaseOrder();
			        } else if ("AdvanceClosing".equals(mTags[2])) {
			        	addAdvance();
			        } else if ("MonthClosing".equals(mTags[2])) {
			        	addClosing();
			        } else if ("Operation".equals(mTags[2])) {
			        	addOperation();
			        } else if ( "Payment".equals(mTags[2])) {
			    		List<String> sval = new ArrayList<String>();
			    		sval.add(AttrVal.getProperty(cDocNum));
			    		sval.add(AttrVal.getProperty(cDocDate));
			        	Payments.put(AttrVal.getProperty(cID).substring(33), sval);
			        	Element thisTag2 = (Element)ElemList.item(j);
			        	NodeList ElemList2 = thisTag2.getChildNodes();
			        	for (k=0; k < ElemList2.getLength(); k++) {
			        		Properties childAttrVal = new Properties();
			        		Element childTag = (Element)ElemList2.item(k);
			        		for(n=0;n<lTagsArray.size();n++)  
			    		        if (lTagsArray.get(n)[1].equals(childTag.getNodeName()))
			    		        	{ pRuEn = (Properties) lRuEn.get(n); break;};
					        for (n=0; n < childTag.getAttributes().getLength(); n++) 
				        	  if ( pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()) != null) {
					        	 childAttrVal.setProperty(pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()), 
					        			         childTag.getAttributes().item(n).getNodeValue().trim());}
					        String sItem = childAttrVal.getProperty("Item");
					        if ( sItem!=null )
					        	{ 
					        	  if  ( (BDDS.get(sItem)!=null) &&  
					        			( !"2.3.3".equals(BDDS.get(sItem).get(1)) )
					        			|| (ElemList2.getLength()==1)	) 
					        	// Is not a bank commission    
					        	lAttrVal.add(childAttrVal);
					        	}
					        else 
					        	System.out.println("Line not added");
			        	}
			        	if ("Y".equals(p_param.getProperty("Payments"))) import1CPayment(trxName);
			        } else if ( "BankStatement".equals(mTags[2])) {
			        	Element thisTag2 = (Element)ElemList.item(j);
			        	NodeList ElemList2 = thisTag2.getChildNodes();
			        	for (k=0; k < ElemList2.getLength(); k++) {
			        		Properties childAttrVal = new Properties();
			        		Element childTag = (Element)ElemList2.item(k);
			        		for(n=0;n<lTagsArray.size();n++)  
			    		        if (lTagsArray.get(n)[1].equals(childTag.getNodeName()))
			    		        	{ pRuEn = (Properties) lRuEn.get(n); break;};
					        for (n=0; n < childTag.getAttributes().getLength(); n++) 
				        	  if ( pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()) != null) {
					        	 childAttrVal.setProperty(pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()), 
					        			         childTag.getAttributes().item(n).getNodeValue().trim());}
				        	lAttrVal.add(childAttrVal);  
			        	}
			        	if ("Y".equals(p_param.getProperty("BankStatements"))) import1CBankStatement(trxName);
			        } else if ( "CashIn".equals(mTags[2]) | "CashOut".equals(mTags[2]) ) {
			        	if ("Y".equals(p_param.getProperty("Cash"))) import1CCash(trxName,mTags[2]);
			        } else if ( "ReceivedInvoice".equals(mTags[2])) {
			        	Element thisTag2 = (Element)ElemList.item(j);
			        	NodeList ElemList2 = thisTag2.getChildNodes();
			        	for (k=0; k < ElemList2.getLength(); k++) {
			        		Properties childAttrVal = new Properties();
			        		Element childTag = (Element)ElemList2.item(k);
			        		// searching document line
			        		for(n=0;n<lTagsArray.size();n++)  
			    		        if (lTagsArray.get(n)[1].equals(childTag.getNodeName()))
			    		        	{ pRuEn = (Properties) lRuEn.get(n); break;};
					        // searching document line
					        for (n=0; n < childTag.getAttributes().getLength(); n++) 
				        	  if ( pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()) != null) {
					        	 childAttrVal.setProperty(pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()), 
					        			         childTag.getAttributes().item(n).getNodeValue().trim());}
				        	lAttrVal.add(childAttrVal);  
			        	}
			        	if ("Y".equals(p_param.getProperty("ReceivedInvoices"))) import1CRecInv(trxName);
			        } else if ( "IssuedInvoice".equals(mTags[2])) {
			        	Element thisTag2 = (Element)ElemList.item(j);
			        	NodeList ElemList2 = thisTag2.getChildNodes();
			        	for (k=0; k < ElemList2.getLength(); k++) {
			        		Properties childAttrVal = new Properties();
			        		Element childTag = (Element)ElemList2.item(k);
			        		// searching document line
			        		for(n=0;n<lTagsArray.size();n++)  
			    		        if (lTagsArray.get(n)[1].equals(childTag.getNodeName()))
			    		        	{ pRuEn = (Properties) lRuEn.get(n); break;};
					        // searching document line
					        for (n=0; n < childTag.getAttributes().getLength(); n++) 
				        	  if ( pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()) != null) {
					        	 childAttrVal.setProperty(pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()), 
					        			         childTag.getAttributes().item(n).getNodeValue().trim());}
				        	lAttrVal.add(childAttrVal);  
			        	}
			        	if ("Y".equals(p_param.getProperty("IssuedInvoices"))) import1CIssInv(trxName);
			        } else if ( "SalesOrder".equals(mTags[2])) {
			        	Element thisTag2 = (Element)ElemList.item(j);
			        	NodeList ElemList2 = thisTag2.getChildNodes();
			        	for (k=0; k < ElemList2.getLength(); k++) {
			        		Properties childAttrVal = new Properties();
			        		Element childTag = (Element)ElemList2.item(k);
			        		// searching document line
			        		for(n=0;n<lTagsArray.size();n++)  
			    		        if (lTagsArray.get(n)[1].equals(childTag.getNodeName()))
			    		        	{ pRuEn = (Properties) lRuEn.get(n); break;};
					        // searching document line
					        for (n=0; n < childTag.getAttributes().getLength(); n++) 
				        	  if ( pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()) != null) {
					        	 childAttrVal.setProperty(pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()), 
					        			         childTag.getAttributes().item(n).getNodeValue().trim());}
				        	lAttrVal.add(childAttrVal);  
			        	}
			        	if ("Y".equals(p_param.getProperty("SalesOrders"))) import1CSalOrd(trxName);
			        } else if ("DocOperation".equals(mTags[2]) ) {
			        	Element thisTag2 = (Element)ElemList.item(j);
			        	NodeList ElemList2 = thisTag2.getChildNodes();
			        	for (k=0; k < ElemList2.getLength(); k++) {
			        		Properties childAttrVal = new Properties();
			        		Element childTag = (Element)ElemList2.item(k);
			        		// searching document line
			        		for(n=0;n<lTagsArray.size();n++)  
			    		        if (lTagsArray.get(n)[1].equals(childTag.getNodeName()))
			    		        	{ pRuEn = (Properties) lRuEn.get(n); break;};
					        // searching document line
					        for (n=0; n < childTag.getAttributes().getLength(); n++) 
				        	  if ( pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()) != null) {
					        	 childAttrVal.setProperty(pRuEn.getProperty(childTag.getAttributes().item(n).getNodeName()), 
					        			         childTag.getAttributes().item(n).getNodeValue().trim());}
				        	lAttrVal.add(childAttrVal);  
			        	}
			        	if ("Y".equals(p_param.getProperty("GL"))) import1CGL(trxName, mTags[2]);
			        }
			        lAttrVal.clear();
  	      	  	}	
			}
	}

/*************** Import Payment from 1C ****************************/	
	private void import1CPayment(String sTrxName) throws ParseException, SQLException
	{
		Properties AttrVal = lAttrVal.get(0);
		if (lAttrVal.size()<2) {
			System.out.println("Payment № "+AttrVal.getProperty(cDocNum)+" Error!!!. Payment is empty");
			return;
		};
		Properties childAttrVal = lAttrVal.get(1);
		if ( AttrVal.getProperty(cDocNum)==null 
		   || AttrVal.getProperty(cDocDate)==null
		   || AttrVal.getProperty(cSettAcc)==null
		   || AttrVal.getProperty(cPayAmt)==null
		   || AttrVal.getProperty(cBisPart)==null
		   || AttrVal.getProperty(cBPAcc)==null)
			return;
		
		//if (!"ERP-01318".equals(AttrVal.getProperty(cDocNum))) return;
		int iPaymentID = 0;
		String sql = "SELECT c_payment_id FROM c_payment"
  			+ " WHERE ad_client_id = " + AD_Client_ID
//  			+ " AND ad_org_id = " + AD_Org_ID
  			+ " AND isactive='Y'"  			
  			+ " AND documentno = '" + AttrVal.getProperty(cDocNum) + "'"
  			+ " AND dateacct = '" + AttrVal.getProperty(cDocDate) + "'";
		if (SingleSQLSelect(sql)) iPaymentID = Integer.valueOf(sRes1);
		
	    int iInvoiceID=0, iChargeID=0, iBPBankAccountID=0;
	    BigDecimal bChargeAmt = null, bTaxAmt=null;

		if (BP.get(AttrVal.getProperty(cBisPart))==null) {
			System.out.println("Payment № "+ AttrVal.getProperty(cDocNum) 
				+". Business Partner not found. ID "+
				AttrVal.getProperty(cBisPart));	return; }
		if (BankAcct.get(AttrVal.getProperty(cSettAcc))==null) 
			{ System.out.println("Payment № "+ AttrVal.getProperty(cDocNum) +". Bank Account not found. ID "+
					AttrVal.getProperty(cSettAcc)+
					BP.get(AttrVal.getProperty(cBisPart)).get(2)); return; }
		if (BPBankAcct.get(AttrVal.getProperty(cBPAcc))==null) { 
			System.out.println("Payment № "+ AttrVal.getProperty(cDocNum) 
					+". Business Partner Bank Account not found. "
					+BP.get(AttrVal.getProperty(cBisPart)).get(1)
					+" "+BP.get(AttrVal.getProperty(cBisPart)).get(2)); }
		else iBPBankAccountID = Integer.valueOf(BPBankAcct.get(AttrVal.getProperty(cBPAcc)).get(0));
		if (childAttrVal.getProperty(cItem)==null) {
			System.out.println("Payment № "+ AttrVal.getProperty(cDocNum) 
				+". Item not found. ID "+
				childAttrVal.getProperty(cItem));	return; }
//		if (Salary.get(childAttrVal.getProperty(cItem)).get(0)!=null)
        iChargeID = findSalary(null,childAttrVal.getProperty("Item"));
        int iBDDS = 0;
        if (BDDS.get(childAttrVal.getProperty("Item"))!=null) iBDDS = Integer.valueOf(BDDS.get(childAttrVal.getProperty("Item")).get(0));
//             Integer.valueOf(Salary.get(childAttrVal.getProperty(cItem)).get(0));
		int iBankAccountID = Integer.valueOf(BankAcct.get(AttrVal.getProperty(cSettAcc)).get(0));
		int iCurrencyID = Integer.valueOf(BankAcct.get(AttrVal.getProperty(cSettAcc)).get(1));
    	int iBPartnerID=Integer.valueOf(BP.get(AttrVal.getProperty(cBisPart)).get(0)); 
        
    	if (iChargeID==0) {
    		switch (iBDDS){
    			case 1000065: //3351 Salary Pay
    			case 1000066:
    				iChargeID = 1000003; 
    				iBPartnerID = 1000005;
    				break;
    			case 1000298: //7211 	
        			iChargeID = 1000030;
        			break;
    			case 1000235: //3311 
    			case 1000240:
    			case 1000305:
    			case 1000073:
    			case 1000142:
    			case 1000244:
    			case 1000149:
    			case 1000249:
    			case 1000143:
    			case 1000227:
    			case 1000242:
    			case 1000211:
    			case 1000191:
    			case 1000253:	
        			iChargeID = 1000024;
        			break;
    			case 1000101: //3121	
        			iChargeID = 1000016;
        			break;
    			case 1000119: //3151
        			iChargeID = 1000019;
        			break;
    			case 1000286: //3231
        			iChargeID = 1000022;
        			break;
    			case 1000097: //3221 
    				iChargeID = 1000021;
    				break;
    			case 1000258: //1621
    				iChargeID =1000012;
    				break;
    			case 1000275: //1252
    				iChargeID = 1000002;
    				break;
    			case 1000238: //1627
    				iChargeID = 1000036;
    				break;
    		}
    	}
    	if (iChargeID==1000005) iBPartnerID = 1000005; // is Salary Pay
        if ((iChargeID == 1000042) | //1613
            	(iChargeID == 1000010) | //1611
            	(iChargeID == 1000024)) //3311 
            	iChargeID = 0;	
    	
        BigDecimal bPayAmt = new BigDecimal(childAttrVal.getProperty(cPayAmt));
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());				

// Is BPartner an Employee? 
//		sql = "SELECT value FROM c_bp_group WHERE c_bp_group_id = " 
//			+ "( SELECT c_bp_group_id FROM c_bpartner "
//			+ "WHERE c_bpartner_id = " + iBPartnerID + " )";
//		if (SingleSQLSelect(sql)) { iChargeID = iSalChargeID;};

    	if( iBankAccountID == 0 || iBPartnerID == 0 || iCurrencyID ==0) return;
		//	New Payment
    	
		MPayment payment = new MPayment (ctx, iPaymentID, sTrxName);
		payment.setAD_Org_ID(AD_Org_ID);
		payment.setDocumentNo(AttrVal.getProperty(cDocNum));
//		payment.setPONum(null);
		
		payment.setTrxType("S"); //TRXTYPE_Sales
		payment.setTenderType("A"); //TENDERTYPE_Check
		payment.setC_BankAccount_ID(iBankAccountID);
		payment.setC_BP_BankAccount_ID(iBPBankAccountID);
		payment.setDescription(AttrVal.getProperty("Content"));
//		payment.setRoutingNo(null);
//		payment.setAccountNo(null);
//		payment.setCheckNo(null);
//		payment.setMicr(null);
//		payment.setCreditCardType(null);
//		payment.setCreditCardNumber(null);
//		payment.setCreditCardExpMM(null);
//		payment.setCreditCardExpYY(null);
//		payment.setCreditCardVV(null);
//		payment.setSwipe(null);
		payment.setDateAcct(timeStampDate);
		payment.setDateTrx(timeStampDate);
		payment.setC_BPartner_ID(iBPartnerID);
		payment.setC_Invoice_ID(iInvoiceID);
		payment.setC_DocType_ID(iAPPDocTypeID);
		payment.setC_Currency_ID(iCurrencyID);
		payment.setC_Charge_ID(iChargeID);
		payment.setChargeAmt(bChargeAmt);
		payment.setTaxAmt(bTaxAmt);
		payment.setPayAmt(bPayAmt);
		payment.setWriteOffAmt(null);
		payment.setDiscountAmt(null);
		payment.setWriteOffAmt(null);
		payment.setERPS_stati_BDDS_ID(iBDDS);
		//payment.setERPS_stati_BDR_ID(iBDR);
		//	Copy statement line reference data
		payment.setA_City(null);
		payment.setA_Country(null);
		payment.setA_EMail(null);
		payment.setA_Ident_DL(null);
		payment.setA_Ident_SSN(null);
		payment.setA_Name(null);
		payment.setA_State(null);
		payment.setA_Street(null);
		payment.setA_Zip(null);
		payment.setR_AuthCode(null);
		payment.setR_Info(null);
		payment.setR_PnRef(null);
		payment.setR_RespMsg(null);
		payment.setR_Result(null);
		payment.setOrig_TrxID(null);
		payment.setVoiceAuthCode(null);
		payment.setDocStatus(p_param.getProperty("DocStatus"));
	    payment.setPosted(false);
		//if (!AttrVal.getProperty(cDocNum).equals("ERP-00001")) return;
		//	Save payment
		if (payment.save())
		{
			if (payment != null )
			{
				payment.setDocAction(p_param.getProperty("DocAction"));
				payment.processIt (p_param.getProperty("DocAction"));
				payment.save();
				System.out.println("Payment "+AttrVal.getProperty(cDocNum)+" saved.");
			}
		}
	return ;	
	}
	/*************** Import Payment from 1C ****************************/	

/************* Import Bank Statement from 1C **************/ 	
	private void import1CBankStatement(String sTrxName) throws ParseException, SQLException
	{
		int i = 0;
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cDocNum)==null
		   || AttrVal.getProperty(cDocDate)==null
		   || AttrVal.getProperty(cBankAcc)==null)
			return;
		System.out.println("BankStatement No:"+AttrVal.getProperty(cDocNum));
		
		//if (!"ERP-239".equals(AttrVal.getProperty(cDocNum))) return;
		//	Detect Duplicates
		String sql = "SELECT c_bankstatement_id FROM c_bankstatement"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'"  			
  			+ " AND name = '" + AttrVal.getProperty(cDocNum) + "'"
  			+ " AND statementdate = '" + AttrVal.getProperty(cDocDate) + "'";
		int iBS = 0;
		if (SingleSQLSelect(sql)) iBS = Integer.valueOf(sRes1);
		
		int lineNo = 10;int iBankAccountID =0;int iCurrencyID=0;
		if (BankAcct.get(AttrVal.getProperty(cBankAcc))!=null) {
		   iBankAccountID = Integer.valueOf(BankAcct.get(AttrVal.getProperty(cBankAcc)).get(0));
		   iCurrencyID = Integer.valueOf(BankAcct.get(AttrVal.getProperty(cBankAcc)).get(1));
		}
		if (iBankAccountID == 0) { Log.log(Level.SEVERE, "Bank Account not found: "
		    +AttrVal.getProperty(cDocNum)
		    +" "+AttrVal.getProperty(cDocDate)); return; 
		}
		int iChargeID = 0;
		int iPaymentID = 0;
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
		
		
		MBankStatement statement = null;
		MBankAccount account = null;

		account = MBankAccount.get (ctx, iBankAccountID);
        if (iBS>0)	statement = new MBankStatement(ctx, iBS, sTrxName);
		//	New Statement
        else statement = new MBankStatement(account);
		statement.setEndingBalance(Env.ZERO);
		statement.setName(AttrVal.getProperty(cDocNum));
		statement.setStatementDate(timeStampDate);
		if ("ID__B___0___0___9657___0___0__________132ERP__".equals(AttrVal.getProperty("Author")))
		  statement.setDescription("Автор документа: пользователь Асем1");
		statement.save();

		for (i=0;i<lAttrVal.size()-1;i++) {
			Properties childAttrVal = lAttrVal.get(i+1);
		//	New StatementLine
			System.out.println("		BankStatement Line No:"+lineNo);
			
			MBankStatementLine line;
			if (iBS==0) line = new MBankStatementLine(statement, lineNo);
			else {
				sql = "SELECT c_bankstatementline_id, c_payment_id FROM c_bankstatementline"
			  			+ " WHERE ad_client_id = " + AD_Client_ID
			  			+ " AND isactive='Y'"  			
			  			+ " AND c_bankstatement_id = '" + iBS + "'"
			  			+ " AND line = '" + lineNo + "'";
					if (SingleSQLSelect(sql)) {
						line = new MBankStatementLine(ctx, Integer.valueOf(sRes1), sTrxName);
						if (sRes2!=null) iPaymentID = Integer.valueOf(sRes2);
					} else 
						line = new MBankStatementLine(statement, lineNo);
			}
			lineNo += 10;
			String sBDR =null, sBPCode = null, sPaymentID = null;
			for (@SuppressWarnings("rawtypes")
			Enumeration e = childAttrVal.propertyNames(); e.hasMoreElements();)
			{
				String sName = childAttrVal.getProperty(e.nextElement().toString());
				if (sName.indexOf("0___8477___0")>0) sBDR = sName; 
				if (sName.indexOf("0___9473___0")>0) sBPCode = sName; 
			}
			iChargeID = findSalary(childAttrVal.getProperty("CorrespondentAccount"),null);
			if ((childAttrVal.getProperty("FinancialPlan")!=null) && 
				( BDDS.get(childAttrVal.getProperty("FinancialPlan"))!=null) &&	
			  ("2.3.3".equals( BDDS.get( childAttrVal.getProperty("FinancialPlan") ).get(1)))) 
				iChargeID = 1000030;
			int iBDDS = 0;
			if (BDDS.get(childAttrVal.getProperty("FinancialPlan"))!=null) 
				iBDDS = Integer.valueOf(BDDS.get(childAttrVal.getProperty("FinancialPlan")).get(0));
			int iBDR = 0;
			if (BDR.get(sBDR)!=null) iBDR = Integer.valueOf(BDR.get(sBDR).get(0));
			BigDecimal bStmtAmt;
			if ("0".equals(childAttrVal.getProperty("Income")))  {
	        	bStmtAmt = new BigDecimal(childAttrVal.getProperty("Expenditure")).negate();
			} else {
	        	bStmtAmt = new BigDecimal(childAttrVal.getProperty("Income"));
			}
			if ((sBPCode!=null) && ( BP.get(sBPCode)!=null)){
				line.setC_BPartner_ID(Integer.valueOf(BP.get(sBPCode).get(0)));
				
			}	
	        if ((iChargeID == 1000042) | //1613
	            	(iChargeID == 1000010) | //1611
	            	(iChargeID == 1000024)) //3311 
	            	iChargeID = 0;	
			iPaymentID = 0;
			if (childAttrVal.getProperty("PrimaryDocument")!=null) {
				sPaymentID = Payments.get(childAttrVal.getProperty("PrimaryDocument").substring(39)).get(0);
				sql = "SELECT c_payment_id FROM c_payment"
						+ " WHERE ad_client_id = " + AD_Client_ID
						+ " AND isactive='Y'"  			
						+ " AND documentno = '" + sPaymentID + "'";
				if (SingleSQLSelect(sql)) iPaymentID = Integer.valueOf(sRes1);
				else {
					iPaymentID = 0;
					  Log.log(Level.SEVERE, "Not found Payment "+ sPaymentID);
				}
			} else {
				sql = "SELECT c_payment_id FROM c_payment"
						+ " WHERE ad_client_id = " + AD_Client_ID
						+ " AND isactive='Y'"  			
						+ " AND c_bpartner_id = " + line.getC_BPartner_ID() 
						+ " AND dateacct = '"+AttrVal.getProperty(cDocDate)+"'"
						+ " AND payamt = '" +childAttrVal.getProperty("Expenditure")+"'";
				if (SingleSQLSelect(sql)) iPaymentID = Integer.valueOf(sRes1);
				else iPaymentID = 0;
			}
			line.setDescription(childAttrVal.getProperty("PaymentPurpose"));
			line.setStatementLineDate(timeStampDate);
			line.setDateAcct(timeStampDate);
			line.setValutaDate(timeStampDate);
			line.setIsReversal(false);
			line.setC_Currency_ID(iCurrencyID);
			line.setStmtAmt(bStmtAmt);
			line.setMemo(null);
			line.setC_Payment_ID(iPaymentID); 
			if (iPaymentID!=0) {
				line.setTrxAmt(bStmtAmt);
				if (iChargeID!=0) {
					int no = 0;				
					sql = "UPDATE c_payment SET c_charge_id = "+iChargeID
						+" WHERE c_payment_id = "+iPaymentID;
					if (!SingleSQLSelect(sql))
						System.out.println("Payment ID "+iPaymentID+ " not updated." + no);
				}	
				/*MPayment payment = new MPayment (ctx, iPaymentID, sTrxName);
				payment.setC_Charge_ID(iChargeID);
				payment.save();*/
			} else {
				if (iChargeID==0) { 
				  Log.log(Level.SEVERE, "Charge_ID == 0: "
					    +AttrVal.getProperty(cDocNum)
					    +" "+AttrVal.getProperty(cDocDate)+" "+(i+1)+
						" " + childAttrVal.getProperty("FinancialPlan") );
				    line = null;
					continue;
			    }
				line.setC_Charge_ID(iChargeID);
				line.setChargeAmt(bStmtAmt);
			}
			line.setERPS_stati_BDDS_ID(iBDDS);
			line.setERPS_stati_BDR_ID(iBDR);
		    
		//	Save statement line
		//System.out.print("Line 1");
		line.save();
		//System.out.println("Line 2");
		line = null;
		}
	}	
	/************* Import Bank Statement from 1C **************/ 	

/************   Import Cash from 1C            **********/	
	private void import1CCash(String sTrxName, String sCashType) throws ParseException, SQLException
	{
		int i = 0;
		Properties AttrVal = lAttrVal.get(0);
		if ( AttrVal.getProperty(cDocNum)==null
		   || AttrVal.getProperty(cDocDate)==null)
			return;
		//System.out.println(AttrVal.getProperty(cDocNum)+" "+i);
		
		//if (!"ERP-138".equals(AttrVal.getProperty(cDocNum))) return;
		//	Detect Duplicates
		String sql = "SELECT c_cash_id FROM c_cash"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'"  			
  			+ " AND name = '" + AttrVal.getProperty(cDocNum) + "'"
  			+ " AND statementdate = '" + AttrVal.getProperty(cDocDate) + "'"
  			+ " AND statementdifference = ";
		BigDecimal bStmtAmt = null;
		if ("CashIn".equals(sCashType)) { 
			bStmtAmt = new BigDecimal(AttrVal.getProperty("Sum"));
			sql+=AttrVal.getProperty("Sum");
		} else if ("CashOut".equals(sCashType)) {
			bStmtAmt = new BigDecimal(AttrVal.getProperty("Sum")).negate();
			sql+="-"+AttrVal.getProperty("Sum");
		}
		int iCashID = 0;
		if (SingleSQLSelect(sql)) iCashID = Integer.valueOf(sRes1);
		
		int iBankAccountID =0;
		int iChargeID = 0;
		int iCashBookID = 0;
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
		
		sql = "SELECT c_cashbook_id FROM c_cashbook"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'";
		if (SingleSQLSelect(sql)) iCashBookID = Integer.valueOf(sRes1);
		else { Log.log(Level.SEVERE, "Could not find C_CASHBOOK_ID");
			return;
		}
		
		String sBDDS = AttrVal.getProperty("IncomeExpenditure"), sDes  = null;
		String sFunds = AttrVal.getProperty("Funds");
		for (@SuppressWarnings("rawtypes")
		Enumeration e = AttrVal.propertyNames(); e.hasMoreElements();)	{
			String sName = AttrVal.getProperty(e.nextElement().toString());
			if (sName.indexOf("0___9383___0")>0) 
				iBankAccountID = Integer.valueOf(BankAcct.get(sName).get(0));
			if ((sBDDS==null) && (sName.indexOf("0___8473___0")>0)) sBDDS = sName; 
		}
		int iBDDS = 0;
		int iDS = 0;
		if (BDDS.get(sBDDS)!=null) iBDDS = Integer.valueOf(BDDS.get(sBDDS).get(0));
		iChargeID = findSalary(AttrVal.getProperty("CorrespondentAccount"),null);
		//	New Cash
		MCash cash = new MCash (ctx, iCashID, sTrxName);
		cash.setEndingBalance(Env.ZERO);
		int iBPartnerID = 0;
		cash.setName(AttrVal.getProperty(cDocNum));
		if ((sFunds!=null) && (DS.get(sFunds)!=null)) { 
			sDes = DS.get(sFunds).get(1) + ". ";
			iDS = Integer.valueOf(DS.get(sFunds).get(0));
		}	
		if (AttrVal.getProperty("TakenFrom")!=null) { sDes+=
			"Принято от: "+AttrVal.getProperty("TakenFrom")+". ";
		sql = "SELECT c_bpartner_id FROM c_bpartner"
	  			+ " WHERE ad_client_id = " + AD_Client_ID
//	  			+ " AND ad_org_id = " + AD_Org_ID
	  			+ " AND isactive='Y' "  			
	  			+ " AND name = '" + AttrVal.getProperty("TakenFrom")+"'";
			if (SingleSQLSelect(sql)) iBPartnerID = Integer.valueOf(sRes1);
			else Log.log(Level.SEVERE, "Business Partner "+AttrVal.getProperty("TakenFrom"));
		}
		if (AttrVal.getProperty("Issue")!=null) { sDes+=
			"Выдать: "+AttrVal.getProperty("Issue")+". ";
			sql = "SELECT c_bpartner_id FROM c_bpartner"
	  			+ " WHERE ad_client_id = " + AD_Client_ID
//	  			+ " AND ad_org_id = " + AD_Org_ID
	  			+ " AND isactive='Y' "  			
	  			+ " AND name = '" + AttrVal.getProperty("Issue")+"'";
			if (SingleSQLSelect(sql)) iBPartnerID = Integer.valueOf(sRes1);
			else Log.log(Level.SEVERE, "Business Partner "+AttrVal.getProperty("Issue"));
		}
		if (AttrVal.getProperty("Base")!=null) sDes+=
			"Основание: "+AttrVal.getProperty("Base")+". ";
		if (AttrVal.getProperty("ByDocument")!=null) sDes+=
			"По документу: "+AttrVal.getProperty("ByDocument")+". ";
		cash.setDescription(sDes);
		cash.setDateAcct(timeStampDate);
		cash.setStatementDate(timeStampDate);
		cash.setC_CashBook_ID(iCashBookID);
		if (iChargeID==1000003) cash.setC_BPartner_ID(1000005); //salary  
		else cash.setC_BPartner_ID(iBPartnerID);
		if (!cash.save()) throw new IllegalStateException("Could not create Cash");
		//	New CashLine
		MCashLine cashLine;
		
		if (iCashID ==0) cashLine = new MCashLine(cash);
		else {
			int iCashLineID = 0;
			sql = "SELECT c_cashline_id FROM c_cashline"
		  			+ " WHERE ad_client_id = " + AD_Client_ID
//		  			+ " AND ad_org_id = " + AD_Org_ID
		  			+ " AND isactive='Y' "  			
		  			+ " AND c_cash_id = '" + iCashID+"'";
			if (SingleSQLSelect(sql)) iCashLineID = Integer.valueOf(sRes1);
			cashLine = new MCashLine(ctx, iCashLineID, sTrxName);
		}
		
		
		cashLine.setAmount(bStmtAmt);
		cashLine.setC_BankAccount_ID(iBankAccountID);
		if ("ID__B___0___0___8473___0___0__________498_____".equals(sBDDS)) {
			cashLine.setCashType("T"); // BankAccountTransfer
			MBankStatement statement = null;
			MBankAccount account = null;
			account = MBankAccount.get (ctx, iBankAccountID);
			//	New Statement
			statement = new MBankStatement(account);
			//statement.setEndingBalance(Env.ZERO);
			statement.setName(AttrVal.getProperty(cDocNum)+" (CASH)");
			statement.setStatementDate(timeStampDate);
			statement.setDescription(sDes);
			statement.save();
			//	New StatementLine
			MBankStatementLine line = new MBankStatementLine(statement, 10);
			sql = "SELECT c_bpartner_id FROM c_bpartner"
		  		+ " WHERE ad_client_id = " + AD_Client_ID
		  		+ " AND isactive='Y'"
		  		+ " AND value like '%620300240882%'";
			if (SingleSQLSelect(sql)) line.setC_BPartner_ID(Integer.valueOf(sRes1));
			line.setDescription(sDes);
			line.setStatementLineDate(timeStampDate);
			line.setDateAcct(timeStampDate);
			line.setValutaDate(timeStampDate);
			line.setIsReversal(false);
			line.setC_Currency_ID(341);
			line.setStmtAmt(bStmtAmt.negate());
			line.setMemo(null);
			line.setTrxAmt(bStmtAmt.negate());
			//	Save statement line
			line.save();
			line = null;
		} else {
			cashLine.setCashType("C"); // Charge
			if (iChargeID==0) { 
				System.out.println("Error,Charge_ID == 0 "+AttrVal.getProperty(cDocNum)+" "+(i+1)+
						" " + AttrVal.getProperty("FinancialPlan") );
				iChargeID = findSalary(AttrVal.getProperty("CorrespondentAccount"),null);
			}
			cashLine.setC_Charge_ID(iChargeID); 
		}	
//			cashLine.setC_Currency_ID(m_C_Currency_ID);
		cashLine.setDescription(sDes);
		cashLine.setERPS_stati_BDDS_ID(iBDDS);
		cashLine.setERPS_denejnie_sredstva_ID(iDS);
//		cashLine.setERPS_stati_BDR_ID(iBDR);
		if (!cashLine.save()) 
			throw new IllegalStateException("Could not create Cash line (Charge)");
	}	
	/************   Import Cash from 1C            **********/	

/************* Import Received Invoice from 1C **************/ 	
	private void import1CRecInv(String sTrxName) throws ParseException, SQLException
	{
		int i = 0;
		Properties AttrVal = lAttrVal.get(0);
		Properties childAttrVal = lAttrVal.get(1);
		if ( AttrVal.getProperty(cDocNum)==null
		   || AttrVal.getProperty(cDocDate)==null
		   || AttrVal.getProperty("Subconto1")==null)
			return;
		
		//if (!"ERP-00512".equals(AttrVal.getProperty(cDocNum))) return;
		System.out.println("Received Invoice: "+AttrVal.getProperty(cDocNum));
		//	Detect Duplicates
		String sql = "SELECT c_invoice_id, c_order_id FROM c_invoice"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'"  			
  			+ " AND documentno = '" + AttrVal.getProperty(cDocNum) + "'"
  			+ " AND dateinvoiced = '" + AttrVal.getProperty("InvoiceDate") + "'";
		int iInvoiceID = 0; 
		int iOrderID = 0;
		if (SingleSQLSelect(sql)) { iInvoiceID = Integer.valueOf(sRes1);
		   iOrderID = Integer.valueOf(sRes2);
		}
		int lineNo = 10;
		int iChargeID = 0;
		int iProductID = 0;
		int iBPartnerID = 0;
		//String sBDR = null; 
		String sBPCode = null;
		sBPCode = AttrVal.getProperty("Subconto1");
		if ((sBPCode!=null) && ( BP.get(sBPCode)!=null))
			iBPartnerID = Integer.valueOf(BP.get(sBPCode).get(0));
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty("InvoiceDate"));
		java.sql.Timestamp DateInvoiced = new Timestamp(date.getTime());
		date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp DateAcct = new Timestamp(date.getTime());
		
		MInvoice invoice = new MInvoice(ctx, iInvoiceID, null);
		invoice.setClientOrg (AD_Client_ID, AD_Org_ID);
		invoice.setC_DocTypeTarget_ID(iAPIDocTypeID);
		invoice.setIsSOTrx(false);
		invoice.setDocumentNo(AttrVal.getProperty(cDocNum));
		invoice.setNo_Schet_Faktura(AttrVal.getProperty("InvoiceNum"));
		String sContract = AttrVal.getProperty("Subconto2");
		if (sContract==null || sContract.indexOf("0___9420___0")<0) sContract = AttrVal.getProperty("Subconto3");
		String sOrderNum = null;
		if (sContract==null) sOrderNum = "БЕЗ ДОГОВОРА "+AttrVal.getProperty(cDocDate)
				+". СФ № "+AttrVal.getProperty("InvoiceNum");
		else if (PurOrder.get(sContract)!=null) sOrderNum=PurOrder.get(sContract).get(3);
		if ("Без договора".equals(sOrderNum)) sOrderNum = "БЕЗ ДОГОВОРА "+AttrVal.getProperty(cDocDate)
				+". СФ № "+AttrVal.getProperty("InvoiceNum");
		if (iOrderID==0) {
		sql = "SELECT c_order_id FROM c_order" +
				" WHERE ad_client_id = " + AD_Client_ID
	  			+ " AND isactive='Y'"  			
	  			+ " AND documentno = '" + sOrderNum + "'"
	  			+ " AND c_doctype_id = '" + iPODocTypeID + "'"
				+ " AND c_bpartner_id = '" + iBPartnerID + "'";
			if (SingleSQLSelect(sql)) 
				iOrderID += Integer.valueOf(sRes1);
		}	
		if (iOrderID==0 && sContract!=null && PurOrder.get(sContract)!=null 
            && PurOrder.get(sContract).get(0)!=null)
			iOrderID = Integer.valueOf(PurOrder.get(sContract).get(0));
		MOrder order = new MOrder(ctx, iOrderID, null);
		order.setDocStatus("DR");
		order.setClientOrg (AD_Client_ID, AD_Org_ID);
		order.setC_DocTypeTarget_ID(iPODocTypeID);
		order.setC_DocType_ID(iPODocTypeID);
		order.setIsSOTrx(false);
		order.setDocumentNo(sOrderNum);
		order.setC_BPartner_ID(iBPartnerID);
		order.setDescription(sOrderNum);
		order.setProcessed(false);
		order.setDateOrdered(DateInvoiced);
		order.setDatePromised(DateInvoiced);
		order.setM_Warehouse_ID(1000000);
		order.setSalesRep_ID(1000000);
		//order.setDateAcct(DateAcct);
		order.save();
		iOrderID = order.getC_Order_ID();
		invoice.setC_Order_ID(iOrderID);
		invoice.setC_BPartner_ID(iBPartnerID);
		//invoice.setC_BPartner_Location_ID(imp.getC_BPartner_Location_ID());
		//invoice.setAD_User_ID(imp.getAD_User_ID());
		invoice.setDescription(childAttrVal.getProperty("Content"));
		//invoice.setC_PaymentTerm_ID(imp.getC_PaymentTerm_ID());
		//invoice.setM_PriceList_ID(imp.getM_PriceList_ID());
		//invoice.setSalesRep_ID(imp.getSalesRep_ID());
		//invoice.setAD_OrgTrx_ID(imp.getAD_OrgTrx_ID());
		//invoice.setC_Activity_ID(imp.getC_Activity_ID());
		//invoice.setC_Campaign_ID(imp.getC_Campaign_ID());
		//invoice.setC_Project_ID(imp.getC_Project_ID());
		invoice.setProcessed(false);
		invoice.setDateInvoiced(DateInvoiced);
		invoice.setDateAcct(DateAcct);
		invoice.save();
		lineNo = 10;
		
		for (i=0;i<lAttrVal.size()-1;i++) {
			childAttrVal = lAttrVal.get(i+1);
				
			//	New Invoice Line
			MInvoiceLine line;
			if (iInvoiceID == 0) line = new MInvoiceLine(invoice);
			else { 
				sql = "SELECT c_invoiceline_id FROM c_invoiceline"
			  			+ " WHERE ad_client_id = " + AD_Client_ID
			  			+ " AND isactive='Y'"  			
			  			+ " AND c_invoice_id = '" + iInvoiceID + "'"
			  			+ " AND line = '" + lineNo + "'";
					if (SingleSQLSelect(sql)) 
						line = new MInvoiceLine(ctx, Integer.valueOf(sRes1), sTrxName);
					else line = new MInvoiceLine(invoice);
			}	
			line.setDescription(childAttrVal.getProperty("Content"));
			line.setLine(lineNo);
			if (childAttrVal.getProperty("Subconto1Db").indexOf("0___9574___0")>0 //asset
				|| childAttrVal.getProperty("Subconto1Db").indexOf("0___9485___0")>0	) {// material
				if (Product.get(childAttrVal.getProperty("Subconto1Db"))!=null)
					iProductID = Integer.valueOf(Product.get(childAttrVal.getProperty("Subconto1Db")).get(0));
				line.setM_Product_ID(iProductID, true);
			} else {
				iChargeID = findSalary(childAttrVal.getProperty("CorrespondentAccountDebit"),null);
				line.setC_Charge_ID(iChargeID);
			}	
			BigDecimal bdQuantity = new BigDecimal("1");
			if (!"0".equals(childAttrVal.getProperty("Quantity")))
				   bdQuantity = new BigDecimal(childAttrVal.getProperty("Quantity"));

			BigDecimal bdPrice = new BigDecimal(childAttrVal.getProperty("SumWithoutNDS"));
			bdPrice.divide(bdQuantity,5);
			BigDecimal bdVAT = new BigDecimal(childAttrVal.getProperty("NDS"));
			line.setPrice(bdPrice);
			line.setQty(bdQuantity);
			line.setTaxAmt(bdVAT);
			String sBDR = childAttrVal.getProperty("Subconto1Db");
			int iBDRID = 0;
			if (BDR.get(sBDR)!= null) iBDRID = Integer.valueOf(BDR.get(sBDR).get(0));
			line.setERPS_stati_BDR_ID(iBDRID);
			if (VAT.get(childAttrVal.getProperty("RateNDS"))!=null)
				line.setC_Tax_ID(Integer.valueOf(VAT.get(childAttrVal.getProperty("RateNDS")).get(0)));
			//BigDecimal taxAmt = imp.getTaxAmt();
			//line.setTaxAmt(taxAmt);
			line.setName(AttrVal.getProperty(cDocNum));
			
			int iOrderLineID = 0;
			if (iOrderID>0) {
				sql = "SELECT c_orderline_id FROM c_orderline"
			  			+ " WHERE ad_client_id = " + AD_Client_ID
			  			+ " AND isactive='Y'"  			
			  			+ " AND c_order_id = '" + iOrderID + "'"
			  			+ " AND dateordered = '" + AttrVal.getProperty("InvoiceDate") + "'";
				if (SingleSQLSelect(sql)) iOrderLineID = Integer.valueOf(sRes1); 
				else {
					order = new MOrder(ctx, iOrderID, null);
					order.setDocStatus("DR");
					order.setClientOrg (AD_Client_ID, AD_Org_ID);
					order.save();
					int olineNo = 0;
					sql = "SELECT MAX(line) FROM c_orderline"
				  			+ " WHERE ad_client_id = " + AD_Client_ID
				  			+ " AND isactive='Y'"  			
				  			+ " AND c_order_id = '" + iOrderID + "'";
					if (SingleSQLSelect(sql)) olineNo = Integer.valueOf(sRes1); 
					olineNo+=10;
					MOrderLine orderline = new MOrderLine(order);
					
					orderline.setLine(olineNo);
					orderline.setDescription(childAttrVal.getProperty("Content"));
					orderline.setPrice(bdPrice);
					orderline.setQty(bdQuantity);
					orderline.setDateOrdered(DateInvoiced);
					orderline.setDatePromised(DateInvoiced);
					//line.setFreightAmt(bdVAT);
					if (VAT.get(childAttrVal.getProperty("RateNDS"))!=null)
						orderline.setC_Tax_ID(Integer.valueOf(VAT.get(childAttrVal.getProperty("RateNDS")).get(0)));
//					line.setName(AttrVal.getProperty(cDocNum));
					orderline.save();
					iOrderLineID = orderline.getC_OrderLine_ID();
				}
			}	
			line.setC_OrderLine_ID(iOrderLineID);
			line.save();
			lineNo += 10;
			if (invoice != null) {
				//invoice.processIt (p_DocAction);
				invoice.save();
			}
		}	
	}	
/************* Import Received Invoice from 1C **************/ 	
	
	/************* Import Issued Invoice from 1C **************/ 	
	private void import1CIssInv(String sTrxName) throws ParseException, SQLException
	{
		int i = 0;
		Properties AttrVal = lAttrVal.get(0);
		Properties childAttrVal = lAttrVal.get(1);
		if ( AttrVal.getProperty(cDocNum)==null
		   || AttrVal.getProperty(cID)==null)
			return;
		//System.out.println(AttrVal.getProperty(cDocNum)+" "+i);
		//if (!"09000028".equals(AttrVal.getProperty(cDocNum))) return;
		//	Detect Duplicates
		String sql = "SELECT c_invoice_id FROM c_invoice"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'"  			
  			+ " AND documentno = '" + AttrVal.getProperty(cDocNum) + "'"
  			+ " AND dateinvoiced = '" + AttrVal.getProperty(cDocDate) + "'";
		int iInvoiceID = 0; 
		if (SingleSQLSelect(sql)) iInvoiceID = Integer.valueOf(sRes1);
		
		int lineNo = 10;
		int iChargeID = 0;
		//int iProductID = 0;
		String sBDR = null;
		String sBPCode = null;
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp DateInvoiced = new Timestamp(date.getTime());
		//date = (Date)formatter.parse(AttrVal.getProperty("EntryDate"));
		java.sql.Timestamp DateAcct = new Timestamp(date.getTime());
		
		MInvoice invoice = new MInvoice(ctx, iInvoiceID, null);
		invoice.setClientOrg (AD_Client_ID, AD_Org_ID);
		invoice.setC_DocTypeTarget_ID(iARIDocTypeID);
		invoice.setIsSOTrx(true);
		invoice.setDocumentNo(AttrVal.getProperty(cDocNum));
		invoice.setNo_Schet_Faktura(AttrVal.getProperty("InvoiceNum"));
		sBPCode = AttrVal.getProperty("BusinessPartner");
		if ((sBPCode!=null) && ( BP.get(sBPCode)!=null))
			invoice.setC_BPartner_ID(Integer.valueOf(BP.get(sBPCode).get(0)));
		else {		
			Log.log(Level.SEVERE, "BP not found: "+sBPCode);
			return;}
		//invoice.setC_BPartner_Location_ID(imp.getC_BPartner_Location_ID());
		//invoice.setAD_User_ID(imp.getAD_User_ID());
		invoice.setDescription(AttrVal.getProperty("Addition"));
		//invoice.setC_PaymentTerm_ID(imp.getC_PaymentTerm_ID());
		//invoice.setM_PriceList_ID(imp.getM_PriceList_ID());
		//invoice.setSalesRep_ID(imp.getSalesRep_ID());
		//invoice.setAD_OrgTrx_ID(imp.getAD_OrgTrx_ID());
		//invoice.setC_Activity_ID(imp.getC_Activity_ID());
		//invoice.setC_Campaign_ID(imp.getC_Campaign_ID());
		//invoice.setC_Project_ID(imp.getC_Project_ID());
		invoice.setProcessed(false);
		invoice.setDateInvoiced(DateInvoiced);
		invoice.setDateAcct(DateAcct);
		//if (AttrVal.getProperty("BaseDoc")!=null) { 
			sql = "SELECT c_order_id, c_project_id FROM c_order"
	  			+ " WHERE ad_client_id = " + AD_Client_ID
	  			+ " AND isactive='Y'"  			
	  			+ " AND dateordered = '" + AttrVal.getProperty(cDocDate) + "'"
	  			+ " AND c_bpartner_id = '" + BP.get(sBPCode).get(0) + "'";
			int iOrderID = 0, iProjectID = 0; 
			if (SingleSQLSelect(sql)) { iOrderID = Integer.valueOf(sRes1);
			  if (sRes2!=null) iProjectID = Integer.valueOf(sRes2);
			}
			invoice.setC_Order_ID(iOrderID);
			invoice.setDateOrdered(DateInvoiced);
			invoice.setC_Project_ID(iProjectID);
		//}	

		invoice.save();
		lineNo = 10;
		
		for (i=0;i<lAttrVal.size()-1;i++) {
			childAttrVal = lAttrVal.get(i+1);
			
			/*for (Enumeration e = childAttrVal.propertyNames(); e.hasMoreElements();) {
				String sName = childAttrVal.getProperty(e.nextElement().toString());
				if (sName.indexOf("0___8477___0")>0) sBDR = sName; 
				if (sName.indexOf("0___9473___0")>0) sBPCode = sName; 
			}*/
			//	New Invoice Line
			MInvoiceLine line;
			if (iInvoiceID == 0) line = new MInvoiceLine(invoice);
			else { 
				sql = "SELECT c_invoiceline_id FROM c_invoiceline"
			  			+ " WHERE ad_client_id = " + AD_Client_ID
			  			+ " AND isactive='Y'"  			
			  			+ " AND c_invoice_id = '" + iInvoiceID + "'"
			  			+ " AND line = '" + lineNo + "'";
					if (SingleSQLSelect(sql)) 
						line = new MInvoiceLine(ctx, Integer.valueOf(sRes1), sTrxName);
					else line = new MInvoiceLine(invoice);
			}	
			sBDR = childAttrVal.getProperty("FinancialIncome");
			line.setLine(lineNo);
			line.setDescription(childAttrVal.getProperty("Name1"));
			iChargeID = findSalary(childAttrVal.getProperty("CorrespondentAccount"),null);
			line.setC_Charge_ID(iChargeID);
			BigDecimal bdQuantity = new BigDecimal("1");
			if (!"0".equals(childAttrVal.getProperty("Quantity")))
				   bdQuantity = new BigDecimal(childAttrVal.getProperty("Quantity"));

			BigDecimal bdPrice = new BigDecimal(childAttrVal.getProperty("Price"));
			BigDecimal bdVAT = new BigDecimal(childAttrVal.getProperty("NDS"));
			line.setPrice(bdPrice);
			line.setQty(bdQuantity);
			line.setTaxAmt(bdVAT);
			int iBDRID = 0;
			if (BDR.get(sBDR)!= null) iBDRID = Integer.valueOf(BDR.get(sBDR).get(0));
			line.setERPS_stati_BDR_ID(iBDRID);
			//line.setM_Product_ID(imp.getM_Product_ID(), true);
			//line.setC_Activity_ID(imp.getC_Activity_ID());
			//line.setC_Campaign_ID(imp.getC_Campaign_ID());
			//line.setC_Project_ID(imp.getC_Project_ID());
			//if ("ID__B___0___0___9701___0___0__________139IT___".equals())
				
			//line.setTax();
			
			line.setC_Tax_ID(Integer.valueOf(VAT.get(childAttrVal.getProperty("RateNDS")).get(0)));
			//BigDecimal taxAmt = imp.getTaxAmt();
			//line.setTaxAmt(taxAmt);
			line.setName(AttrVal.getProperty(cDocNum));
			line.save();
			lineNo += 10;
			if (invoice != null) {
				//invoice.processIt (p_DocAction);
				invoice.save();
			}		
		}
	}	
/************* Import Issued Invoice from 1C **************/ 	

/************* Import Sales Orders from 1C **************/ 	
	private void import1CSalOrd(String sTrxName) throws ParseException, SQLException
	{
		int i = 0;
		Properties AttrVal = lAttrVal.get(0);
		Properties childAttrVal = lAttrVal.get(1);
		if ( AttrVal.getProperty(cDocNum)==null
		   || AttrVal.getProperty(cID)==null)
			return;
		//System.out.println(AttrVal.getProperty(cDocNum)+" "+i);
		//if (!"ERP-028".equals(AttrVal.getProperty(cDocNum))) return;
		//	Detect Duplicates
		String sql = "SELECT c_order_id FROM c_order"
  			+ " WHERE ad_client_id = " + AD_Client_ID
  			+ " AND isactive='Y'"  			
  			+ " AND documentno = '" + AttrVal.getProperty(cDocNum) + "'"
  			+ " AND dateordered = '" + AttrVal.getProperty(cDocDate) + "'";
		int iOrderID = 0; 
		if (SingleSQLSelect(sql)) iOrderID = Integer.valueOf(sRes1);
		
		int lineNo = 10;
		//int iChargeID = 0;
		//int iProductID = 0;
		//String sBDR = null; 
		String sBPCode = null;
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp DateOrdered = new Timestamp(date.getTime());
		//date = (Date)formatter.parse(AttrVal.getProperty("EntryDate"));
		//java.sql.Timestamp DateAcct = new Timestamp(date.getTime());
		
		MOrder order = new MOrder(ctx, iOrderID, null);
		order.setClientOrg (AD_Client_ID, AD_Org_ID);
		order.setC_DocTypeTarget_ID(iSODocTypeID);
		order.setC_DocType_ID(iSODocTypeID);
		order.setIsSOTrx(true);
		order.setDocumentNo(AttrVal.getProperty(cDocNum));
		sBPCode = AttrVal.getProperty("BusinessPartner");
		if ((sBPCode!=null) && ( BP.get(sBPCode)!=null))
			order.setC_BPartner_ID(Integer.valueOf(BP.get(sBPCode).get(0)));
		else {		
			Log.log(Level.SEVERE, "BP not found: "+sBPCode);
			return;}
		order.setDescription(AttrVal.getProperty("NameServices"));
		order.setProcessed(false);
		order.setDateOrdered(DateOrdered);
		order.setDatePromised(DateOrdered);
		order.setM_Warehouse_ID(1000000);
		order.setSalesRep_ID(1000000);
		//order.setDateAcct(DateAcct);
		order.save();
		lineNo = 10;
		
		for (i=0;i<lAttrVal.size()-1;i++) {
			childAttrVal = lAttrVal.get(i+1);
			
			/*for (Enumeration e = childAttrVal.propertyNames(); e.hasMoreElements();) {
				String sName = childAttrVal.getProperty(e.nextElement().toString());
				if (sName.indexOf("0___8477___0")>0) sBDR = sName; 
				if (sName.indexOf("0___9473___0")>0) sBPCode = sName; 
			}*/
			//	New Invoice Line
			MOrderLine line;
			if (iOrderID == 0) line = new MOrderLine(order);
			else { 
				sql = "SELECT c_orderline_id FROM c_orderline"
			  			+ " WHERE ad_client_id = " + AD_Client_ID
			  			+ " AND isactive='Y'"  			
			  			+ " AND c_order_id = '" + iOrderID + "'"
			  			+ " AND line = '" + lineNo + "'";
					if (SingleSQLSelect(sql)) 
						line = new MOrderLine(ctx, Integer.valueOf(sRes1), sTrxName);
					else line = new MOrderLine(order);
			}	
			line.setLine(lineNo);
			line.setDescription(childAttrVal.getProperty("Name1"));
			BigDecimal bdQuantity = new BigDecimal("1");
			if (!"0".equals(childAttrVal.getProperty("Quantity")))
				   bdQuantity = new BigDecimal(childAttrVal.getProperty("Quantity"));

			BigDecimal bdPrice = new BigDecimal(childAttrVal.getProperty("InAll"));
			//BigDecimal bdVAT = new BigDecimal(childAttrVal.getProperty("NDS"));
			line.setPrice(bdPrice);
			line.setQty(bdQuantity);
			//line.setFreightAmt(bdVAT);
			
			line.setC_Tax_ID(Integer.valueOf(VAT.get(childAttrVal.getProperty("RateNDS")).get(0)));
//			line.setName(AttrVal.getProperty(cDocNum));
			line.save();
			lineNo += 10;
			if (order != null) {
				//invoice.processIt (p_DocAction);
				order.save();
			}		
		}
	}	
/************* Import Issued Invoice from 1C **************/ 	

	/************* Import General Ledger from 1C **************/ 	
	private void import1CGL(String sTrxName, String sDocType) throws ParseException, SQLException
	{
		if (lAttrVal.size()<2) return;
		Properties AttrVal = lAttrVal.get(0);
		//Properties childAttrVal = lAttrVal.get(1);
		if ( AttrVal.getProperty(cID)==null)
			return;
		
//********** batch
		StringBuffer sDocNum = new StringBuffer("ERP-"
			+"20"+AttrVal.getProperty(cDocDate).substring(6,8)
			+AttrVal.getProperty(cDocDate).substring(3,5));
		int C_DocType_ID = 0;
		StringBuffer sPriDocNum = new StringBuffer("");
		sPriDocNum.setLength(0);
		if ((Advance.get(AttrVal.getProperty(cID))!=null)&&
			   (Advance.get(AttrVal.getProperty(cID)).get(1)!=null)) {
			sPriDocNum.append(Advance.get(AttrVal.getProperty(cID)).get(1)); 
			C_DocType_ID = 1000049; sDocNum.append("A");} 
		else if ((Closing.get(AttrVal.getProperty(cID))!=null)&&
				(Closing.get(AttrVal.getProperty(cID)).get(1)!=null)) {
			sPriDocNum.append(Closing.get(AttrVal.getProperty(cID)).get(1));
			C_DocType_ID = 1000048; sDocNum.append("C");}
		else if ((Operation.get(AttrVal.getProperty(cID))!=null)&&
				(Operation.get(AttrVal.getProperty(cID)).get(1)!=null)) {
			sPriDocNum.append(Operation.get(AttrVal.getProperty(cID)).get(1));
			C_DocType_ID = 1000047; sDocNum.append("O");
		}	   	

		//if (!"ERP-011".equals(sPriDocNum.toString())) return;
		int iJBID = 0;
		StringBuffer sql = new StringBuffer ();
		if (pJournalBatch.getProperty(sDocNum.toString())!=null)
				iJBID = Integer.valueOf(pJournalBatch.getProperty(sDocNum.toString()));
        if (iJBID < 1 ) {  
        	 sql.append("SELECT GL_JournalBatch_ID FROM GL_JournalBatch "
        					+ " WHERE DocumentNo = "+ "'"+sDocNum+"'");
        	if (SingleSQLSelect(sql.toString())) iJBID = Integer.valueOf(sRes1);
        }
		if (iJBID<0) iJBID = 0;
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy");
		Date date = (Date)formatter.parse(AttrVal.getProperty(cDocDate));
		java.sql.Timestamp DateAcct = new Timestamp(date.getTime());

		MJournalBatch batch = new MJournalBatch (ctx, iJBID, sTrxName);
		batch.setClientOrg(AD_Client_ID, AD_Org_ID);
		batch.setDocumentNo (sDocNum.toString());
		batch.setDescription("Import from 1C. "+AttrVal.getProperty("Description"));
		batch.setC_DocType_ID(C_DocType_ID);
		batch.setGL_Category_ID (1000002);
		batch.setPostingType("A");
		batch.setDateAcct(DateAcct);
		batch.setDateDoc (DateAcct);
		batch.setC_Currency_ID(341);
		if (!batch.save())
			Log.log(Level.SEVERE, "Batch not saved");
		if (pJournalBatch.getProperty(sDocNum.toString())==null)
			pJournalBatch.setProperty(sDocNum.toString(), 
					String.valueOf(batch.getGL_JournalBatch_ID()));
//************ batch

//************ journal
		int iJID = 0;
		sql.setLength(0);
		sql.append("SELECT GL_Journal_ID FROM GL_Journal "
  	      + " WHERE DocumentNo = "+"'"+ sPriDocNum+"'"
  	      + " AND  GL_JournalBatch_ID = "+ batch.getGL_JournalBatch_ID());
		if (SingleSQLSelect(sql.toString())) 
		   iJID = Integer.valueOf(sRes1);
		if (iJID<0) iJID = 0;
		MJournal journal = new MJournal (ctx, iJID, sTrxName);
		journal.setGL_JournalBatch_ID(batch.getGL_JournalBatch_ID());
		journal.setClientOrg(AD_Client_ID, AD_Org_ID);
		//
		journal.setDescription (batch.getDescription());
		journal.setDocumentNo (sPriDocNum.toString());
		//
		journal.setC_AcctSchema_ID (1000000);
		journal.setC_DocType_ID (C_DocType_ID);
		journal.setGL_Category_ID (batch.getGL_Category_ID());
		journal.setPostingType (batch.getPostingType());
		//journal.setGL_Budget_ID(batch.getGL_Category_ID());
		//
		journal.setCurrency (batch.getC_Currency_ID(), 114, new BigDecimal("1"));
		//
		//journal.setC_Period_ID(batch.getC_Period_ID());
		journal.setDateAcct(batch.getDateAcct());		//	sets Period if not defined
		journal.setDateDoc (batch.getDateDoc());
		//
		if (!journal.save())
		{
			Log.log(Level.SEVERE, "Journal not saved");
		} System.out.println(batch.getDocumentNo()+ " " +journal.getDocumentNo());
		

//************ Lines		
		int i=0;
		int iBDR, iBDDS, iDS;
		for (i=0;i<lAttrVal.size()-1;i++) {
			Properties childAttrVal = lAttrVal.get(i+1);
			if(    (childAttrVal.getProperty("AccountD")==null) 
				|| (Account.get(childAttrVal.getProperty("AccountD"))==null)
				|| (childAttrVal.getProperty("AccountC")==null) 
				|| (Account.get(childAttrVal.getProperty("AccountC"))==null)
					) continue;
			//	Lines
			int iJLID = 0, iLine = i*20+10;
			iBDR = iBDDS = iDS = 0;			
			sql.setLength(0);
			sql.append("SELECT GL_JournalLine_ID FROM GL_JournalLine "
	  	      + " WHERE GL_Journal_ID = "+journal.get_ID()
	  	      + " AND line = "+ iLine);
			if (SingleSQLSelect(sql.toString()))
			    iJLID = Integer.valueOf(sRes1);
			MJournalLine line;
			if (iJLID>0) line = new MJournalLine (ctx,iJLID,sTrxName);
			else line = new MJournalLine (journal);
			//
			line.setDescription(journal.getDescription());
			line.setCurrency (journal.getC_Currency_ID(), journal.getC_ConversionType_ID(), 
					journal.getCurrencyRate());
			sql.setLength(0);
			String sAcc = null;
			String sBP = null;
			
			for (@SuppressWarnings("rawtypes")
			Enumeration e = childAttrVal.propertyNames(); e.hasMoreElements();)	{
				String sElement = e.nextElement().toString();
				String sName = childAttrVal.getProperty(sElement);
				if ((  sName.indexOf("0___9473___0")>0
					|sName.indexOf("0___9697___0")>0)
					&sName.indexOf("9473___0___0________16036ERP")<0) { 
					sBP = sName;
				} if (sElement.indexOf("contoC")>0) {
						continue;
				} else if (sName.indexOf("0___8477___0")>0) {
					if (BDR.get(sName)!= null) iBDR = Integer.valueOf(BDR.get(sName).get(0));
				} else if (sName.indexOf("0___9410___0")>0) {
					if (DS.get(sName)!= null) iDS = Integer.valueOf(DS.get(sName).get(0));
				} else if (sName.indexOf("0___8473___0")>0) {
					if (BDDS.get(sName)!= null) iBDDS = Integer.valueOf(BDDS.get(sName).get(0));
				}
			}

			if((childAttrVal.getProperty("AccountD")!=null) &&
					(Account.get(childAttrVal.getProperty("AccountD"))!=null))
					sAcc = childAttrVal.getProperty("AccountD");

			iJLID = findValidCombination(sAcc,sBP);
			if(iJLID<1) { 
				System.out.print("Combination not found! Sum:"+
						  	childAttrVal.getProperty("Sum")+" Line:"+ (i+1)); 
				if ((sAcc!=null) && (Account.get(sAcc)!=null)) 
					System.out.println(" Account: "+ Account.get(sAcc).get(2)
							+" " +BP.get(sBP).get(2));
				else System.out.println("");
				continue; 
			}
			line.setC_ValidCombination_ID(iJLID);
			line.setLine (iLine);
			BigDecimal bSum;
			if (childAttrVal.getProperty("Sum")!=null)
				bSum = new BigDecimal(childAttrVal.getProperty("Sum"));
			else bSum = new BigDecimal("0");
			line.setAmtSourceDr (bSum);
			//line.setAmtAcct (bSum, bSum);	//	only if not 0
			line.setDateAcct (journal.getDateAcct());
			//
			line.setERPS_stati_BDR_ID(iBDR);
			line.setERPS_denejnie_sredstva_ID(iDS);
			line.setERPS_stati_BDDS_ID(iBDDS);
			//line.setC_UOM_ID(imp.getC_UOM_ID());
			line.setQty(new BigDecimal("0"));
			//
			if (!line.save()) {
				Log.log(Level.SEVERE, "Journal Line not saved");
			} 	
			
			iLine= (i+1)*20;
			sql.setLength(0);
			iJLID = 0;
			iBDR = iBDDS = iDS = 0;			
			sql.append("SELECT GL_JournalLine_ID FROM GL_JournalLine "
	  	      + " WHERE GL_Journal_ID = "+journal.get_ID()
	  	      + " AND line = "+ iLine);
			if(SingleSQLSelect(sql.toString())) iJLID = Integer.valueOf(sRes1);
			if (iJLID>0) line = new MJournalLine (ctx,iJLID,sTrxName);
			else line = new MJournalLine (journal);
			//
			line.setDescription(journal.getDescription());
			line.setCurrency (journal.getC_Currency_ID(), journal.getC_ConversionType_ID(), 
					journal.getCurrencyRate());
			sAcc = null;
			sBP = null;
			for (@SuppressWarnings("rawtypes")
			Enumeration e = childAttrVal.propertyNames(); e.hasMoreElements();)	{
				String sElement = e.nextElement().toString();
				String sName = childAttrVal.getProperty(sElement);
				if ((  sName.indexOf("0___9473___0")>0
						|sName.indexOf("0___9697___0")>0)
						&sName.indexOf("9473___0___0________16036ERP")<0) { 
						sBP = sName;
				} if (sElement.indexOf("contoD")>0) {
					continue;
				} else if (sName.indexOf("0___8477___0")>0) {
					if (BDR.get(sName)!= null) iBDR = Integer.valueOf(BDR.get(sName).get(0));
				} else if (sName.indexOf("0___9410___0")>0) {
					if (DS.get(sName)!= null) iDS = Integer.valueOf(DS.get(sName).get(0));
				} else if (sName.indexOf("0___8473___0")>0) {
					if (BDDS.get(sName)!= null) iBDDS = Integer.valueOf(BDDS.get(sName).get(0));
				}
			}
			
			if((childAttrVal.getProperty("AccountC")!=null) &&
				(Account.get(childAttrVal.getProperty("AccountC"))!=null))
				sAcc = childAttrVal.getProperty("AccountC");
			iJLID = findValidCombination(sAcc,sBP);
			if(iJLID<1) { System.out.print("Combination not found! Sum:"+
					   childAttrVal.getProperty("Sum")+" Line:"+(i+1)); 
				if ((sAcc!=null) && (Account.get(sAcc)!=null)) 
					System.out.println(" Account: "+ Account.get(sAcc).get(2)
							+" " +BP.get(sBP).get(2));
				else System.out.println("");
				continue; 
			}
			line.setC_ValidCombination_ID(iJLID);
			line.setLine (iLine);
			line.setAmtSourceCr (bSum);
			line.setDateAcct (journal.getDateAcct());
			line.setERPS_stati_BDR_ID(iBDR);
			line.setERPS_denejnie_sredstva_ID(iDS);
			line.setERPS_stati_BDDS_ID(iBDDS);
			//line.setC_UOM_ID(imp.getC_UOM_ID());
			line.setQty(new BigDecimal("0"));
			if (!line.save()) {
				Log.log(Level.SEVERE, "Journal Line not saved");
			} 	
			
			
		}	
	}	
/************* Import Issued Invoice from 1C **************/	
}