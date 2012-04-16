package org.compiere.apps.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.form.PaySelect;
import org.compiere.apps.form.PaySelect.BankInfo;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.model.MRole;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;

public class ReportOsv implements FormPanel, ActionListener, TableModelListener, ASyncProcess
{
	
	/**	Logger			*/
	public static CLogger log = CLogger.getCLogger(ReportOsv.class);
	
	/**	FormFrame			*/
	private FormFrame 		m_frame;
	/**	Window No			*/
	public int         	m_WindowNo = 0;
	/** @todo withholding */
	private CPanel panel = new CPanel();
	/**/
	public boolean         m_isLocked = false;
	
	private static final long serialVersionUID = 6917300855914216888L;

	@Override
	public void lockUI(ProcessInfo pi) {
		panel.setEnabled(false);
		m_isLocked = true;
		
	}

	@Override
	public void unlockUI(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUILocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void executeASync(ProcessInfo pi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//  Generate PaySelection
		if (e.getSource() == bGenerate)
		{

		}else if(e.getSource() == bCancel){
			dispose();
		}else if(e.getSource() == fieldBankAccount){
			if(sub1.isVisible())
				sub1.setVisible(false);
			else
				sub1.setVisible(true);
		}
		
	}
	
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel parameterPanel = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private JButton bGenerate = ConfirmPanel.createOKButton(true);
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	private VCheckBox sub1 = new VCheckBox();
	private CPanel commandPanel = new CPanel();
	private FlowLayout commandLayout = new FlowLayout();
	private CLabel labelBankAccount = new CLabel();
	private VComboBox fieldBankAccount = new VComboBox();
	
	@Override
	public void init(int WindowNo, FormFrame frame) {
		
		// log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			CompiereColor.setBackground(panel);
			//
			mainPanel.setLayout(mainLayout);
			parameterPanel.setLayout(parameterLayout);
			//
			sub1.setText(Msg.getMsg(Env.getCtx(), "OnlyDue"));
			labelBankAccount.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
			fieldBankAccount.addActionListener(this);
			//
			bGenerate.addActionListener(this);
			bCancel.addActionListener(this);
			mainPanel.add(parameterPanel, BorderLayout.NORTH);
			parameterPanel.add(labelBankAccount,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
				parameterPanel.add(fieldBankAccount,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			commandPanel.setLayout(commandLayout);
			commandLayout.setAlignment(FlowLayout.RIGHT);
			commandLayout.setHgap(10);
			commandPanel.add(bCancel, null);
			commandPanel.add(bGenerate, null);
			parameterPanel.add(sub1,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			
	
			ArrayList<AccountInfo> accountData = getAccountData();
			for(AccountInfo bis : accountData)
				fieldBankAccount.addItem(bis);

			if (fieldBankAccount.getItemCount() == 0)
				ADialog.error(m_WindowNo, panel, "VPaySelectNoBank");
			else
				fieldBankAccount.setSelectedIndex(0);
			
//			jbInit();
			//dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		
	}

	@Override
	public void dispose() {
		if (m_frame != null)
			m_frame.dispose();
		m_frame = null;
		
	}
	
	private ArrayList<AccountInfo> getAccountData()
	{
		ArrayList<AccountInfo> data = new ArrayList<AccountInfo>();
		//
		int m_AD_Client_ID = Env.getAD_Client_ID(Env.getCtx());
		//  Bank Account Info
		String sql = MRole.getDefault().addAccessSQL(
			"SELECT c_elementvalue_id, value||' - '||name as name"
			+" FROM c_elementvalue WHERE ad_client_id=1000000 AND isactive='Y'"
			+" ORDER BY value",
			"c_elementvalue", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RW);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				AccountInfo bi = new AccountInfo (rs.getInt(1), rs.getString(2));
				data.add(bi);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return data;
	}
	
	/**************************************************************************
	 *  Bank Account Info
	 */
	public class BankInfos
	{
		/**
		 * 	BankInfo
		 *	@param newC_BankAccount_ID
		 *	@param newC_Currency_ID
		 *	@param newName
		 *	@param newCurrency
		 *	@param newBalance
		 *	@param newTransfers
		 */
		public BankInfos (int newC_BankAccount_ID, int newC_Currency_ID,
			String newName, String newCurrency, boolean newTransfers)
		{
			C_BankAccount_ID = newC_BankAccount_ID;
			C_Currency_ID = newC_Currency_ID;
			Name = newName;
			Currency = newCurrency;
		}
		int C_BankAccount_ID;
		int C_Currency_ID;
		String Name;
		public String Currency;
		public Double Balance;
		boolean Transfers;

		/**
		 * 	to String
		 *	@return info
		 */
		public String toString()
		{
			return Name;
		}
	}   //  BankInfo

	/**************************************************************************
	 *  Account Info
	 */
	public class AccountInfo
	{
		/**
		 * 	AccountInfo
		 *	@param Account_ID
		 *	@param AccountName
		 */
		public AccountInfo (int Account_ID, String AccountName)
		{
			mAccount_ID = Account_ID;
			mAccountName = AccountName;
		}
		int mAccount_ID;
		String mAccountName;

		/**
		 * 	to String
		 *	@return info
		 */
		public String toString()
		{
			return mAccountName;
		}
	}   //  AccountInfo

}
