package org.compiere.acct;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAcctSchema;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class DocMy extends Doc {
	
	/** Account Type - Cash     - Asset */
	public static final int     ACCTTYPE_CashAssetAdvance = 111;
	
	/** Account Type - Cash     - Asset */
	public static final int     ACCTTYPE_CashTransferAdvance = 222;
	

	DocMy(MAcctSchema[] ass, Class<?> clazz, ResultSet rs,
			String defaultDocumentType, String trxName) {
		super(ass, clazz, rs, defaultDocumentType, trxName);
	}

	@Override
	protected String loadDocumentDetails() {
		return null;
	}

	@Override
	public BigDecimal getBalance() {
		return null;
	}

	@Override
	public ArrayList<Fact> createFacts(MAcctSchema as) {
		return null;
	}

	/**
	 * Get the Valid Combination id for Accounting Schema Advance
	 * @param AcctType
	 * @param as
	 * @return
	 */
	protected int getValidCombination_ID_Advance(int AcctType, MAcctSchema as) {

		int para_1 = 0; // first parameter (second is always AcctSchema)
		String sql = null;

		/** Account Type - Invoice */
		if (AcctType == ACCTTYPE_Charge) // see getChargeAccount in DocLine
		{
			int cmp = getAmount(AMTTYPE_Charge).compareTo(Env.ZERO);
			if (cmp == 0)
				return 0;
			else if (cmp < 0)
				sql = "SELECT CH_Expense_Acct FROM C_Charge_Acct WHERE C_Charge_ID=? AND C_AcctSchema_ID=?";
			else
				sql = "SELECT CH_Revenue_Acct FROM C_Charge_Acct WHERE C_Charge_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_Charge_ID();
		} else if (AcctType == ACCTTYPE_V_Liability) {
			sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_V_Liability_Services) {
			sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_V_Prepayment) {
			sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else {
			log.severe("Not found AcctType=" + AcctType);
			return 0;
		}
		// Do we have sql & Parameter
		if (sql == null || para_1 == 0) {
			log.severe("No Parameter for AcctType=" + AcctType + " - SQL="
					+ sql);
			return 0;
		}

		// Get Acct
		int Account_ID = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = DB.prepareStatement(sql, null);
			if (para_1 == -1) // GL Accounts
				pstmt.setInt(1, as.getC_AcctSchema_ID());
			else {
				pstmt.setInt(1, para_1);
				pstmt.setInt(2, as.getC_AcctSchema_ID());
			}
			rs = pstmt.executeQuery();
			if (rs.next())
				Account_ID = rs.getInt(1);
		} catch (SQLException e) {
			log.log(Level.SEVERE, "AcctType=" + AcctType + " - SQL=" + sql, e);
			return 0;
		} finally {
			DB.close(rs, pstmt);
			rs = null;
			pstmt = null;
		}
		// No account
		if (Account_ID == 0) {
			log.severe("NO account Type=" + AcctType + ", Record="
					+ p_po.get_ID());
			return 0;
		}
		return Account_ID;
	}

}
