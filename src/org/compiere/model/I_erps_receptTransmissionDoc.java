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

/** Generated Interface for erps_receptTransmissionDoc
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS
 */
public interface I_erps_receptTransmissionDoc 
{

    /** TableName=erps_receptTransmissionDoc */
    public static final String Table_Name = "erps_receptTransmissionDoc";

    /** AD_Table_ID=1000026 */
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

    /** Column name DateDoc */
    public static final String COLUMNNAME_DateDoc = "DateDoc";

	/** Set Document Date.
	  * Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc);

	/** Get Document Date.
	  * Date of the Document
	  */
	public Timestamp getDateDoc();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name erps_Accountant */
    public static final String COLUMNNAME_erps_Accountant = "erps_Accountant";

	/** Set erps_Accountant	  */
	public void seterps_Accountant (int erps_Accountant);

	/** Get erps_Accountant	  */
	public int geterps_Accountant();

	public org.compiere.model.I_C_BPartner geterps_Account() throws RuntimeException;

    /** Column name erps_fromResponsible */
    public static final String COLUMNNAME_erps_fromResponsible = "erps_fromResponsible";

	/** Set erps_fromResponsible	  */
	public void seterps_fromResponsible (int erps_fromResponsible);

	/** Get erps_fromResponsible	  */
	public int geterps_fromResponsible();

	public org.compiere.model.I_C_BPartner geterps_fromResponsi() throws RuntimeException;

    /** Column name erps_receptTransmissionDoc_ID */
    public static final String COLUMNNAME_erps_receptTransmissionDoc_ID = "erps_receptTransmissionDoc_ID";

	/** Set erps_receptTransmissionDoc_ID	  */
	public void seterps_receptTransmissionDoc_ID (int erps_receptTransmissionDoc_ID);

	/** Get erps_receptTransmissionDoc_ID	  */
	public int geterps_receptTransmissionDoc_ID();

    /** Column name Erps_Specialist */
    public static final String COLUMNNAME_Erps_Specialist = "Erps_Specialist";

	/** Set Erps_Specialist	  */
	public void setErps_Specialist (int Erps_Specialist);

	/** Get Erps_Specialist	  */
	public int getErps_Specialist();

	public org.compiere.model.I_C_BPartner getErps_Special() throws RuntimeException;

    /** Column name erps_toResponsible */
    public static final String COLUMNNAME_erps_toResponsible = "erps_toResponsible";

	/** Set erps_toResponsible	  */
	public void seterps_toResponsible (int erps_toResponsible);

	/** Get erps_toResponsible	  */
	public int geterps_toResponsible();

	public org.compiere.model.I_C_BPartner geterps_toResponsi() throws RuntimeException;

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

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();
}
