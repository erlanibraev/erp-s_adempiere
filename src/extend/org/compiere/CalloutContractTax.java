package extend.org.compiere;

import java.math.BigDecimal;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MTax;
import org.compiere.util.Env;

public class CalloutContractTax extends CalloutEngine {
	
	/** Tax							*/
	private MTax 		m_tax = null;
	
	public String calculateTax (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		if (isCalloutActive() || value == null)
			return "";
		
		BigDecimal taxBaseAmt = Env.ZERO;
		BigDecimal taxAmt = Env.ZERO;

		//
		if (m_tax == null)
			m_tax = MTax.get(ctx, (Integer) mTab.getValue("C_Tax_ID"));
		
		boolean documentLevel = m_tax.isDocumentLevel();
		BigDecimal baseAmt = (BigDecimal) mTab.getValue("PlannedAmt");
		taxBaseAmt = taxBaseAmt.add(baseAmt);
		
		//
		if (!documentLevel)		// calculate line tax
			taxAmt = taxAmt.add(m_tax.calculateTax(baseAmt, true, 2));
		
		if (taxBaseAmt == null)
			return "Warning: amount of the tax base is null";
		
		//	Calculate Tax
		if (documentLevel)		//	document level
			taxAmt =m_tax.calculateTax(taxBaseAmt, true, 2);
		
		mTab.setValue("TaxAmt",taxAmt);
		//	Set Base
		mTab.setValue("TaxBaseAmt", taxBaseAmt.subtract(taxAmt));
		
		return "";
		
	}

}
