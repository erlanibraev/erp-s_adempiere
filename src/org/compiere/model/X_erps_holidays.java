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

/** Generated Model for erps_holidays
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_holidays extends PO implements I_erps_holidays, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120723L;

    /** Standard Constructor */
    public X_erps_holidays (Properties ctx, int erps_holidays_ID, String trxName)
    {
      super (ctx, erps_holidays_ID, trxName);
      /** if (erps_holidays_ID == 0)
        {
			setEndDate (new Timestamp( System.currentTimeMillis() ));
			seterps_countryCode (null);
			seterps_holidays_ID (0);
			setName (null);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_erps_holidays (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_holidays[")
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

	/** Set erps_countryCode.
		@param erps_countryCode erps_countryCode	  */
	public void seterps_countryCode (String erps_countryCode)
	{
		set_Value (COLUMNNAME_erps_countryCode, erps_countryCode);
	}

	/** Get erps_countryCode.
		@return erps_countryCode	  */
	public String geterps_countryCode () 
	{
		return (String)get_Value(COLUMNNAME_erps_countryCode);
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
			set_ValueNoCheck (COLUMNNAME_erps_holidays_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_holidays_ID, Integer.valueOf(erps_holidays_ID));
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