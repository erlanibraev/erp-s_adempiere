/**********************************************************************
* This file is part of Adempiere ERP Bazaar                           *
* http://www.adempiere.org                                            *
*                                                                     *
* Copyright (C) Duman Zhunissov                                       *
*                                                                     *
* This program is free software; you can redistribute it and/or       *
* modify it under the terms of the GNU General Public License         *
* as published by the Free Software Foundation; either version 2      *
* of the License, or (at your option) any later version.              *
*                                                                     *
* This program is distributed in the hope that it will be useful,     *
* but WITHOUT ANY WARRANTY; without even the implied warranty of      *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the        *
* GNU General Public License for more details.                        *
*                                                                     *
* You should have received a copy of the GNU General Public License   *
* along with this program; if not, write to the Free Software         *
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,          *
* MA 02110-1301, USA.                                                 *
*                                                                     *
* Contributors:                                                       *
* - Duman Zhunissov                                                   *
*                                                                     *
* Sponsor:                                                           *
* - "ERP-Service "KazTransCom" LLP                                    *
**********************************************************************/
package org.compiere.FA;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.adempiere.process.rpl.imp.Import1C;
import org.adempiere.process.rpl.imp.ImporterFrom1C;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.compiere.Adempiere;
import org.compiere.acct.Doc;
import org.compiere.model.MAccount;
import org.compiere.model.MJournal;
import org.compiere.model.MJournalBatch;
import org.compiere.model.MJournalLine;
import org.compiere.model.X_A_Asset;
import org.compiere.model.X_A_Depreciation;
import org.compiere.model.X_A_Depreciation_Build;
import org.compiere.model.X_A_Depreciation_Convention;
import org.compiere.model.X_A_Depreciation_Exp;
import org.compiere.model.X_A_Depreciation_Method;
import org.compiere.model.X_A_Depreciation_Workfile;
import org.compiere.model.X_C_Period;
import org.compiere.model.X_GL_Category;
import org.compiere.model.X_GL_Journal;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogMgt;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 *	Post Depreciation
 *	
 *  @author Duman Znunissov
 *  @version $Id: DepreciationPost.java,v 1.0 $
 */

