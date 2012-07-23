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

/** Generated Model for erps_workSchedule
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_workSchedule extends PO implements I_erps_workSchedule, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120723L;

    /** Standard Constructor */
    public X_erps_workSchedule (Properties ctx, int erps_workSchedule_ID, String trxName)
    {
      super (ctx, erps_workSchedule_ID, trxName);
      /** if (erps_workSchedule_ID == 0)
        {
			seterps_daySchedule_ID (0);
			seterps_scheduleRule_ID (0);
			seterps_workSchedule_ID (0);
			seterps_workScheduleSettings_ID (0);
        } */
    }

    /** Load Constructor */
    public X_erps_workSchedule (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_workSchedule[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set erps_dayName.
		@param erps_dayName erps_dayName	  */
	public void seterps_dayName (String erps_dayName)
	{
		set_Value (COLUMNNAME_erps_dayName, erps_dayName);
	}

	/** Get erps_dayName.
		@return erps_dayName	  */
	public String geterps_dayName () 
	{
		return (String)get_Value(COLUMNNAME_erps_dayName);
	}

	/** Set erps_dayoff.
		@param erps_dayoff erps_dayoff	  */
	public void seterps_dayoff (boolean erps_dayoff)
	{
		set_Value (COLUMNNAME_erps_dayoff, Boolean.valueOf(erps_dayoff));
	}

	/** Get erps_dayoff.
		@return erps_dayoff	  */
	public boolean iserps_dayoff () 
	{
		Object oo = get_Value(COLUMNNAME_erps_dayoff);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_erps_daySchedule geterps_daySchedule() throws RuntimeException
    {
		return (I_erps_daySchedule)MTable.get(getCtx(), I_erps_daySchedule.Table_Name)
			.getPO(geterps_daySchedule_ID(), get_TrxName());	}

	/** Set erps_daySchedule.
		@param erps_daySchedule_ID erps_daySchedule	  */
	public void seterps_daySchedule_ID (int erps_daySchedule_ID)
	{
		if (erps_daySchedule_ID < 1) 
			set_Value (COLUMNNAME_erps_daySchedule_ID, null);
		else 
			set_Value (COLUMNNAME_erps_daySchedule_ID, Integer.valueOf(erps_daySchedule_ID));
	}

	/** Get erps_daySchedule.
		@return erps_daySchedule	  */
	public int geterps_daySchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_daySchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set erps_holiday.
		@param erps_holiday erps_holiday	  */
	public void seterps_holiday (boolean erps_holiday)
	{
		set_Value (COLUMNNAME_erps_holiday, Boolean.valueOf(erps_holiday));
	}

	/** Get erps_holiday.
		@return erps_holiday	  */
	public boolean iserps_holiday () 
	{
		Object oo = get_Value(COLUMNNAME_erps_holiday);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public I_erps_holidays geterps_holidays() throws RuntimeException
    {
		return (I_erps_holidays)MTable.get(getCtx(), I_erps_holidays.Table_Name)
			.getPO(geterps_holidays_ID(), get_TrxName());	}

	/** Set erps_holidays.
		@param erps_holidays_ID erps_holidays	  */
	public void seterps_holidays_ID (int erps_holidays_ID)
	{
		if (erps_holidays_ID < 1) 
			set_Value (COLUMNNAME_erps_holidays_ID, null);
		else 
			set_Value (COLUMNNAME_erps_holidays_ID, Integer.valueOf(erps_holidays_ID));
	}

	/** Get erps_holidays.
		@return erps_holidays	  */
	public int geterps_holidays_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_holidays_ID);
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

	/** Set erps_workSchedule.
		@param erps_workSchedule_ID erps_workSchedule	  */
	public void seterps_workSchedule_ID (int erps_workSchedule_ID)
	{
		if (erps_workSchedule_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_workSchedule_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_workSchedule_ID, Integer.valueOf(erps_workSchedule_ID));
	}

	/** Get erps_workSchedule.
		@return erps_workSchedule	  */
	public int geterps_workSchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_workSchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_erps_workScheduleSettings geterps_workScheduleSettings() throws RuntimeException
    {
		return (I_erps_workScheduleSettings)MTable.get(getCtx(), I_erps_workScheduleSettings.Table_Name)
			.getPO(geterps_workScheduleSettings_ID(), get_TrxName());	}

	/** Set erps_workScheduleSettings.
		@param erps_workScheduleSettings_ID erps_workScheduleSettings	  */
	public void seterps_workScheduleSettings_ID (int erps_workScheduleSettings_ID)
	{
		if (erps_workScheduleSettings_ID < 1) 
			set_Value (COLUMNNAME_erps_workScheduleSettings_ID, null);
		else 
			set_Value (COLUMNNAME_erps_workScheduleSettings_ID, Integer.valueOf(erps_workScheduleSettings_ID));
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
}