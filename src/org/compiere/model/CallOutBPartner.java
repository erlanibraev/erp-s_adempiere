package org.compiere.model;

import java.util.Properties; 

import javax.swing.JOptionPane;

import org.compiere.model.CalloutEngine; 
import org.compiere.model.GridField; 
import org.compiere.model.GridTab; 
import org.compiere.util.AdempiereSystemError; 
import org.compiere.util.Env; 

public class CallOutBPartner extends CalloutEngine{
	public String isEmployee(Properties ctx, int windowNo, GridTab mTab, GridField mField, 
			 Object value) throws AdempiereSystemError { 

		Boolean isEmployee = (Boolean) mTab.getValue("IsEmployee");
		if(isEmployee.equals(true)){
			int result = JOptionPane.showConfirmDialog(null, "При отметке Бизнес-партнера как служащего, " +
					"то буду сняты отметки 'Поставщик' и 'Покупатель', поставить отметку?", 
					"Предупреждение", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION){
				  mTab.setValue("IsCustomer", false);
				  mTab.setValue("IsVendor", false);
				  mTab.setValue("IsEmployee", true);
			} else mTab.setValue("IsEmployee", false);	  
		}
		return null;
	}
}
