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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.compiere.apps.ADialog;
import org.compiere.apps.ConfirmPanel;
import org.compiere.apps.IProcessParameter;
import org.compiere.apps.ProcessCtl;
import org.compiere.apps.ProcessParameterPanel;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.apps.form.PaySelect;
import org.compiere.apps.form.PaySelect.BankInfo;
import org.compiere.grid.ed.VCheckBox;
import org.compiere.grid.ed.VComboBox;
import org.compiere.grid.ed.VDate;
import org.compiere.model.MRole;
import org.compiere.model.X_C_PaySelection;
import org.compiere.plaf.CompiereColor;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.util.ASyncProcess;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
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
			//  Prepare Process
			int AD_Proces_ID = 1000044;	
			ProcessInfo pi = new ProcessInfo (m_frame.getTitle(), AD_Proces_ID);
			pi.setAD_User_ID (Env.getAD_User_ID(Env.getCtx()));
			pi.setAD_Client_ID(Env.getAD_Client_ID(Env.getCtx()));

			List<ProcessInfoParameter> po = new ArrayList<ProcessInfoParameter>();
			AccountInfo mm = (AccountInfo)fieldAccount.getSelectedItem();
			
				po.add(new ProcessInfoParameter("account_id",new BigDecimal(mm.mAccount_ID),null,"",""));
			if((Boolean) sub1.getValue())
				po.add(new ProcessInfoParameter("sub1","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub1","N",null,"",""));
			if((Boolean) sub2.getValue())
				po.add(new ProcessInfoParameter("sub2","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub2","N",null,"",""));
			if((Boolean) sub3.getValue())
				po.add(new ProcessInfoParameter("sub3","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub3","N",null,"",""));
			if((Boolean) sub4.getValue())
				po.add(new ProcessInfoParameter("sub4","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub4","N",null,"",""));
			if((Boolean) sub5.getValue())
				po.add(new ProcessInfoParameter("sub5","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub5","N",null,"",""));
			if((Boolean) sub6.getValue())
				po.add(new ProcessInfoParameter("sub6","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub6","N",null,"",""));
			if((Boolean) sub7.getValue())
				po.add(new ProcessInfoParameter("sub7","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub7","N",null,"",""));
			if((Boolean) sub8.getValue())
				po.add(new ProcessInfoParameter("sub8","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub8","N",null,"",""));
			if((Boolean) sub9.getValue())
				po.add(new ProcessInfoParameter("sub9","Y",null,"",""));
			else
				po.add(new ProcessInfoParameter("sub9","N",null,"",""));
			po.add(new ProcessInfoParameter("date1",(Timestamp)fieldDate1.getValue(),null,"",""));
			po.add(new ProcessInfoParameter("date2",(Timestamp)fieldDate2.getValue(),null,"",""));
			
			po.add(new ProcessInfoParameter("osv","N",null,"",""));
			//
			ProcessInfoParameter[] pp = new ProcessInfoParameter[po.size()];
			po.toArray(pp);
			pi.setParameter(pp);
			
			ProcessParameterPanel pu = new ProcessParameterPanel(m_WindowNo, pi);
			//	Execute Process
			ProcessCtl.process(this, m_WindowNo, (IProcessParameter) pu, pi, null);
			
		}else if(e.getSource() == bCancel){
			dispose();
		}else if(e.getSource() == fieldAccount){
			
		}else if(e.getSource() == sub1){
			sub7.setVisible(false);
			sub8.setVisible(false);
			sub9.setVisible(false);
		}
		
	}
	
	@Override
	public void init(int WindowNo, FormFrame frame) {
		
		// log.info("");
		m_WindowNo = WindowNo;
		m_frame = frame;
		try
		{
			jbInit();
			dynInit();
			frame.getContentPane().add(commandPanel, BorderLayout.SOUTH);
			frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		
	}
	
	//
	private CPanel mainPanel = new CPanel();
	private BorderLayout mainLayout = new BorderLayout();
	private CPanel PanelAccount = new CPanel();
	private CPanel PanelDate = new CPanel();
	private CPanel PanelSub = new CPanel();
	private GridBagLayout parameterLayout = new GridBagLayout();
	private CPanel commandPanel = new CPanel();
	private FlowLayout commandLayout = new FlowLayout();
	//
	private JButton bGenerate = ConfirmPanel.createOKButton(true);
	private JButton bCancel = ConfirmPanel.createCancelButton(true);
	//
	private CLabel labelAccount = new CLabel();
	private VComboBox fieldAccount = new VComboBox();
	private CLabel labelDate1 = new CLabel();
	private VDate fieldDate1 = new VDate();
	private CLabel labelDate2 = new CLabel();
	private VDate fieldDate2 = new VDate();
	private VCheckBox sub1 = new VCheckBox();
	private VCheckBox sub2 = new VCheckBox();
	private VCheckBox sub3 = new VCheckBox();
	private VCheckBox sub4 = new VCheckBox();
	private VCheckBox sub5 = new VCheckBox();
	private VCheckBox sub6 = new VCheckBox();
	private VCheckBox sub7 = new VCheckBox();
	private VCheckBox sub8 = new VCheckBox();
	private VCheckBox sub9 = new VCheckBox();
	
	/**
	 *  Static Init
	 *  @throws Exception
	 */
	private void jbInit() throws Exception
	{
		CompiereColor.setBackground(panel);
		//
		mainPanel.setLayout(mainLayout);
		PanelAccount.setLayout(parameterLayout);
		PanelDate.setLayout(parameterLayout);
		PanelSub.setLayout(parameterLayout);
		//
		sub1.setText(Msg.getMsg(Env.getCtx(), "sub1"));
		sub2.setText(Msg.getMsg(Env.getCtx(), "sub2"));
		sub3.setText(Msg.getMsg(Env.getCtx(), "sub3"));
		sub4.setText(Msg.getMsg(Env.getCtx(), "sub4"));
		sub5.setText(Msg.getMsg(Env.getCtx(), "sub5"));
		sub6.setText(Msg.getMsg(Env.getCtx(), "sub6"));
		sub7.setText(Msg.getMsg(Env.getCtx(), "sub7"));
		sub8.setText(Msg.getMsg(Env.getCtx(), "sub8"));
		sub9.setText(Msg.getMsg(Env.getCtx(), "sub9"));
		labelAccount.setText(Msg.translate(Env.getCtx(), "C_BankAccount_ID"));
		labelDate1.setText(Msg.translate(Env.getCtx(), "date.from"));
		labelDate2.setText(Msg.translate(Env.getCtx(), "date.to"));
		fieldAccount.addActionListener(this);
		sub1.addActionListener(this);
		//
		bGenerate.addActionListener(this);
		bCancel.addActionListener(this);
		mainPanel.add(PanelAccount, BorderLayout.NORTH);
		mainPanel.add(PanelDate, BorderLayout.CENTER);
		mainPanel.add(PanelSub, BorderLayout.SOUTH);
		PanelAccount.add(labelAccount,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelAccount.add(fieldAccount,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		PanelDate.add(labelDate1,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelDate.add(fieldDate1,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
					,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		PanelDate.add(labelDate2,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelDate.add(fieldDate2,   new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		// Contractor (Contract)
		PanelSub.add(sub1,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelSub.add(sub2,  new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelSub.add(sub3,  new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		// Contractor (Project)
		PanelSub.add(sub4,  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 55, 5, 5), 0, 0));
		PanelSub.add(sub5,  new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 55, 5, 5), 0, 0));
		PanelSub.add(sub6,  new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 55, 5, 5), 0, 0));
		//
		PanelSub.add(sub7,  new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelSub.add(sub8,  new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		PanelSub.add(sub9,  new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0
				,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		
		commandPanel.setLayout(commandLayout);
		commandLayout.setAlignment(FlowLayout.RIGHT);
		commandLayout.setHgap(10);
		commandPanel.add(bCancel, null);
		commandPanel.add(bGenerate, null);
		
	}
	
	/**
	 *  Dynamic Init.
	 *  - Load Account Info
	 */
	private void dynInit()
	{
		ArrayList<AccountInfo> accountData = getAccountData();
		for(AccountInfo bis : accountData)
			fieldAccount.addItem(bis);

		if (fieldAccount.getItemCount() == 0)
			ADialog.error(m_WindowNo, panel, "account.select");
		else
			fieldAccount.setSelectedIndex(0);
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
			+" FROM c_elementvalue WHERE ad_client_id=? AND isactive='Y'"
			+" ORDER BY value",
			"c_elementvalue", MRole.SQL_FULLYQUALIFIED, MRole.SQL_RW);
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, m_AD_Client_ID);
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
	 *  Account Info
	 */
	public class AccountInfo
	{

		private int mAccount_ID;
		private String mAccountName;
		
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
