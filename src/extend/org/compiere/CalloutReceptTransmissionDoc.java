package extend.org.compiere;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MAsset;

/**
 * Callout to work with the module AA, table erps_receptTransmissionDoc ...
 * @author V.Sokolov
 * @version $Id: CalloutReceptTransmissionDoc.java, v 1.0 2012/03/07
 */
public class CalloutReceptTransmissionDoc extends CalloutEngine {
	
	private String msg = "";
	
	/*
	 * default Date (Sysdate)
	 */
	public void defaultValue(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		Date d = new Date();
		mTab.setValue("DateDoc", new Timestamp(d.getTime()));
	}
	
	/*
	 * 
	 */
	public String setDescription(Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value){
		
		Integer assetId = (Integer) value;
		if(assetId == null)
			return msg = "Not found the main means of";
		
		MAsset as = new MAsset(ctx, assetId, null);
		mTab.setValue("Description", as.getValue()+ ",   "+ as.getName());
		
		return msg;
	}

}
