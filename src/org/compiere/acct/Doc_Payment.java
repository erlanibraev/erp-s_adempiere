/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCharge;
import org.compiere.model.MPayment;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 * Post Invoice Documents.
 * 
 * <pre>
 *  Table:              C_Payment (335)
 *  Document Types      ARP, APP
 * </pre>
 * 
 * @author Jorg Janke
 * @version $Id: Doc_Payment.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_Payment extends Doc {
	/**
	 * Constructor
	 * 
	 * @param ass
	 *            accounting schemata
	 * @param rs
	 *            record
	 * @param trxName
	 *            trx
	 */
	public Doc_Payment(MAcctSchema[] ass, ResultSet rs, String trxName) {
		super(ass, MPayment.class, rs, null, trxName);
	} // Doc_Payment

	/** Tender Type */
	private String m_TenderType = null;
	/** Prepayment */
	private boolean m_Prepayment = false;
	/** Bank Account */
	private int m_C_BankAccount_ID = 0;

	/**
	 * Load Specific Document Details
	 * 
	 * @return error message or null
	 */
	protected String loadDocumentDetails() {
		MPayment pay = (MPayment) getPO();
		setDateDoc(pay.getDateTrx());
		m_TenderType = pay.getTenderType();
		m_Prepayment = pay.isPrepayment();
		m_C_BankAccount_ID = pay.getC_BankAccount_ID();
		// Amount
		setAmount(Doc.AMTTYPE_Gross, pay.getPayAmt());
		return null;
	} // loadDocumentDetails

	/**************************************************************************
	 * Get Source Currency Balance - always zero
	 * 
	 * @return Zero (always balanced)
	 */
	public BigDecimal getBalance() {
		BigDecimal retValue = Env.ZERO;
		// log.config( toString() + " Balance=" + retValue);
		return retValue;
	} // getBalance

	/**
	 * Create Facts (the accounting logic) for ARP, APP.
	 * 
	 * <pre>
	 *  ARP
	 *      BankInTransit   DR
	 *      UnallocatedCash         CR
	 *      or Charge/C_Prepayment
	 *  APP
	 *      PaymentSelect   DR
	 *      or Charge/V_Prepayment
	 *      BankInTransit           CR
	 *  CashBankTransfer
	 *      -
	 * </pre>
	 * 
	 * @param as
	 *            accounting schema
	 * @return Fact
	 */
	public ArrayList<Fact> createFacts(MAcctSchema as) {

		// V.Sokolov
		int m_GL_Category_ID = 0;
		String sql_ = "SELECT GL_Category_ID FROM C_DocType WHERE C_DocType_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rsDT = null;
		try {
			pstmt = DB.prepareStatement(sql_, null);
			pstmt.setInt(1, getC_DocType_ID());
			rsDT = pstmt.executeQuery();
			if (rsDT.next()) {
				m_GL_Category_ID = rsDT.getInt(1);
			}
		} catch (SQLException e) {
			log.log(Level.SEVERE, sql_, e);
		} finally {
			DB.close(rsDT, pstmt);
			rsDT = null;
			pstmt = null;
		}

		// not to create entries V.Sokolov
		ArrayList<Fact> facts_ = new ArrayList<Fact>();
		if (m_GL_Category_ID == 0 || m_GL_Category_ID == 1000001)
			return facts_;

		// create Fact Header
		Fact fact = new Fact(this, as, Fact.POST_Actual);
		MAccount acct = null;
		// Cash Transfer
		if ("X".equals(m_TenderType)) {
			ArrayList<Fact> facts = new ArrayList<Fact>();
			facts.add(fact);
			return facts;
		}

		// a.nurpiisov 16072012------------------{
		boolean fix = false;
		String doctype = null;
		String zsql = " SELECT d.docbasetype, p.c_order_id, p.c_invoice_id, b.isemployee "
				+ " FROM c_payment p "
				+ " inner join c_doctype d on d.c_doctype_id = p.c_doctype_id "
				+ " inner join c_bpartner b on b.c_bpartner_id = p.c_bpartner_id "
				+ " and p.c_payment_id = ? ";
		pstmt = null;
		rsDT = null;
		try {
			pstmt = DB.prepareStatement(zsql, null);
			pstmt.setInt(1, get_ID());
			rsDT = pstmt.executeQuery();
			if (rsDT.next()) {
				String isemployee = rsDT.getString(4);
				doctype = rsDT.getString(1);
				if (isemployee.equals("Y")) {
					acct = getAccount(Doc.ACCTTYPE_Employee, as);
				} else {
					String typedoc = rsDT.getString(1);
					int c_order_id = rsDT.getInt(2);
					int c_invoice_id = rsDT.getInt(3);

					if ((!typedoc.equals("") || typedoc != null)
							&& typedoc.equals("APP")) {

						if (c_order_id != 0)
							acct = getAccount(Doc.ACCTTYPE_PaymentSelect2, as);
						else
							acct = getAccount(Doc.ACCTTYPE_V_Liability, as);

					} else if ((!typedoc.equals("") || typedoc != null)
							&& typedoc.equals("ARR")) {

						if (c_order_id != 0)
							acct = getAccount(Doc.ACCTTYPE_CustomerContract, as);
						else
							acct = getAccount(Doc.ACCTTYPE_C_Receivable, as);
						// acct = getAccount(Doc.ACCTTYPE_CustomerInvoice, as);

					} else
						fix = true;
				}
			} else
				fix = true;

		} catch (SQLException e) {
			log.log(Level.SEVERE, zsql, e);
		} finally {
			DB.close(rsDT, pstmt);
			rsDT = null;
			pstmt = null;
		}

		if (!fix) {
			if (doctype.equals("APP")) {
				FactLine fl = fact.createLine(null, acct, getC_Currency_ID(),
						getAmount(), null);
				fl = fact.createLine(null,
						getAccount(Doc.ACCTTYPE_BankInTransit, as),
						getC_Currency_ID(), null, getAmount());
			} else {
				FactLine fl = fact.createLine(null,
						getAccount(Doc.ACCTTYPE_BankInTransit, as),
						getC_Currency_ID(), getAmount(), null);
				fl = fact.createLine(null, acct, getC_Currency_ID(), null,
						getAmount());

			}
		}
		// }--------------------------------------

		if (fix) {
			int AD_Org_ID = getBank_Org_ID(); // Bank Account Org
			if (getDocumentType().equals(DOCTYPE_ARReceipt)) {
				// Asset
				FactLine fl = fact.createLine(null,
						getAccount(Doc.ACCTTYPE_BankInTransit, as),
						getC_Currency_ID(), getAmount(), null);
				if (fl != null && AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
				//
				acct = null;
				if (getC_Charge_ID() != 0)
					acct = MCharge
							.getAccount(getC_Charge_ID(), as, getAmount());
				else if (m_Prepayment)
					acct = getAccount(Doc.ACCTTYPE_C_Prepayment, as);
				else {
					acct = getAccount(Doc.ACCTTYPE_UnallocatedCash, as);
					StringBuffer sql = new StringBuffer(
							"SELECT b.v_liability_acct FROM c_payment as a"
									+ " LEFT JOIN erps_stati_bdds as b ON"
									+ " (a.erps_stati_bdds_id = b.erps_stati_bdds_id)"
									+ " WHERE c_payment_id = " + get_ID());
					int no = DB.getSQLValue(getTrxName(), sql.toString());
					log.fine(sql + " " + no);
					System.out.println(sql + " " + no);
					if (no > 0)
						acct = MAccount.get(as.getCtx(), no);
				}
				fl = fact.createLine(null, acct, getC_Currency_ID(), null,
						getAmount());
				if (fl != null && AD_Org_ID != 0 && getC_Charge_ID() == 0) // don't
																			// overwrite
																			// charge
					fl.setAD_Org_ID(AD_Org_ID);
			}
			// APP
			else if (getDocumentType().equals(DOCTYPE_APPayment)) {
				acct = null;
				if (getC_Charge_ID() != 0)
					acct = MCharge
							.getAccount(getC_Charge_ID(), as, getAmount());
				else if (m_Prepayment)
					acct = getAccount(Doc.ACCTTYPE_V_Prepayment, as);
				else {
					acct = getAccount(Doc.ACCTTYPE_PaymentSelect, as);
					StringBuffer sql = new StringBuffer(
							"SELECT b.v_liability_acct FROM c_payment as a"
									+ " LEFT JOIN erps_stati_bdds as b ON"
									+ " (a.erps_stati_bdds_id = b.erps_stati_bdds_id)"
									+ " WHERE c_payment_id = " + get_ID());
					int no = DB.getSQLValue(getTrxName(), sql.toString());
					log.fine(sql + " " + no);
					System.out.println(sql + " " + no);
					if (no > 0)
						acct = MAccount.get(as.getCtx(), no);
				}
				FactLine fl = fact.createLine(null, acct, getC_Currency_ID(),
						getAmount(), null);
				if (fl != null && AD_Org_ID != 0 && getC_Charge_ID() == 0) // don't
																			// overwrite
																			// charge
					fl.setAD_Org_ID(AD_Org_ID);

				// Asset
				fl = fact.createLine(null,
						getAccount(Doc.ACCTTYPE_BankInTransit, as),
						getC_Currency_ID(), null, getAmount());
				if (fl != null && AD_Org_ID != 0)
					fl.setAD_Org_ID(AD_Org_ID);
			} else {
				p_Error = "DocumentType unknown: " + getDocumentType();
				log.log(Level.SEVERE, p_Error);
				fact = null;
			}
		}
		//
		facts_.add(fact);
		return facts_;
	} // createFact

	/**
	 * Get AD_Org_ID from Bank Account
	 * 
	 * @return AD_Org_ID or 0
	 */
	private int getBank_Org_ID() {
		if (m_C_BankAccount_ID == 0)
			return 0;
		//
		MBankAccount ba = MBankAccount.get(getCtx(), m_C_BankAccount_ID);
		return ba.getAD_Org_ID();
	} // getBank_Org_ID

} // Doc_Payment
