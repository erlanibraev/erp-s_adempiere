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
import org.compiere.util.KeyNamePair;

/** Generated Model for erps_scheduleRule
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_scheduleRule extends PO implements I_erps_scheduleRule, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120723L;

    /** Standard Constructor */
    public X_erps_scheduleRule (Properties ctx, int erps_scheduleRule_ID, String trxName)
    {
      super (ctx, erps_scheduleRule_ID, trxName);
      /** if (erps_scheduleRule_ID == 0)
        {
			setDateFrom (new Timestamp( System.currentTimeMillis() ));
			setDescription (null);
			setEndDate (new Timestamp( System.currentTimeMillis() ));
			seterps_dayNumber (0);
// 1
			seterps_sampleSchedule_ID (0);
			seterps_scheduleRule_ID (0);
			setName (null);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_erps_scheduleRule (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_scheduleRule[")
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

	/** Set erps_dayNumber.
		@param erps_dayNumber erps_dayNumber	  */
	public void seterps_dayNumber (int erps_dayNumber)
	{
		set_Value (COLUMNNAME_erps_dayNumber, Integer.valueOf(erps_dayNumber));
	}

	/** Get erps_dayNumber.
		@return erps_dayNumber	  */
	public int geterps_dayNumber () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_dayNumber);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_erps_sampleSchedule geterps_sampleSchedule() throws RuntimeException
    {
		return (I_erps_sampleSchedule)MTable.get(getCtx(), I_erps_sampleSchedule.Table_Name)
			.getPO(geterps_sampleSchedule_ID(), get_TrxName());	}

	/** Set erps_sampleSchedule.
		@param erps_sampleSchedule_ID erps_sampleSchedule	  */
	public void seterps_sampleSchedule_ID (int erps_sampleSchedule_ID)
	{
		if (erps_sampleSchedule_ID < 1) 
			set_Value (COLUMNNAME_erps_sampleSchedule_ID, null);
		else 
			set_Value (COLUMNNAME_erps_sampleSchedule_ID, Integer.valueOf(erps_sampleSchedule_ID));
	}

	/** Get erps_sampleSchedule.
		@return erps_sampleSchedule	  */
	public int geterps_sampleSchedule_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_sampleSchedule_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set erps_scheduleRule.
		@param erps_scheduleRule_ID erps_scheduleRule	  */
	public void seterps_scheduleRule_ID (int erps_scheduleRule_ID)
	{
		if (erps_scheduleRule_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_scheduleRule_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_scheduleRule_ID, Integer.valueOf(erps_scheduleRule_ID));
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
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