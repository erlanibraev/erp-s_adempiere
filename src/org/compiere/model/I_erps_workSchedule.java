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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for erps_workSchedule
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS
 */
public interface I_erps_workSchedule 
{

    /** TableName=erps_workSchedule */
    public static final String Table_Name = "erps_workSchedule";

    /** AD_Table_ID=1000061 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateFrom */
    public static final String COLUMNNAME_DateFrom = "DateFrom";

	/** Set Date From.
	  * Starting date for a range
	  */
	public void setDateFrom (Timestamp DateFrom);

	/** Get Date From.
	  * Starting date for a range
	  */
	public Timestamp getDateFrom();

    /** Column name erps_dayName */
    public static final String COLUMNNAME_erps_dayName = "erps_dayName";

	/** Set erps_dayName	  */
	public void seterps_dayName (String erps_dayName);

	/** Get erps_dayName	  */
	public String geterps_dayName();

    /** Column name erps_dayoff */
    public static final String COLUMNNAME_erps_dayoff = "erps_dayoff";

	/** Set erps_dayoff	  */
	public void seterps_dayoff (boolean erps_dayoff);

	/** Get erps_dayoff	  */
	public boolean iserps_dayoff();

    /** Column name erps_daySchedule_ID */
    public static final String COLUMNNAME_erps_daySchedule_ID = "erps_daySchedule_ID";

	/** Set erps_daySchedule	  */
	public void seterps_daySchedule_ID (int erps_daySchedule_ID);

	/** Get erps_daySchedule	  */
	public int geterps_daySchedule_ID();

	public I_erps_daySchedule geterps_daySchedule() throws RuntimeException;

    /** Column name erps_holiday */
    public static final String COLUMNNAME_erps_holiday = "erps_holiday";

	/** Set erps_holiday	  */
	public void seterps_holiday (boolean erps_holiday);

	/** Get erps_holiday	  */
	public boolean iserps_holiday();

    /** Column name erps_holidays_ID */
    public static final String COLUMNNAME_erps_holidays_ID = "erps_holidays_ID";

	/** Set erps_holidays	  */
	public void seterps_holidays_ID (int erps_holidays_ID);

	/** Get erps_holidays	  */
	public int geterps_holidays_ID();

	public I_erps_holidays geterps_holidays() throws RuntimeException;

    /** Column name erps_scheduleRule_ID */
    public static final String COLUMNNAME_erps_scheduleRule_ID = "erps_scheduleRule_ID";

	/** Set erps_scheduleRule	  */
	public void seterps_scheduleRule_ID (int erps_scheduleRule_ID);

	/** Get erps_scheduleRule	  */
	public int geterps_scheduleRule_ID();

	public I_erps_scheduleRule geterps_scheduleRule() throws RuntimeException;

    /** Column name erps_workSchedule_ID */
    public static final String COLUMNNAME_erps_workSchedule_ID = "erps_workSchedule_ID";

	/** Set erps_workSchedule	  */
	public void seterps_workSchedule_ID (int erps_workSchedule_ID);

	/** Get erps_workSchedule	  */
	public int geterps_workSchedule_ID();

    /** Column name erps_workScheduleSettings_ID */
    public static final String COLUMNNAME_erps_workScheduleSettings_ID = "erps_workScheduleSettings_ID";

	/** Set erps_workScheduleSettings	  */
	public void seterps_workScheduleSettings_ID (int erps_workScheduleSettings_ID);

	/** Get erps_workScheduleSettings	  */
	public int geterps_workScheduleSettings_ID();

	public I_erps_workScheduleSettings geterps_workScheduleSettings() throws RuntimeException;

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