public class DepreciationPost extends SvrProcess
{
	/** Instance Logger 			*/
	private CLogger Log = CLogger.getCLogger(DepreciationPost.class);
	/** Parameters				*/
	private int p_AD_Org_ID = 0;
	private int	p_C_Period_ID = 0;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() != null) {
				if ("AD_Org_ID".equals(name))
					p_AD_Org_ID=Integer.valueOf(para[i].getParameter().toString());
				else if ("C_Period_ID".equals(name))
					p_C_Period_ID=Integer.valueOf(para[i].getParameter().toString());
				else
					log.info("prepare - Unknown Parameter: " + name);
			}
		}
	}	//	prepare

	
	/**
	 * 	Post Depreciation
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		String msg;
		log.info("doIt - Post Depreciation =" + p_C_Period_ID);

		int iAssetID = 0, iAssetCount=0, iAccumDepAcct=0, iDepAcct=0;
		int no=0;
		String clientCheck = " AND AD_Client_ID=" + getAD_Client_ID();
		StringBuffer sql =  new StringBuffer();
		Map<MultiKey, BigDecimal> DeprPost = new HashMap<MultiKey, BigDecimal>();

		sql.setLength(0);
		sql.append("SELECT A_Depreciation_Build_ID "
		   + " FROM A_Depreciation_Build as a, C_Period as c"  
		   + " WHERE a.C_Period_ID = c.C_Period_ID" 
		   + " AND StartDate > (SELECT EndDate FROM C_Period WHERE C_Period_ID = "
		   +  p_C_Period_ID+")");
		no = DB.getSQLValue(get_TrxName(), sql.toString());
		if (no>0) { msg="Next Period Exists: "+no;
			return msg; }
		sql.setLength(0);
		sql.append("SELECT A_Depreciation_Build_ID"
				+ " FROM A_Depreciation_Build "
				+ " WHERE AD_Org_ID =" +p_AD_Org_ID
				+ " AND C_Period_ID = "+p_C_Period_ID
	    	    + " AND isActive = 'Y'"
				+ clientCheck); 
		no = DB.getSQLValue(get_TrxName(), sql.toString());
		
		if (p_C_Period_ID == 0)
			throw new IllegalArgumentException("No Period");
        X_C_Period Period = new X_C_Period(getCtx(), p_C_Period_ID, null);

		sql.setLength(0);
		sql.append("SELECT A_Depreciation_Build_ID"
				+ " FROM A_Depreciation_Build "
				+ " WHERE AD_Org_ID =" +p_AD_Org_ID
				+ " AND C_Period_ID = "+p_C_Period_ID
	    	    + " AND isActive = 'Y'"
				+ clientCheck); 
		no = DB.getSQLValue(get_TrxName(), sql.toString());
        
		X_A_Depreciation_Build DepBuild = new X_A_Depreciation_Build (getCtx(), no, null);
		DepBuild.setAD_Org_ID(p_AD_Org_ID);
		DepBuild.setC_Period_ID(p_C_Period_ID);
		DepBuild.setPostingType(X_A_Depreciation_Build.POSTINGTYPE_Actual);
        DepBuild.save();

        sql.setLength(0);
        sql.append("SELECT A_Asset_ID, Name, AssetServiceDate "
    		+ " FROM A_Asset "
    		+ " WHERE AD_Org_ID =" + p_AD_Org_ID 
    		+ " AND AssetServiceDate < '" + Period.getEndDate()+"'"
			+ " AND A_Asset_CreateDate < '" + Period.getEndDate()+"'"
    		+ clientCheck
    	    + " AND isActive = 'Y'"
    	    //+ " AND A_Asset_ID = 1000138"
			+ " ORDER BY A_Asset_ID");
		PreparedStatement pstmt = null;
		pstmt = DB.prepareStatement (sql.toString(),null);
		ResultSet rs = null;
		try {				
		  rs = pstmt.executeQuery();
		  while (rs.next()){
			iAssetID = 0;  
			BigDecimal bAcquisValue = new BigDecimal(0.0);
			BigDecimal bAccumDep = new BigDecimal(0.0);
			BigDecimal bBookVal = new BigDecimal(0.0);
			BigDecimal bDepPerc = new BigDecimal(0.0);
			BigDecimal bMonthDep = new BigDecimal(0.0);
			//System.out.print(rs.getString("A_Asset_ID")+" ");
			iAssetCount++;
			iAssetID = rs.getInt("A_Asset_ID");

/* Finding Asset Accounts  ++++++++++++++++++++++++++++++++++++++++++++*/
	    	sql.setLength(0);
	        sql.append("SELECT A_Asset_Acct_ID, A_Accumdepreciation_Acct,"
	        	+ " A_Depreciation_Acct, A_Depreciation_Variable_Perc "
	    		+ " FROM A_Asset_Acct "
	    		+ " WHERE A_Asset_ID =" + iAssetID
	    	    + " AND isActive = 'Y'"
	    		+ clientCheck); 
			PreparedStatement pstmt2 = null;
			pstmt2 = DB.prepareStatement (sql.toString(),null);
			ResultSet rs2 = null;
			try {				
			  rs2 = pstmt2.executeQuery();
			  iAccumDepAcct=iDepAcct=0;
			  while (rs2.next()){
				  iAccumDepAcct = rs2.getInt("A_Accumdepreciation_Acct");
				  iDepAcct = rs2.getInt("A_Depreciation_Acct");
				  bDepPerc = rs2.getBigDecimal("A_Depreciation_Variable_Perc"); 
			  }
			  if (iDepAcct==0) System.out.println(iAssetID+" Depreciation Expense Account is empty!!!");
			  if (iAccumDepAcct==0) System.out.println(iAssetID+" Accumulated Depreciation Account is empty!!!");
			} catch (Exception e) {
			  System.out.println("A_Asset_Acct. Asset ID: "+iAssetID+" "+e);
			} finally {
				DB.close(rs2, pstmt2);
			    rs2 = null; pstmt2 = null;
			}
/*---------------------------------------  Finding Asset Accounts */
			
