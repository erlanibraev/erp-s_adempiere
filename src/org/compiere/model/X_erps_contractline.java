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
import org.compiere.util.KeyNamePair;

/** Generated Model for erps_contractline
 *  @author Adempiere (generated) 
 *  @version Release 3.7.0LTS - $Id$ */
public class X_erps_contractline extends PO implements I_erps_contractline, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20120717L;

    /** Standard Constructor */
    public X_erps_contractline (Properties ctx, int erps_contractline_ID, String trxName)
    {
      super (ctx, erps_contractline_ID, trxName);
      /** if (erps_contractline_ID == 0)
        {
			setCommittedAmt (Env.ZERO);
			setC_Tax_ID (0);
			seterps_contract_ID (0);
			seterps_contractline_ID (0);
			setIsCommitCeiling (false);
			setIsComplete (false);
			setName (null);
			setPlannedAmt (Env.ZERO);
			setProjInvoiceRule (null);
// @ProjInvoiceRule@
			setSeqNo (0);
// @SQL=SELECT NVL(MAX(SeqNo),0)+10 AS DefaultValue FROM C_ProjectPhase WHERE C_Project_ID=@C_Project_ID@
        } */
    }

    /** Load Constructor */
    public X_erps_contractline (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_erps_contractline[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Committed Amount.
		@param CommittedAmt 
		The (legal) commitment amount
	  */
	public void setCommittedAmt (BigDecimal CommittedAmt)
	{
		set_Value (COLUMNNAME_CommittedAmt, CommittedAmt);
	}

	/** Get Committed Amount.
		@return The (legal) commitment amount
	  */
	public BigDecimal getCommittedAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CommittedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Tax getC_Tax() throws RuntimeException
    {
		return (org.compiere.model.I_C_Tax)MTable.get(getCtx(), org.compiere.model.I_C_Tax.Table_Name)
			.getPO(getC_Tax_ID(), get_TrxName());	}

	/** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
	public void setC_Tax_ID (int C_Tax_ID)
	{
		if (C_Tax_ID < 1) 
			set_Value (COLUMNNAME_C_Tax_ID, null);
		else 
			set_Value (COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
	}

	/** Get Tax.
		@return Tax identifier
	  */
	public int getC_Tax_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Tax_ID);
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

	public I_erps_contract geterps_contract() throws RuntimeException
    {
		return (I_erps_contract)MTable.get(getCtx(), I_erps_contract.Table_Name)
			.getPO(geterps_contract_ID(), get_TrxName());	}

	/** Set erps_contract.
		@param erps_contract_ID erps_contract	  */
	public void seterps_contract_ID (int erps_contract_ID)
	{
		if (erps_contract_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_contract_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_contract_ID, Integer.valueOf(erps_contract_ID));
	}

	/** Get erps_contract.
		@return erps_contract	  */
	public int geterps_contract_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_contract_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set erps_contractline.
		@param erps_contractline_ID erps_contractline	  */
	public void seterps_contractline_ID (int erps_contractline_ID)
	{
		if (erps_contractline_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_erps_contractline_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_erps_contractline_ID, Integer.valueOf(erps_contractline_ID));
	}

	/** Get erps_contractline.
		@return erps_contractline	  */
	public int geterps_contractline_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_erps_contractline_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set erps_documentSignDate.
		@param erps_documentSignDate erps_documentSignDate	  */
	public void seterps_documentSignDate (Timestamp erps_documentSignDate)
	{
		set_Value (COLUMNNAME_erps_documentSignDate, erps_documentSignDate);
	}

	/** Get erps_documentSignDate.
		@return erps_documentSignDate	  */
	public Timestamp geterps_documentSignDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_erps_documentSignDate);
	}

	/** Set erps_kazsoderzhanieVistavleno.
		@param erps_kazsoderzhanieVistavleno erps_kazsoderzhanieVistavleno	  */
	public void seterps_kazsoderzhanieVistavleno (boolean erps_kazsoderzhanieVistavleno)
	{
		set_Value (COLUMNNAME_erps_kazsoderzhanieVistavleno, Boolean.valueOf(erps_kazsoderzhanieVistavleno));
	}

	/** Get erps_kazsoderzhanieVistavleno.
		@return erps_kazsoderzhanieVistavleno	  */
	public boolean iserps_kazsoderzhanieVistavleno () 
	{
		Object oo = get_Value(COLUMNNAME_erps_kazsoderzhanieVistavleno);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set erps_overallPlannedAmount.
		@param erps_overallPlannedAmount erps_overallPlannedAmount	  */
	public void seterps_overallPlannedAmount (BigDecimal erps_overallPlannedAmount)
	{
		set_Value (COLUMNNAME_erps_overallPlannedAmount, erps_overallPlannedAmount);
	}

	/** Get erps_overallPlannedAmount.
		@return erps_overallPlannedAmount	  */
	public BigDecimal geterps_overallPlannedAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_erps_overallPlannedAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Generate Order.
		@param GenerateOrder 
		Generate Order
	  */
	public void setGenerateOrder (String GenerateOrder)
	{
		set_Value (COLUMNNAME_GenerateOrder, GenerateOrder);
	}

	/** Get Generate Order.
		@return Generate Order
	  */
	public String getGenerateOrder () 
	{
		return (String)get_Value(COLUMNNAME_GenerateOrder);
	}

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set Commitment is Ceiling.
		@param IsCommitCeiling 
		The commitment amount/quantity is the chargeable ceiling 
	  */
	public void setIsCommitCeiling (boolean IsCommitCeiling)
	{
		set_Value (COLUMNNAME_IsCommitCeiling, Boolean.valueOf(IsCommitCeiling));
	}

	/** Get Commitment is Ceiling.
		@return The commitment amount/quantity is the chargeable ceiling 
	  */
	public boolean isCommitCeiling () 
	{
		Object oo = get_Value(COLUMNNAME_IsCommitCeiling);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Complete.
		@param IsComplete 
		It is complete
	  */
	public void setIsComplete (boolean IsComplete)
	{
		set_Value (COLUMNNAME_IsComplete, Boolean.valueOf(IsComplete));
	}

	/** Get Complete.
		@return It is complete
	  */
	public boolean isComplete () 
	{
		Object oo = get_Value(COLUMNNAME_IsComplete);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
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

	/** Set Planned Amount.
		@param PlannedAmt 
		Planned amount for this project
	  */
	public void setPlannedAmt (BigDecimal PlannedAmt)
	{
		set_Value (COLUMNNAME_PlannedAmt, PlannedAmt);
	}

	/** Get Planned Amount.
		@return Planned amount for this project
	  */
	public BigDecimal getPlannedAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PlannedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Unit Price.
		@param PriceActual 
		Actual Price 
	  */
	public void setPriceActual (BigDecimal PriceActual)
	{
		set_Value (COLUMNNAME_PriceActual, PriceActual);
	}

	/** Get Unit Price.
		@return Actual Price 
	  */
	public BigDecimal getPriceActual () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_PriceActual);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** ProjInvoiceRule AD_Reference_ID=383 */
	public static final int PROJINVOICERULE_AD_Reference_ID=383;
	/** None = - */
	public static final String PROJINVOICERULE_None = "-";
	/** Committed Amount = C */
	public static final String PROJINVOICERULE_CommittedAmount = "C";
	/** Time&Material max Comitted = c */
	public static final String PROJINVOICERULE_TimeMaterialMaxComitted = "c";
	/** Time&Material = T */
	public static final String PROJINVOICERULE_TimeMaterial = "T";
	/** Product  Quantity = P */
	public static final String PROJINVOICERULE_ProductQuantity = "P";
	/** Set Invoice Rule.
		@param ProjInvoiceRule 
		Invoice Rule for the project
	  */
	public void setProjInvoiceRule (String ProjInvoiceRule)
	{

		set_Value (COLUMNNAME_ProjInvoiceRule, ProjInvoiceRule);
	}

	/** Get Invoice Rule.
		@return Invoice Rule for the project
	  */
	public String getProjInvoiceRule () 
	{
		return (String)get_Value(COLUMNNAME_ProjInvoiceRule);
	}

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getSeqNo()));
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

	/** Set Tax Amount.
		@param TaxAmt 
		Tax Amount for a document
	  */
	public void setTaxAmt (BigDecimal TaxAmt)
	{
		set_Value (COLUMNNAME_TaxAmt, TaxAmt);
	}

	/** Get Tax Amount.
		@return Tax Amount for a document
	  */
	public BigDecimal getTaxAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Tax base Amount.
		@param TaxBaseAmt 
		Base for calculating the tax amount
	  */
	public void setTaxBaseAmt (BigDecimal TaxBaseAmt)
	{
		set_Value (COLUMNNAME_TaxBaseAmt, TaxBaseAmt);
	}

	/** Get Tax base Amount.
		@return Base for calculating the tax amount
	  */
	public BigDecimal getTaxBaseAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TaxBaseAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}