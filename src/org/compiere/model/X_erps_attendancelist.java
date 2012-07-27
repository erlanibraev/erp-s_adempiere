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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for erps_attendancelist
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_attendancelist extends PO implements I_erps_attendancelist, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120727L;

    /** Standard Constructor */
    public X_erps_attendancelist (Properties ctx, int erps_attendancelist_ID, String trxName)
    {
      super (ctx, erps_attendancelist_ID, trxName);
      /** if (erps_attendancelist_ID == 0)
        {
			setC_BPartner_ID (0);
			setEndDate (new Timestamp( System.currentTimeMillis() ));
			seterps_attendancelist_ID (0);
			seterps_attendanceType (null);
			setStartDate (new Timestamp( System.currentTimeMillis() ));
        } */
    }

    /** Load Constructor */
    public X_erps_attendancelist (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_attendancelist[")
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

	/** Set attendancelist.
		@param erps_attendancelist_ID attendancelist	  */
	public void seterps_attendancelist_ID (int erps_attendancelist_ID)
	{
		if (erps_attendancelist_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_attendancelist_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_attendancelist_ID, Integer.valueOf(erps_attendancelist_ID));
	}

	/** Get attendancelist.
		@return attendancelist	  */
	public int geterps_attendancelist_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_attendancelist_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** erps_attendanceType AD_Reference_ID=1000052 */
	public static final int ERPS_ATTENDANCETYPE_AD_Reference_ID=1000052;
	/** Weekends, holidays = 10000036 */
	public static final String ERPS_ATTENDANCETYPE_WeekendsHolidays = "10000036";
	/** Overtime = 10000037 */
	public static final String ERPS_ATTENDANCETYPE_Overtime = "10000037";
	/** Set erps_attendanceType.
		@param erps_attendanceType erps_attendanceType	  */
	public void seterps_attendanceType (String erps_attendanceType)
	{

		set_Value (COLUMNNAME_erps_attendanceType, erps_attendanceType);
	}

	/** Get erps_attendanceType.
		@return erps_attendanceType	  */
	public String geterps_attendanceType () 
	{
		return (String)get_Value(COLUMNNAME_erps_attendanceType);
	}

	/** Set erps_hourQuota.
		@param erps_hourQuota erps_hourQuota	  */
	public void seterps_hourQuota (BigDecimal erps_hourQuota)
	{
		set_Value (COLUMNNAME_erps_hourQuota, erps_hourQuota);
	}

	/** Get erps_hourQuota.
		@return erps_hourQuota	  */
	public BigDecimal geterps_hourQuota () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_erps_hourQuota);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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