package org.compiere.process;

import java.util.logging.Level;

import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProject;
import org.compiere.model.MProjectPhase;
import org.compiere.model.Merpscontract;
import org.compiere.model.Merpscontractline;

public class ContractLineGenOrder extends SvrProcess {

	private int		m_ContractLine_ID = 0;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else
				log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
		}
	}

	@Override
	protected String doIt() throws Exception {
		
		m_ContractLine_ID = getRecord_ID();
		log.info("doIt - ContractLine_ID=" + m_ContractLine_ID);
		if (m_ContractLine_ID == 0)
			throw new IllegalArgumentException("erps_contractLine_ID == 0");
		Merpscontractline fromLine = new Merpscontractline (getCtx(), m_ContractLine_ID, get_TrxName());
		Merpscontract fromContract = ContractGenOrder.getContract(getCtx(), fromLine.geterps_contract_ID(), get_TrxName());
		MOrder order = new MOrder (fromContract, true, MOrder.DocSubTypeSO_OnCredit);
		order.setDateOrdered(fromLine.getStartDate());
		order.setDatePromised(fromLine.getEndDate());
		order.setDescription(order.getDescription() + " - " + fromLine.getName());
		if (!order.save())
			throw new Exception("Could not create Order");
		
		MOrderLine ol1 = new MOrderLine(order);
		ol1.setLine(fromLine.getSeqNo());
		StringBuffer sb1 = new StringBuffer (fromLine.getName());
		if (fromLine.getDescription() != null && fromLine.getDescription().length() > 0)
			sb1.append(" - ").append(fromLine.getDescription());
		ol1.setDescription(sb1.toString());
		//
		ol1.setQty(fromLine.getQty());
		ol1.setPrice(fromLine.getPlannedAmt());
		ol1.setC_Tax_ID(fromContract.getC_Tax_ID());
		if (!ol1.save())
			log.log(Level.SEVERE, "doIt - Lines not generated");
		
		return null;
	}

}