/*---------------------  Finding Asset Transactions ---------------------*/
			BigDecimal bAssetValueAmt = new BigDecimal(0.0);
			BigDecimal bAssetDepAmt = new BigDecimal(0.0);
			//Date tDateAcct;
	    	sql.setLength(0);
	        sql.append("SELECT AssetValueAmt, AssetDepAmt, DateAcct"
	    	      + " FROM A_Asset_Addition"  
	    	      + " WHERE A_Asset_ID = "+ iAssetID
	    	      + " AND DateAcct < '" + Period.getEndDate()+"'"
		    	  + " AND isActive = 'Y'"
	    	      + clientCheck); 
			pstmt2 = null;
			pstmt2 = DB.prepareStatement (sql.toString(),null);
			rs2 = null;
			try {				
			  rs2 = pstmt2.executeQuery();
			  while (rs2.next()){
				  bAssetValueAmt = rs2.getBigDecimal("AssetValueAmt");
				  bAssetDepAmt = rs2.getBigDecimal("AssetDepAmt");
				  //tDateAcct = rs2.getDate("DateAcct");
				  if (bAssetValueAmt!=null) bAcquisValue=bAcquisValue.add(bAssetValueAmt);
				  if (bAssetDepAmt!=null) bAccumDep=bAccumDep.add(bAssetDepAmt);
				  //System.out.print(tDateAcct+ " "+bAssetValueAmt+" "+bAssetDepAmt);
			  }
			  if ((bAssetValueAmt==null)&(bAssetDepAmt==null))
				  System.out.println(iAssetID+" Asset Value Amount and Depreciation is empty!!!");
			} catch (Exception e) {
			  System.out.println("A_Asset_Addition. Asset ID: "+iAssetID+" "+e);
			} finally {
				DB.close(rs2, pstmt2);
			    rs2 = null; pstmt2 = null;
			}
/*-------------------------------------  Finding Asset Transactions ------*/
			
/*---------------------  Finding Asset Depreciation ---------------------*/
			BigDecimal bExpense = new BigDecimal(0.0);
	    	sql.setLength(0);
	        sql.append("SELECT A_Entry_Type, DateAcct, Expense, IsDepreciated"
	        		+ ", PostingType, A_Period, A_Asset_ID"
	        		+ " FROM A_Depreciation_Exp"
	        		+ " WHERE A_Asset_ID = "+ iAssetID
	        		+ " AND A_Period <"+ p_C_Period_ID
	        		+ " AND A_Entry_Type = '"+ 
	        		  X_A_Depreciation_Exp.A_ENTRY_TYPE_Depreciation+"'"
	        		+ clientCheck); 
			pstmt2 = null;
			pstmt2 = DB.prepareStatement (sql.toString(),null);
			rs2 = null;
			try {				
			  rs2 = pstmt2.executeQuery();
			  //iAccumDepAcct=iDepAcct=0;
			  while (rs2.next()){
				  bExpense = rs2.getBigDecimal("Expense");
				  bAccumDep=bAccumDep.add(bExpense);
				  //System.out.print(" "+rs2.getBigDecimal("A_Period")+ " "+bExpense);
			  }
			} catch (Exception e) {
			  System.out.println("A_Depreciation_Exp. Asset ID: "+iAssetID+" "+e);
			} finally {
				DB.close(rs2, pstmt2);
			    rs2 = null; pstmt2 = null;
			}
/*--------------------------------  Finding Asset Depreciation ------------*/

/*----------------  Calculate Asset Depreciation --++++++++++++++++----------*/
			bBookVal=bAcquisValue.subtract(bAccumDep);
			if (bDepPerc.compareTo(BigDecimal.ZERO)>0)
               bMonthDep=bAcquisValue.multiply(bDepPerc).divide(new BigDecimal(1200),2, BigDecimal.ROUND_HALF_UP);
			if (bMonthDep.compareTo(bBookVal)>0) bMonthDep = bBookVal;
            if ( (iDepAcct>0) & (iAccumDepAcct>0) & (bMonthDep.compareTo(BigDecimal.ZERO)>0)) {
            	MultiKey DeprAccounts = new MultiKey(iDepAcct,iAccumDepAcct);
            	if (DeprPost.containsKey(DeprAccounts))
            		DeprPost.put(DeprAccounts, ((BigDecimal)DeprPost.get(DeprAccounts)).add(bMonthDep));	
            	else
            		DeprPost.put(DeprAccounts, bMonthDep);
            }
			//System.out.print(" "+bAcquisValue+ " "+bAccumDep+" "+bBookVal+" "+bDepPerc+" "+bMonthDep);
			/*------------------ Saving Month Deprecation ---------------*/
			if (bMonthDep.compareTo(BigDecimal.ZERO)>0) {
				sql.setLength(0);
				sql.append("SELECT A_Depreciation_Exp_ID"
						+ " FROM A_Depreciation_Exp "
						+ " WHERE AD_Org_ID =" +p_AD_Org_ID
						+ " AND A_Period = "+p_C_Period_ID
						+ " AND A_Asset_ID = "+iAssetID
		        		+ " AND A_Entry_Type = '"+ 
		        		  X_A_Depreciation_Exp.A_ENTRY_TYPE_Depreciation+"'"
						+ clientCheck); 
				no = DB.getSQLValue(get_TrxName(), sql.toString());
				if (no<0) no=0;
				X_A_Depreciation_Exp DepExp = new X_A_Depreciation_Exp (getCtx(), no, null);
				DepExp.setAD_Org_ID(p_AD_Org_ID);
				DepExp.setExpense(bMonthDep);
				DepExp.setA_Entry_Type(X_A_Depreciation_Exp.A_ENTRY_TYPE_Depreciation);
				DepExp.setA_Period(p_C_Period_ID);
				DepExp.setIsDepreciated(false);
				DepExp.setDescription("Амортизация за "+Period.getName());
				DepExp.setA_Asset_ID(iAssetID);
				DepExp.setDateAcct(Period.getEndDate());
				DepExp.setA_Account_Number(iDepAcct);
				DepExp.setA_AccumDepreciation_Acct(iAccumDepAcct);
				DepExp.setPostingType(X_A_Depreciation_Build.POSTINGTYPE_Actual);
				DepExp.save();
			}
			/*------------------ Saving Month Deprecation ---------------*/
