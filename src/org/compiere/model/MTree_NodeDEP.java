package org.compiere.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

import org.compiere.util.CLogger;
import org.compiere.util.DB;

public class MTree_NodeDEP extends X_AD_TreeNodeDEP {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6634429332682523072L;
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (MTree_NodeDEP.class);

	public MTree_NodeDEP(Properties ctx, int AD_TreeNodeDEP_ID, String trxName) {
		super(ctx, AD_TreeNodeDEP_ID, trxName);
	}

	public MTree_NodeDEP(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * 	Get Tree Node
	 *	@param tree tree
	 *	@param Node_ID node
	 *	@return node or null
	 */
	public static MTree_NodeDEP get (MTree_Base tree, int Node_ID)
	{
		MTree_NodeDEP retValue = null;
		String sql = "SELECT * FROM AD_TreeNodeDEP WHERE AD_Tree_ID=? AND Node_ID=?";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, tree.get_TrxName());
			pstmt.setInt (1, tree.getAD_Tree_ID());
			pstmt.setInt (2, Node_ID);
			ResultSet rs = pstmt.executeQuery ();
			if (rs.next ())
				retValue = new MTree_NodeDEP (tree.getCtx(), rs, tree.get_TrxName());
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "get", e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		return retValue;
	}	//	get
	
	/**
	 * 	Full Constructor
	 *	@param tree tree
	 *	@param Node_ID node
	 */
	public MTree_NodeDEP (MTree_Base tree, int Node_ID)
	{
		super (tree.getCtx(), 0, tree.get_TrxName());
		setClientOrg(tree);
		setAD_Tree_ID (tree.getAD_Tree_ID());
		setNode_ID(Node_ID);
		//	Add to root
		setParent_ID(0);
		setSeqNo (0);
	}	//	MTree_NodeDEP

}
