package org.erps;

import org.compiere.util.DB;
import org.compiere.util.Env;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ErpsCreateSwift {
	public static int createSwift(Properties ctx){

		try{
			final JFileChooser fc = new JFileChooser();
			JList list = new JList();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			FileOutputStream fos = null;
			OutputStreamWriter out = null;
			
			if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(list)) {
				String FileName = fc.getSelectedFile().getAbsolutePath() + "/Swift.txt";
				File file = new File(FileName);//fc.getSelectedFile();
				fos = new FileOutputStream(file);
				out = new OutputStreamWriter(fos, "CP866");
				
				// lookup prefix
				Properties pro = Env.getCtx();
				String prefix = null;
				for (Enumeration e = pro.keys(); e.hasMoreElements(); /**/) {//402
					int prefix_int = 0;
					String key = (String) e.nextElement();
					prefix_int = key.indexOf("_TabInfo_AD_Table_ID");
					if(prefix_int > 0){
						String key_value = pro.getProperty(key);
							if(key_value.equals("335")){ //335 = Платежное поручение
								prefix = key.substring(0, prefix_int);
								break;
							}
					}
				}

				
				String value = prefix + "C_Payment_ID";
				String C_Payment_ID = Env.getContext(ctx, value);
				
				String sql = 
						" select	" + 
						"	org.name as orgname " +
						"	,bp.name " +
						" 	,documentno " +
						"	,bp.rnn as rnn1 " +
						"	,(select name from c_bpartner where c_bpartner_id = cast(main.directorsignaturepayment as numeric)) director " +
						"	,(select name from c_bpartner where c_bpartner_id = cast(main.chiefaccontantsignaturepayment as numeric)) chiefacc " +
						" 	,main.payamt " +
						"   ,main.description " +
						"	,bp.resident " +
						" 	,(select eco_sector_code from erps_eco_sector_code where erps_eco_sector_code_id = bp.erps_eco_sector_code_id) sector " +
						"	,(select cod_naznach_plateja from erps_cod_naznach_plateja where erps_cod_naznach_plateja_id = main.erps_cod_naznach_plateja_id) kpn " +						
						"	,dateacct " +
						"	,(select iso_code from c_currency where c_currency_id = main.c_currency_id) currency " +
						"	,(select accountno from c_bankaccount where c_bankaccount_id = main.c_bankaccount_id) bankacc " +	
						"	,(select routingno from c_bank as b " +
						"			inner join c_bankaccount as ba on ba.c_bank_id = b.c_bank_id " +
						"			where c_bankaccount_id = main.c_bankaccount_id) routingno " +
						
						
						" from c_payment as main " +
						"	left join ad_org as org on org.ad_org_id = main.ad_org_id " +
						"	left join c_bpartner bp on  bp.c_bpartner_id = main.c_bpartner_id " +

						" where c_payment_id = ? ";//1000257 ";

				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				String orgName = null;
				String name = null;
				String documentNo = null;
				String rnn1 = null;
				String director = null;
				String chiefAcc = null;
				String payamt = null;
				String description = null;
				String resident2 = null;
				String sector2 = null;
				String kpn = null;
				String dateacct = null;
				String currency = null;
				String bankacc = null;
				String routingno = null;
				
					pstmt = DB.prepareStatement(sql, null);
					pstmt.setInt(1, Integer.parseInt(C_Payment_ID));

					rs = pstmt.executeQuery();
					if (rs.next())
					{
						orgName = rs.getString("orgname");
						name = rs.getString("name");
						documentNo = rs.getString("documentNo");
						rnn1 = rs.getString("rnn1");
						director = rs.getString("director");
						chiefAcc = rs.getString("chiefacc");
						payamt = rs.getString("payamt");
						description = rs.getString("description");
						resident2 = rs.getString("resident");						
						sector2 = rs.getString("sector");
						kpn = rs.getString("kpn");
						dateacct = rs.getString("dateacct");
						currency = rs.getString("currency");
						bankacc = rs.getString("bankacc");
						routingno = rs.getString("routingno");
					}

				//write file
				//static
				out.write("{ 1:F01K059260000000010228724}"); out.write("\r\n");
				out.write("{ 2:O102SGROSS00000000000000000106190951U}"); out.write("\r\n");
				out.write("{4:"); out.write("\r\n");
				out.write(":20:REFERENCE"); out.write("\r\n");
				
				//2011-12-30 00:00:00
				if(dateacct!=null) dateacct = dateacct.substring(2, 4) + dateacct.substring(5, 7) + dateacct.substring(8,10);  
				out.write(":32A:"+ dateacct + currency + payamt); out.write("\r\n");
				out.write(":50:/D/KZ839261501140044000"); out.write("\r\n");
				out.write("/NAME/TOO ERP -Service "+'"'+"KazTransCom"+'"'); out.write("\r\n");				
				out.write("/RNN/620300240882"); out.write("\r\n");
				
				out.write("/CHIEF/" + (director == null ? "" : director)); out.write("\r\n");
				out.write("/MAINBK/" + (chiefAcc == null ? "" : chiefAcc)); out.write("\r\n");
				out.write("/IRS/1"); out.write("\r\n");
				out.write("/SECO/7"); out.write("\r\n");		
				out.write(":52B:195301716"); out.write("\r\n");	//old BIK
				out.write(":57B:" + (routingno == null ? "" : routingno)); out.write("\r\n");	//new BIK
				out.write(":59:KZ299261802148808000"); out.write("\r\n");	//fix				
				out.write("/NAME/" + (name == null ? "" : name)); out.write("\r\n");
				out.write("/RNN/" + (rnn1 == null ? "" : rnn1)); out.write("\r\n");
				out.write("/IRS/" + (resident2.equals("Y") ? "1" : "2")); out.write("\r\n"); 
				out.write("/SECO/" + (sector2 == null ? "" : sector2)); out.write("\r\n");
				
				out.write(":70:/NUM/"+(documentNo == null ? "" : documentNo)); out.write("\r\n");	//fix
				out.write("/DATE/00000"); out.write("\r\n");
				out.write("/VO/01"); out.write("\r\n");	//fix
				out.write("/SEND/07"); out.write("\r\n");	//fix
				out.write("/KNP/" + (kpn == null ? "" : kpn)); out.write("\r\n");
				out.write("/ASSIGN/" + (description == null ? "" : description)); out.write("\r\n");
				
				out.write(""); out.write("\r\n");
				out.write("-}");
				out.flush();
				out.close();
				fos.close();
				//System.out.println(file);
			}
		}catch(Exception e){
			System.out.println(e.toString());
			return 1;
		}
//		finally{
//			out.close();
//			fos.close();
//		}
		return 0;
	}
}
