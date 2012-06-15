/******************************************************************************
 * Copyright (C) 2012 Duman Znunissov                                         *
 * Copyright (C) 2012 ERP-Service KazTransCom LLP                             *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.erps;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Msg;
/**
 * Fixed Asset Depreciation
 * 
 * @author Rob Klein
 * @version 	$Id: Conventions.java,v 1.0 $
 * 
 */

public class ErpsUpdater extends SvrProcess{

	/** Parameters				*/
	private String p_TableName;
	private String p_ColumnName;
	private String p_FileName;
	private String p_IDName;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() != null) {
				if ("TableName".equals(name))
					p_TableName=para[i].getParameter().toString();
				else if ("ColumnName".equals(name))
					p_ColumnName=para[i].getParameter().toString();
				else if ("IDName".equals(name))
					p_IDName=para[i].getParameter().toString();
				else if ("FileName".equals(name))
					p_FileName = para[i].getParameter().toString();
				else
					log.info("prepare - Unknown Parameter: " + name);
			}
		}
	}	//	prepare

	
	/**
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt() throws java.lang.Exception
	{
		StringBuffer result = new StringBuffer("");
		//
		StringBuffer sql = new StringBuffer();
		File m_file = new File(p_FileName);
		int i=0, j=0;
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(m_file)), 10240);
			String s = null;
			while ((s = in.readLine()) != null)
			{
				System.out.println(s);
				sql.setLength(0);
				sql.append("UPDATE "+p_TableName
						+ " SET "+p_ColumnName+"='"+s.subSequence(s.indexOf(";")+1,s.length())+"'"
						+ " WHERE " +p_IDName+"="+s.subSequence(0,s.indexOf(";")));
				j =j+ DB.executeUpdate(sql.toString(), get_TrxName());
				i++;
			}
			in.close();
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		
		String msg = Msg.translate(getCtx(), "Lines ") + " #" + i+ ", "+j;
		return msg;
	}	//	doIt
}
