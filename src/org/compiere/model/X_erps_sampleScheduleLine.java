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

/** Generated Model for erps_sampleScheduleLine
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_sampleScheduleLine extends PO implements I_erps_sampleScheduleLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120723L;

    /** Standard Constructor */
    public X_erps_sampleScheduleLine (Properties ctx, int erps_sampleScheduleLine_ID, String trxName)
    {
      super (ctx, erps_sampleScheduleLine_ID, trxName);
      /** if (erps_sampleScheduleLine_ID == 0)
        {
			seterps_daySchedule_ID (0);
			seterps_sampleSchedule_ID (0);
			seterps_sampleScheduleLine_ID (0);
			seterps_workDays (null);
        } */
    }

    /** Load Constructor */
    public X_erps_sampleScheduleLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_sampleScheduleLine[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set erps_sampleScheduleLine.
		@param erps_sampleScheduleLine_ID erps_sampleScheduleLine	  */
	public void seterps_sampleScheduleLine_ID (int erps_sampleScheduleLine_ID)
	{
		if (erps_sampleScheduleLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_sampleScheduleLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_sampleScheduleLine_ID, Integer.valueOf(erps_sampleScheduleLine_ID));
	}

	/** Get erps_sampleScheduleLine.
		@return erps_sampleScheduleLine	  */
	public int geterps_sampleScheduleLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_sampleScheduleLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** erps_workDays AD_Reference_ID=1000049 */
	public static final int ERPS_WORKDAYS_AD_Reference_ID=1000049;
	/** First = 10000014 */
	public static final String ERPS_WORKDAYS_First = "10000014";
	/** Second = 10000015 */
	public static final String ERPS_WORKDAYS_Second = "10000015";
	/** Third = 10000016 */
	public static final String ERPS_WORKDAYS_Third = "10000016";
	/** Fourth = 10000017 */
	public static final String ERPS_WORKDAYS_Fourth = "10000017";
	/** Fifth = 10000018 */
	public static final String ERPS_WORKDAYS_Fifth = "10000018";
	/** Sixth = 10000019 */
	public static final String ERPS_WORKDAYS_Sixth = "10000019";
	/** Seventh = 10000020 */
	public static final String ERPS_WORKDAYS_Seventh = "10000020";
	/** Set erps_workDays.
		@param erps_workDays erps_workDays	  */
	public void seterps_workDays (String erps_workDays)
	{

		set_Value (COLUMNNAME_erps_workDays, erps_workDays);
	}

	/** Get erps_workDays.
		@return erps_workDays	  */
	public String geterps_workDays () 
	{
		return (String)get_Value(COLUMNNAME_erps_workDays);
	}
}