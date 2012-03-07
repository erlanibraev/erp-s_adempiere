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

/** Generated Model for erps_recTransDocLine
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_recTransDocLine extends PO implements I_erps_recTransDocLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120307L;

    /** Standard Constructor */
    public X_erps_recTransDocLine (Properties ctx, int erps_recTransDocLine_ID, String trxName)
    {
      super (ctx, erps_recTransDocLine_ID, trxName);
      /** if (erps_recTransDocLine_ID == 0)
        {
			seterps_recTransDocLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_erps_recTransDocLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_recTransDocLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_A_Asset getA_Asset() throws RuntimeException
    {
		return (org.compiere.model.I_A_Asset)MTable.get(getCtx(), org.compiere.model.I_A_Asset.Table_Name)
			.getPO(getA_Asset_ID(), get_TrxName());	}

	/** Set Asset.
		@param A_Asset_ID 
		Asset used internally or by customers
	  */
	public void setA_Asset_ID (int A_Asset_ID)
	{
		if (A_Asset_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_ID, Integer.valueOf(A_Asset_ID));
	}

	/** Get Asset.
		@return Asset used internally or by customers
	  */
	public int getA_Asset_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_ID);
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

	/** Set erps_recTransDocLine.
		@param erps_recTransDocLine_ID erps_recTransDocLine	  */
	public void seterps_recTransDocLine_ID (int erps_recTransDocLine_ID)
	{
		if (erps_recTransDocLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_recTransDocLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_recTransDocLine_ID, Integer.valueOf(erps_recTransDocLine_ID));
	}

	/** Get erps_recTransDocLine.
		@return erps_recTransDocLine	  */
	public int geterps_recTransDocLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_recTransDocLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}