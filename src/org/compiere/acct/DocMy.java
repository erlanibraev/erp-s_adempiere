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

	public int getValidCombination_ID(int AcctType, MAcctSchema as) {
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
			// sql =
			// "SELECT V_Liability_Acct FROM C_BP_Vendor_Acct WHERE C_BPartner_ID=? AND C_AcctSchema_ID=?";
			// para_1 = getC_BPartner_ID();
			sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_V_Liability_Services) {
			// sql =
			// "SELECT V_Liability_Services_Acct FROM C_BP_Vendor_Acct WHERE C_BPartner_ID=? AND C_AcctSchema_ID=?";
			// para_1 = getC_BPartner_ID();
			sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_C_Receivable) {
			sql = "SELECT C_Receivable_Acct FROM C_BP_Customer_Acct WHERE C_BPartner_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_C_Receivable_Services) {
			sql = "SELECT C_Receivable_Services_Acct FROM C_BP_Customer_Acct WHERE C_BPartner_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_V_Prepayment) {
			// sql =
			// "SELECT V_Prepayment_Acct FROM C_BP_Vendor_Acct WHERE C_BPartner_ID=? AND C_AcctSchema_ID=?";
			// para_1 = getC_BPartner_ID();
			sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_C_Prepayment) {
			sql = "SELECT C_Prepayment_Acct FROM C_BP_Customer_Acct WHERE C_BPartner_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		}

		/** Account Type - Payment */
		else if (AcctType == ACCTTYPE_UnallocatedCash) {
			sql = "SELECT B_UnallocatedCash_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		} else if (AcctType == ACCTTYPE_BankInTransit) {
			sql = "SELECT B_InTransit_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		} else if (AcctType == ACCTTYPE_PaymentSelect) {
			sql = "SELECT B_PaymentSelect_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		}

		/** Account Type - Allocation */
		else if (AcctType == ACCTTYPE_DiscountExp) {
			sql = "SELECT a.PayDiscount_Exp_Acct FROM C_BP_Group_Acct a, C_BPartner bp "
					+ "WHERE a.C_BP_Group_ID=bp.C_BP_Group_ID AND bp.C_BPartner_ID=? AND a.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_DiscountRev) {
			sql = "SELECT PayDiscount_Rev_Acct FROM C_BP_Group_Acct a, C_BPartner bp "
					+ "WHERE a.C_BP_Group_ID=bp.C_BP_Group_ID AND bp.C_BPartner_ID=? AND a.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		} else if (AcctType == ACCTTYPE_WriteOff) {
			sql = "SELECT WriteOff_Acct FROM C_BP_Group_Acct a, C_BPartner bp "
					+ "WHERE a.C_BP_Group_ID=bp.C_BP_Group_ID AND bp.C_BPartner_ID=? AND a.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		}

		/** Account Type - Bank Statement */
		else if (AcctType == ACCTTYPE_BankAsset) {
			sql = "SELECT B_Asset_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		} else if (AcctType == ACCTTYPE_InterestRev) {
			sql = "SELECT B_InterestRev_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		} else if (AcctType == ACCTTYPE_InterestExp) {
			sql = "SELECT B_InterestExp_Acct FROM C_BankAccount_Acct WHERE C_BankAccount_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_BankAccount_ID();
		}

		/** Account Type - Cash */
		else if (AcctType == ACCTTYPE_CashAsset) {
			sql = "SELECT CB_Asset_Acct FROM C_CashBook_Acct WHERE C_CashBook_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_CashBook_ID();
		} else if (AcctType == ACCTTYPE_CashTransfer) {
			sql = "SELECT CB_CashTransfer_Acct FROM C_CashBook_Acct WHERE C_CashBook_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_CashBook_ID();
		} else if (AcctType == ACCTTYPE_CashExpense) {
			// sql =
			// "SELECT CB_Expense_Acct FROM C_CashBook_Acct WHERE C_CashBook_ID=? AND C_AcctSchema_ID=?";
			// para_1 = getC_CashBook_ID();
			if (getC_BPartner_ID() == 0) {
				log.log(Level.SEVERE, "C_BPartner_ID is null");
				throw new AdempiereException("Не выбран Бизнесс-партнер");
			} else {
				sql = "select e_expense_acct from adempiere.c_bp_employee_acct t where t.c_bpartner_id=? and t.C_AcctSchema_ID=?";
				para_1 = getC_BPartner_ID();
			}
		} else if (AcctType == ACCTTYPE_CashReceipt) {
			sql = "SELECT CB_Receipt_Acct FROM C_CashBook_Acct WHERE C_CashBook_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_CashBook_ID();
		} else if (AcctType == ACCTTYPE_CashDifference) {
			sql = "SELECT CB_Differences_Acct FROM C_CashBook_Acct WHERE C_CashBook_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_CashBook_ID();
		}

		/** Inventory Accounts */
		else if (AcctType == ACCTTYPE_InvDifferences) {
			sql = "SELECT W_Differences_Acct FROM M_Warehouse_Acct WHERE M_Warehouse_ID=? AND C_AcctSchema_ID=?";
			// "SELECT W_Inventory_Acct, W_Revaluation_Acct, W_InvActualAdjust_Acct FROM M_Warehouse_Acct WHERE M_Warehouse_ID=? AND C_AcctSchema_ID=?";
			para_1 = getM_Warehouse_ID();
		} else if (AcctType == ACCTTYPE_NotInvoicedReceipts) {
			sql = "SELECT NotInvoicedReceipts_Acct FROM C_BP_Group_Acct a, C_BPartner bp "
					+ "WHERE a.C_BP_Group_ID=bp.C_BP_Group_ID AND bp.C_BPartner_ID=? AND a.C_AcctSchema_ID=?";
			para_1 = getC_BPartner_ID();
		}

		/** Project Accounts */
		else if (AcctType == ACCTTYPE_ProjectAsset) {
			sql = "SELECT PJ_Asset_Acct FROM C_Project_Acct WHERE C_Project_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_Project_ID();
		} else if (AcctType == ACCTTYPE_ProjectWIP) {
			sql = "SELECT PJ_WIP_Acct FROM C_Project_Acct WHERE C_Project_ID=? AND C_AcctSchema_ID=?";
			para_1 = getC_Project_ID();
		}

		/** GL Accounts */
		else if (AcctType == ACCTTYPE_PPVOffset) {
			sql = "SELECT PPVOffset_Acct FROM C_AcctSchema_GL WHERE C_AcctSchema_ID=?";
			para_1 = -1;
		} else if (AcctType == ACCTTYPE_CommitmentOffset) {
			sql = "SELECT CommitmentOffset_Acct FROM C_AcctSchema_GL WHERE C_AcctSchema_ID=?";
			para_1 = -1;
		} else if (AcctType == ACCTTYPE_CommitmentOffsetSales) {
			sql = "SELECT CommitmentOffsetSales_Acct FROM C_AcctSchema_GL WHERE C_AcctSchema_ID=?";
			para_1 = -1;
		}

		else {
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