/*--------------------------------  Calculate Asset Depreciation ------------*/
			//System.out.println("");
		  }
		} catch (Exception e) {
			  System.out.println("A_Depreciation_Exp. Asset ID:"+iAssetID+" "+e);
		} finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}

		/*------------------ Creating Journal Batch -----------------*/
		int iDocTypeID = 0, iCurrencyID = 0, iGLCatID = 0, iConvTypeID;
		// Finding Doc Type ID
		sql.setLength(0);
        sql.append("SELECT C_DocType_ID FROM C_DocType"
		+ " WHERE Name='GL Journal' AND DocBaseType='GLJ' AND isActive ='Y'"+clientCheck);
		iDocTypeID = DB.getSQLValue(get_TrxName(), sql.toString());
		if (iDocTypeID<0) return "Error DocTypeID";

		// Finding Currency ID
		sql.setLength(0);
        sql.append("SELECT C_Currency_ID FROM C_AcctSchema "
		+ " WHERE isActive='Y'"+clientCheck);
		iCurrencyID = DB.getSQLValue(get_TrxName(), sql.toString());
		if (iCurrencyID<0) return "Error CurrencyID";
        
		// Finding GL Category ID
		sql.setLength(0);
        sql.append("SELECT GL_Category_ID FROM GL_Category "
		+ " WHERE isActive='Y' AND CategoryType = '"+X_GL_Category.CATEGORYTYPE_Manual+"'"
        +" AND isDefault = 'Y'"+clientCheck);
		iGLCatID = DB.getSQLValue(get_TrxName(), sql.toString());
		if (iGLCatID<0) return "Error GL CategoryID";

		// Finding Conversion Type
		sql.setLength(0);
        sql.append("SELECT C_ConversionType_ID FROM C_ConversionType "
		+ " WHERE isActive='Y' AND isDefault = 'Y'");
		iConvTypeID = DB.getSQLValue(get_TrxName(), sql.toString());
		if (iConvTypeID<0) return "Error Conversion Type ID";
		
		String sDocNum = "DEP-"+Period.getC_Year().getFiscalYear()+"-"+Period.getPeriodNo();

		sql.setLength(0);
        sql.append("SELECT GL_JournalBatch_ID FROM GL_JournalBatch "
        					+ " WHERE DocumentNo = "+ "'"+sDocNum+"'");
		no = DB.getSQLValue(get_TrxName(), sql.toString());
		if (no<0) no =0;

		MJournalBatch batch = new MJournalBatch (getCtx(), no, null);
		batch.setAD_Org_ID(getAD_Client_ID());
		batch.setAD_Org_ID(p_AD_Org_ID);
		batch.setDocumentNo(sDocNum);
		batch.setDescription("Амортизация за "+Period.getName());
		batch.setC_DocType_ID(iDocTypeID);
		batch.setPostingType(X_A_Depreciation_Build.POSTINGTYPE_Actual);
		batch.setDateAcct(Period.getEndDate());
		batch.setDateDoc(Period.getEndDate());
		batch.setC_Currency_ID(iCurrencyID);
		batch.setGL_Category_ID(iGLCatID);
		if (!batch.save()) {
			Log.log(Level.SEVERE, "Batch Journal not saved - "+batch.getDocumentNo());
		} 
		/*------------------ Creating Journal Batch -----------------*/
		
		/* ----------------- Creating Journal ----------------------*/
		sDocNum="DEP-001";
		sql.setLength(0);
		sql.append("SELECT GL_Journal_ID FROM GL_Journal "
		  	      + " WHERE DocumentNo = "+"'"+sDocNum+"'"
		  	      + " AND GL_JournalBatch_ID = "+batch.get_ID());
		no = DB.getSQLValue(get_TrxName(), sql.toString());
		if (no<0) no = 0;
		MJournal journal = new MJournal (getCtx(), no, null);
		journal.setGL_JournalBatch_ID(batch.getGL_JournalBatch_ID());
		journal.setClientOrg(getAD_Client_ID(), p_AD_Org_ID);
		journal.setDescription (batch.getDescription());
		journal.setDocumentNo (sDocNum.toString());
		journal.setC_AcctSchema_ID (1000000);
		journal.setC_DocType_ID (batch.getC_DocType_ID());
		journal.setGL_Category_ID (batch.getGL_Category_ID());
		journal.setPostingType (batch.getPostingType());
		journal.setCurrency (batch.getC_Currency_ID(), iConvTypeID, new BigDecimal("1"));
		journal.setC_Period_ID(batch.getC_Period_ID());
		journal.setDateAcct(batch.getDateAcct());	
		journal.setDateDoc (batch.getDateDoc());
		if (!journal.save()) {
			Log.log(Level.SEVERE, "Journal not saved - "+batch.getDocumentNo()+ " " +journal.getDocumentNo());
		} 
		/* ----------------- Creating Journal ----------------------*/
				
		/*++++++++++++++++++++++ Creating Journal Lines ++++++++++++++++++++*/		
		int i=0;
		int iLine =0;
		Set set = DeprPost.keySet();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			MultiKey DeprAccounts = (MultiKey)itr.next();
			//	********************** Lines Debit *****************
			iLine = ++i*20-10;
			sql.setLength(0);
			sql.append("SELECT GL_JournalLine_ID FROM GL_JournalLine "
				+ " WHERE GL_Journal_ID = "+journal.get_ID()
			  	+ " AND line = "+ iLine);
			no = DB.getSQLValue(get_TrxName(), sql.toString());
			MJournalLine line;
			if (no>0) 
				line = new MJournalLine (getCtx(),no,null);
			else 
				line = new MJournalLine (journal);
			line.setDescription(journal.getDescription());
			line.setCurrency (journal.getC_Currency_ID(), journal.getC_ConversionType_ID(), 
					journal.getCurrencyRate());
			line.setC_ValidCombination_ID((Integer)DeprAccounts.getKey(0));
			line.setLine (iLine);
			line.setAmtSourceDr ((BigDecimal)DeprPost.get(DeprAccounts));
			line.setAmtSourceCr (new BigDecimal("0"));
			//line.setAmtAcct (bSum, bSum);	//	only if not 0
			line.setDateAcct (journal.getDateAcct());
			//
			//line.setC_UOM_ID(imp.getC_UOM_ID());
			line.setQty(new BigDecimal("0"));
			//
			if (!line.save()) 
				Log.log(Level.SEVERE, "Journal Line "+ sDocNum.toString() +", "
				      + iLine+ " not saved");
			//	********************** Lines Debit *****************
			
			//	********************** Lines Credit *****************
			iLine+=10;
			sql.setLength(0);
			sql.append("SELECT GL_JournalLine_ID FROM GL_JournalLine "
				+ " WHERE GL_Journal_ID = "+journal.get_ID()
			  	+ " AND line = "+ iLine);
			no = DB.getSQLValue(get_TrxName(), sql.toString());
			line = null;
			if (no>0) 
				line = new MJournalLine (getCtx(),no,null);
			else 
				line = new MJournalLine (journal);
			line.setDescription(journal.getDescription());
			line.setCurrency (journal.getC_Currency_ID(), journal.getC_ConversionType_ID(), 
					journal.getCurrencyRate());
			line.setC_ValidCombination_ID((Integer)DeprAccounts.getKey(1));
			line.setLine (iLine);
			line.setAmtSourceDr (new BigDecimal("0"));
			line.setAmtSourceCr ((BigDecimal)DeprPost.get(DeprAccounts));
			//line.setAmtAcct (bSum, bSum);	//	only if not 0
			line.setDateAcct (journal.getDateAcct());
			//
			//line.setC_UOM_ID(imp.getC_UOM_ID());
			line.setQty(new BigDecimal("0"));
			//
			if (!line.save()) 
				Log.log(Level.SEVERE, "Journal Line "+ sDocNum.toString() +", "
				      + iLine+ " not saved");
			//	********************** Lines Credit *****************
		}			
		/* ----------------- Creating Journal Lines ----------------------*/
		
		/*-----------Creating Journal ------------------------------*/
		
		msg = Msg.translate(getCtx(), "Asset Count") + " #" + iAssetCount;
		return msg;
	}	//	doIt

	
}
