
package org.adempiere.process.rpl.imp;

import java.util.Properties;
import java.util.logging.Level;

import org.adempiere.process.rpl.XMLHelper;
import org.compiere.Adempiere;
import org.compiere.process.ProcessInfo;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.CLogMgt;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.w3c.dom.Document;
import org.adempiere.process.rpl.imp.Import1C;

public class ImporterFrom1C extends SvrProcess {
	private	Properties p_param = new Properties();
		/**
		 * Get Parameters
		 */
		protected void prepare() {
			// Parameter
			ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++) {
				String name = para[i].getParameterName();
				if (para[i].getParameter() == null);
				else if ("FileName".equals(name))
					p_param.setProperty("FileName", para[i].getParameter().toString());
				else if ("DocAction".equals(name))
					p_param.setProperty("DocAction", para[i].getParameter().toString());
				else if ("DocStatus".equals(name))
					p_param.setProperty("DocStatus", para[i].getParameter().toString());
				else if ("Payments".equals(name))
					p_param.setProperty("Payments", para[i].getParameter().toString());
				else if ("BankStatements".equals(name))
					p_param.setProperty("BankStatements", para[i].getParameter().toString());
				else if ("Cash".equals(name))
					p_param.setProperty("Cash", para[i].getParameter().toString());
				else if ("ReceivedInvoices".equals(name))
					p_param.setProperty("ReceivedInvoices", para[i].getParameter().toString());
				else if ("IssuedInvoices".equals(name))
					p_param.setProperty("IssuedInvoices", para[i].getParameter().toString());
				else if ("SalesOrders".equals(name))
					p_param.setProperty("SalesOrders", para[i].getParameter().toString());
				else if ("GL".equals(name))
					p_param.setProperty("GL", para[i].getParameter().toString());
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
			
		}

		/**
		 * Process
		 * 
		 * @return info
		 */
		protected String doIt() throws Exception 
		{
			StringBuffer result = new StringBuffer("");
			
			// Load XML file and parse it
			/*String fileNameOr = org.compiere.util.Ini.findAdempiereHome()
			+ System.getProperty("file.separator")  
			+ "data"
			+ System.getProperty("file.separator");
			
			String pathToXmlFile = fileNameOr+"XmlExport-test.xml";
			*/
			Document documentToBeImported = XMLHelper.createDocumentFromFile(p_param.getProperty("FileName",
					"C:"+System.getProperty("file.separator")+"Import.xml"));
			
			Import1C imp1C = new Import1C(getCtx());
			imp1C.importXMLDocument(result, documentToBeImported, get_TrxName(), p_param);

			addLog(0, null, null, Msg.getMsg(getCtx(), "Import1C") + "\n" + result.toString());
			result.setLength(0);
			result.append("Import from 1C");
			return result.toString();
		}
		
		public static void main(String[] args) 
		{
			CLogMgt.setLoggerLevel(Level.INFO, null);
			CLogMgt.setLevel(Level.INFO);
			
			Adempiere.startupEnvironment(false);
			ProcessInfo pi = new ProcessInfo("Test Import Model", 1000000);
			pi.setAD_Client_ID(100000);
			pi.setAD_User_ID(100);
			
			ImporterFrom1C importerFrom1C = new ImporterFrom1C();
			importerFrom1C.startProcess(Env.getCtx(), pi, null);
			
			System.out.println("Process=" + pi.getTitle() + " Error="+pi.isError() + " Summary=" + pi.getSummary());
		}

}

