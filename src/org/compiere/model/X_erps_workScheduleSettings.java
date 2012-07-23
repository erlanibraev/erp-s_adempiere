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

/** Generated Model for erps_workScheduleSettings
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_workScheduleSettings extends PO implements I_erps_workScheduleSettings, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120723L;

    /** Standard Constructor */
    public X_erps_workScheduleSettings (Properties ctx, int erps_workScheduleSettings_ID, String trxName)
    {
      super (ctx, erps_workScheduleSettings_ID, trxName);
      /** if (erps_workScheduleSettings_ID == 0)
        {
			setEndDate (new Timestamp( System.currentTimeMillis() ));
			seterps_scheduleRule_ID (0);
			seterps_workScheduleSettings_ID (0);
			setProcessed (false);
// N
			setStartDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_erps_workScheduleSettings (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_workScheduleSettings[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
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

	/** Set erps_workScheduleSettings.
		@param erps_workScheduleSettings_ID erps_workScheduleSettings	  */
	public void seterps_workScheduleSettings_ID (int erps_workScheduleSettings_ID)
	{
		if (erps_workScheduleSettings_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_workScheduleSettings_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_workScheduleSettings_ID, Integer.valueOf(erps_workScheduleSettings_ID));
	}

	/** Get erps_workScheduleSettings.
		@return erps_workScheduleSettings	  */
	public int geterps_workScheduleSettings_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_workScheduleSettings_ID);
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

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
	}
}