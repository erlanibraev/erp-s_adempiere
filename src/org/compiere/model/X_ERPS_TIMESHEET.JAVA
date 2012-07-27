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

/** Generated Model for erps_timesheet
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_timesheet extends PO implements I_erps_timesheet, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120727L;

    /** Standard Constructor */
    public X_erps_timesheet (Properties ctx, int erps_timesheet_ID, String trxName)
    {
      super (ctx, erps_timesheet_ID, trxName);
      /** if (erps_timesheet_ID == 0)
        {
			setC_BPartner_ID (0);
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
			seterps_hourQuota (0);
			seterps_scheduleRule_ID (0);
			seterps_timesheet_ID (0);
        } */
    }

    /** Load Constructor */
    public X_erps_timesheet (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_timesheet[")
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

	/** Set erps_hourQuota.
		@param erps_hourQuota erps_hourQuota	  */
	public void seterps_hourQuota (int erps_hourQuota)
	{
		set_Value (COLUMNNAME_erps_hourQuota, Integer.valueOf(erps_hourQuota));
	}

	/** Get erps_hourQuota.
		@return erps_hourQuota	  */
	public int geterps_hourQuota () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_hourQuota);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_erps_scheduleRule geterps_scheduleRule() throws RuntimeException
    {
		return (I_erps_scheduleRule)MTable.get(getCtx(), I_erps_scheduleRule.Table_Name)
			.getPO(geterps_scheduleRule_ID(), get_TrxName());	}

	/** Set erps_scheduleRule.
		@param erps_scheduleRule_ID erps_scheduleRule	  */
	public void seterps_scheduleRule_ID (int erps_scheduleRule_ID)
	{
		if (erps_scheduleRule_ID < 1) 
			set_Value (COLUMNNAME_erps_scheduleRule_ID, null);
		else 
			set_Value (COLUMNNAME_erps_scheduleRule_ID, Integer.valueOf(erps_scheduleRule_ID));
	}

	/** Get erps_scheduleRule.
		@return erps_scheduleRule	  */
	public int geterps_scheduleRule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_scheduleRule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set erps_timesheet.
		@param erps_timesheet_ID erps_timesheet	  */
	public void seterps_timesheet_ID (int erps_timesheet_ID)
	{
		if (erps_timesheet_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_timesheet_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_timesheet_ID, Integer.valueOf(erps_timesheet_ID));
	}

	/** Get erps_timesheet.
		@return erps_timesheet	  */
	public int geterps_timesheet_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_timesheet_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}