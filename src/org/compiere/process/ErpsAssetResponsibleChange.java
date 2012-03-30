package org.compiere.process;

import java.util.logging.Level;
import org.compiere.model.MAsset;
import org.compiere.model.MerpsrecTransDocLine;
import org.compiere.model.MerpsrecTransDocSigned;
import org.compiere.model.MerpsreceptTransmissionDoc;
import org.compiere.util.CLogger;
import org.erps.ErpsEncoder;

public class ErpsAssetResponsibleChange extends SvrProcess {

	private String finalMsg = "OK";
	private MerpsrecTransDocLine[] lines;
	private MerpsreceptTransmissionDoc TransmissionDoc = null;
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (ErpsAssetResponsibleChange.class);
	
	private ErpsEncoder encoder = new ErpsEncoder();	
	@Override
	protected void prepare() {
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)	{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null); 
			else 
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		
		//
		TransmissionDoc = new MerpsreceptTransmissionDoc(getCtx(), getRecord_ID(), get_TrxName());
		lines = TransmissionDoc.getLines();

	}

	@Override
	protected String doIt() throws Exception {
		
		if(TransmissionDoc == null){
			s_log.log(Level.SEVERE, "TransmissionDoc is null ");
			return encoder.encodeUTF8("Error. TransmissionDoc is null");
		}
		
		if(lines.length == 0){
			s_log.log(Level.SEVERE, "TransmissionDocLine or TransmissionDocSigned is null");
			return encoder.encodeUTF8("TransmissionDocLine or TransmissionDocSigned is null");
		}
		
		//  Lines
		for(MerpsrecTransDocLine dl: lines){
			MAsset as = new MAsset(getCtx(), dl.getA_Asset_ID());
			as.setC_BPartnerSR_ID(TransmissionDoc.geterps_toResponsible());
			as.saveEx();
			dl.setProcessed(true);
			dl.saveUpdate();
		}
		
		TransmissionDoc.setProcessed(true);
		TransmissionDoc.saveUpdate();
		
		return finalMsg;
	}

}
