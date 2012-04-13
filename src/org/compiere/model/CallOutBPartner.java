package org.compiere.model;

import java.util.Properties; 

import javax.swing.JOptionPane;

import org.compiere.model.CalloutEngine; 
import org.compiere.model.GridField; 
import org.compiere.model.GridTab; 
import org.compiere.util.AdempiereSystemError; 
import org.compiere.util.Env; 

public class CallOutBPartner extends CalloutEngine{
	public String isWho(Properties ctx, int windowNo, GridTab mTab, GridField mField, 
			 Object value) throws AdempiereSystemError { 
		
		String who = mField.getColumnName();
		boolean value2  = (Boolean) mField.getValue();
		if(who.equals("IsEmployee")){
			if(value2) 
				mTab.setValue("IsEmployee", true);
			else
				mTab.setValue("IsEmployee", false);
			
			mTab.setValue("IsCustomer", false);
			mTab.setValue("IsVendor", false);
			  
		} else if(who.equals("IsCustomer")){
			if(value2) 
				mTab.setValue("IsCustomer", true);
			else
				mTab.setValue("IsCustomer", false);
			
		  mTab.setValue("IsVendor", false);
		  mTab.setValue("IsEmployee", false);
		  
		} else if(who.equals("IsVendor")){
			if(value2)
				mTab.setValue("IsVendor", true);
			else
				mTab.setValue("IsVendor", false);
			
		  mTab.setValue("IsVendor", true);
		  mTab.setValue("IsEmployee", false);			
		} else;
		
		return null;
	}
}
