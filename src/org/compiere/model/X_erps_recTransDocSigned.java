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
import java.util.Properties;

/** Generated Model for erps_recTransDocSigned
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_recTransDocSigned extends PO implements I_erps_recTransDocSigned, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120309L;

    /** Standard Constructor */
    public X_erps_recTransDocSigned (Properties ctx, int erps_recTransDocSigned_ID, String trxName)
    {
      super (ctx, erps_recTransDocSigned_ID, trxName);
      /** if (erps_recTransDocSigned_ID == 0)
        {
			setC_BPartner_ID (0);
			seterps_receptTransmissionDoc_ID (0);
			seterps_recTransDocSigned_ID (0);
        } */
    }

    /** Load Constructor */
    public X_erps_recTransDocSigned (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_recTransDocSigned[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	public I_erps_receptTransmissionDoc geterps_receptTransmissionDoc() throws RuntimeException
    {
		return (I_erps_receptTransmissionDoc)MTable.get(getCtx(), I_erps_receptTransmissionDoc.Table_Name)
			.getPO(geterps_receptTransmissionDoc_ID(), get_TrxName());	}

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

	/** Set erps_recTransDocSigned.
		@param erps_recTransDocSigned_ID erps_recTransDocSigned	  */
	public void seterps_recTransDocSigned_ID (int erps_recTransDocSigned_ID)
	{
		if (erps_recTransDocSigned_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_recTransDocSigned_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_recTransDocSigned_ID, Integer.valueOf(erps_recTransDocSigned_ID));
	}

	/** Get erps_recTransDocSigned.
		@return erps_recTransDocSigned	  */
	public int geterps_recTransDocSigned_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_recTransDocSigned_ID);
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
}