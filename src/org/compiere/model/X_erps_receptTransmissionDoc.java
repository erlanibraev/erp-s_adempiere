/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for erps_receptTransmissionDoc
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_receptTransmissionDoc extends PO implements I_erps_receptTransmissionDoc, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120307L;

    /** Standard Constructor */
    public X_erps_receptTransmissionDoc (Properties ctx, int erps_receptTransmissionDoc_ID, String trxName)
    {
      super (ctx, erps_receptTransmissionDoc_ID, trxName);
      /** if (erps_receptTransmissionDoc_ID == 0)
        {
			seterps_receptTransmissionDoc_ID (0);
        } */
    }

    /** Load Constructor */
    public X_erps_receptTransmissionDoc (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_erps_receptTransmissionDoc[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Date From.
		@param DateFrom 
		Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom)
	{
		set_Value (COLUMNNAME_DateFrom, DateFrom);
	}

	/** Get Date From.
		@return Starting date for a range
	  */
	public Timestamp getDateFrom () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateFrom);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	public org.compiere.model.I_C_BPartner geterps_Account() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(geterps_Accountant(), get_TrxName());	}

	/** Set erps_Accountant.
		@param erps_Accountant erps_Accountant	  */
	public void seterps_Accountant (int erps_Accountant)
	{
		set_Value (COLUMNNAME_erps_Accountant, Integer.valueOf(erps_Accountant));
	}

	/** Get erps_Accountant.
		@return erps_Accountant	  */
	public int geterps_Accountant () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_Accountant);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner geterps_fromResponsi() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(geterps_fromResponsible(), get_TrxName());	}

	/** Set erps_fromResponsible.
		@param erps_fromResponsible erps_fromResponsible	  */
	public void seterps_fromResponsible (int erps_fromResponsible)
	{
		set_Value (COLUMNNAME_erps_fromResponsible, Integer.valueOf(erps_fromResponsible));
	}

	/** Get erps_fromResponsible.
		@return erps_fromResponsible	  */
	public int geterps_fromResponsible () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_fromResponsible);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set erps_receptTransmissionDoc_ID.
		@param erps_receptTransmissionDoc_ID erps_receptTransmissionDoc_ID	  */
	public void seterps_receptTransmissionDoc_ID (int erps_receptTransmissionDoc_ID)
	{
		if (erps_receptTransmissionDoc_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_receptTransmissionDoc_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_receptTransmissionDoc_ID, Integer.valueOf(erps_receptTransmissionDoc_ID));
	}

	/** Get erps_receptTransmissionDoc_ID.
		@return erps_receptTransmissionDoc_ID	  */
	public int geterps_receptTransmissionDoc_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_receptTransmissionDoc_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getErps_Special() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getErps_Specialist(), get_TrxName());	}

	/** Set Erps_Specialist.
		@param Erps_Specialist Erps_Specialist	  */
	public void setErps_Specialist (int Erps_Specialist)
	{
		set_Value (COLUMNNAME_Erps_Specialist, Integer.valueOf(Erps_Specialist));
	}

	/** Get Erps_Specialist.
		@return Erps_Specialist	  */
	public int getErps_Specialist () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Erps_Specialist);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner geterps_toResponsi() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(geterps_toResponsible(), get_TrxName());	}

	/** Set erps_toResponsible.
		@param erps_toResponsible erps_toResponsible	  */
	public void seterps_toResponsible (int erps_toResponsible)
	{
		set_Value (COLUMNNAME_erps_toResponsible, Integer.valueOf(erps_toResponsible));
	}

	/** Get erps_toResponsible.
		@return erps_toResponsible	  */
	public int geterps_toResponsible () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_toResponsible);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}