package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.DB;
import org.compiere.util.Env;

public class Merpscontractline extends X_erps_contractline {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 959981802963776832L;

	public Merpscontractline(Properties ctx, int erps_contractline_ID,
			String trxName) {
		super(ctx, erps_contractline_ID, trxName);
	}

	public Merpscontractline(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/** Tax							*/
	private MTax 		m_tax = null;
	
	@Override
	protected boolean afterSave(boolean newRecord, boolean success) {
	
		String sql = "UPDATE erps_contract p SET (PlannedAmt,CommittedAmt) = " +
				"(SELECT COALESCE(SUM(pl.PlannedAmt),0)," +
				" COALESCE(SUM(pl.CommittedAmt),0)" +
				" FROM erps_contractLine pl" +
				" WHERE pl.erps_contract_id=p.erps_contract_id AND pl.IsActive='Y')" +
				" WHERE erps_contract_ID=" + geterps_contract_ID();
			int no = DB.executeUpdate(sql, get_TrxName());
			if (no != 1)
				log.log(Level.SEVERE, "updateHeader contract - #" + no);
			
			Merpscontract ec = new Merpscontract(getCtx(), geterps_contract_ID(), get_TrxName());
			BigDecimal taxBaseAmt = Env.ZERO;
			BigDecimal taxAmt = Env.ZERO;

			//
			if (m_tax == null)
				m_tax = MTax.get(getCtx(), (Integer) ec.getC_Tax_ID());
			
			boolean documentLevel = m_tax.isDocumentLevel();
			BigDecimal baseAmt = (BigDecimal) ec.getPlannedAmt();
			taxBaseAmt = taxBaseAmt.add(baseAmt);
			
			//
			if (!documentLevel)		// calculate line tax
				taxAmt = taxAmt.add(m_tax.calculateTax(baseAmt, true, 2));
			
			//	Calculate Tax
			if (documentLevel)		//	document level
				taxAmt =m_tax.calculateTax(taxBaseAmt, true, 2);
			
			ec.setTaxAmt(taxAmt);
			//	Set Base
			ec.setTaxBaseAmt(taxBaseAmt.subtract(taxAmt));
			ec.saveEx();
		
		return true;
	}

}
