package org.compiere.process;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.Merpscontract;
import org.compiere.model.Merpscontractline;
import org.compiere.util.Env;

public class ContractGenOrder extends SvrProcess {
	
	/**	Contract ID from contract directly		*/
	private int		Contract_ID = 0;

	@Override
	protected void prepare() {
		
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		//
		Contract_ID = getRecord_ID();
	}

	@Override
	protected String doIt() throws Exception {
		
		log.info("Contract_ID=" + Contract_ID);
		if (Contract_ID == 0)
			throw new IllegalArgumentException("Contract_ID == 0");
		Merpscontract fromConract = getContract (getCtx(), Contract_ID, get_TrxName());
		Env.setSOTrx(getCtx(), true);	//	Set SO context

		/** @todo duplicate invoice prevention */
		
		MOrder order = new MOrder (fromConract, true, MOrder.DocSubTypeSO_OnCredit);
	 	if (!order.save())
	 	throw new Exception("Could not create Order");
	 	//
	 	MOrderLine ol1 = new MOrderLine(order);
	 	StringBuffer sb1 = new StringBuffer (fromConract.getName());
		if (fromConract.getDescription() != null && fromConract.getDescription().length() > 0)
			sb1.append(" - ").append(fromConract.getDescription());
		ol1.setDescription(sb1.toString());
		ol1.setPrice(fromConract.getPlannedAmt());
		ol1.setQty(new BigDecimal("1"));
		ol1.setC_Tax_ID(fromConract.getC_Tax_ID());
		if (!ol1.save())
			log.log(Level.SEVERE, "doIt - Lines not generated");
		
		return "generation of the act is completed -> " + "@C_Order_ID@ " + order.getDocumentNo();
	}
	

	/**
	 * 	Get and validate Contract
	 * 	@param ctx context
	 * 	@param Contract_ID id
	 * 	@return valid contract
	 * 	@param trxName transaction
	 */
	static protected Merpscontract getContract (Properties ctx, int Contract_ID, String trxName)
	{
		Merpscontract fromConract = new Merpscontract (ctx, Contract_ID, trxName);
		if (fromConract.geterps_contract_ID() == 0)
			throw new IllegalArgumentException("Conract not found Contract_ID=" + Contract_ID);
		if (fromConract.getM_PriceList_Version_ID() == 0)
			throw new IllegalArgumentException("Conract has no Price List");
		if (fromConract.getM_Warehouse_ID() == 0)
			throw new IllegalArgumentException("Conract has no Warehouse");
		if (fromConract.getC_BPartner_ID() == 0 || fromConract.getC_BPartner_Location_ID() == 0)
			throw new IllegalArgumentException("Conract has no Business Partner/Location");
		return fromConract;
	}	//	getContract

}
