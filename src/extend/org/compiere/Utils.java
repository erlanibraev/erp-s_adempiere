package extend.org.compiere;

import java.io.File;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Properties;

import jxl.Sheet;
import jxl.Workbook;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAttachment;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MProcess;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Language;
import org.compiere.util.Msg;
import org.compiere.utils.DigestOfFile;
import org.joda.time.DateTime;


public class Utils {
	
	/**	Logger							*/
	protected static CLogger			log = CLogger.getCLogger (Utils.class);
	
	/** */
	public static String localFilePath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
	
	public static MAttachmentEntry getAttachment(ProcessInfo pi, Properties m_ctx, String fileName){
		
		MProcess process = new MProcess(Env.getCtx(),  pi.getAD_Process_ID(), pi.getTransactionName());
		MAttachment attachment = process.getAttachment();
		
		if(attachment == null)
			throw new AdempiereException(Msg.translate(m_ctx, "AttachmentNotFound"));
		//
		MAttachmentEntry[] list = attachment.getEntries();
		if(list.length == 1)
			return list[0];
		//
		MAttachmentEntry entry = null;
		for(MAttachmentEntry a: attachment.getEntries()){
			if(fileName.isEmpty() || fileName == null) {
				entry = a;
				break;
			}else if (a.getName().substring(0, a.getName().indexOf(".")) .equals(fileName)) {
				entry = a;
				break;
			}
		}

		return entry;
	}
    
    /**
	 * Download db attachment to local file
	 * @param entry
	 * @return File
	 */
	public static File getAttachmentEntryFile(MAttachmentEntry entry) {
		String localFile = localFilePath + entry.getName();
		String downloadedLocalFile = localFilePath +"TMP" + entry.getName();
		File reportFile = new File(localFile);
		if (reportFile.exists()) {
			String localMD5hash = DigestOfFile.GetLocalMD5Hash(reportFile);
			String entryMD5hash = DigestOfFile.getMD5Hash(entry.getData());
			if (localMD5hash.equals(entryMD5hash))
			{
				log.info(" no need to download: local report is up-to-date");
			}
			else
			{
				log.info(" report on server is different that local one, download and replace");
				File downloadedFile = new File(downloadedLocalFile);
				entry.getFile(downloadedFile);
				if (! reportFile.delete()) {
					throw new AdempiereException("Cannot delete temporary file " + reportFile.toString());
				}
				if (! downloadedFile.renameTo(reportFile)) {
					throw new AdempiereException("Cannot rename temporary file " + downloadedFile.toString() + " to " + reportFile.toString());
				}
			}
		} else {
			entry.getFile(reportFile);
		}
		return reportFile;
	}
	
	/**
	 * The cell to start
	 * @param book
	 * @param delimiter
	 * @return
	 */
	public static ExcelCell getCellStart(Workbook book, String delimiter){
		
		ExcelCell cell = new ExcelCell();
		Sheet sheet =  book.getSheet(0);
		String s = "";
		
		for(int i=0; i < sheet.getColumns(); i++){
			for(int j=0; j < sheet.getRows(); j++){
				s = sheet.getCell(i, j).getContents().toLowerCase();
                if (s.isEmpty() || !s.equals(delimiter)) {
                    continue;
                }
                cell.setC(i);
                cell.setR(j);
                cell.setV(s);
                cell.setType("String");
			}
			
		}
		
		return cell;
		
	}
	
	private static String[] arrMonthNameRu = {"", "январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь"};
    private static String[] arrMonthNameRuCase = {"", "января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};
    private static String[] arrMonthNameEn = {"", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
    private static LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>();
    private static final String case_ = "_CASE";
    private static final String lanRu = Language.AD_Language_ru_RU;
    private static final String lanEn = Language.AD_Language_en_US;
    
    /**
     * method which helps manipulating the entered date and getting the name of month
     * @param dt Timestamp parameter, provides formatting and parsing the entered date
     * @param ln String of language, used to translate the month to required language 
     * @param c boolean parameter, used to check the grammatical properties of month's name 
     * @return String (name of required month)
     */
    public static String getMonthName(Timestamp dt, String ln, boolean c) {

        DateTime dd = new DateTime(dt.getTime());
        String key = ln;

        // map used for getting result by checking language and using appropriate array of String
        // case used for grammatical properties of month's name
        map.put(lanRu+case_, arrMonthNameRuCase);
        map.put(lanRu, arrMonthNameRu);
        map.put(lanEn, arrMonthNameEn);
        map.put(lanEn+case_, arrMonthNameEn);
        
        if (c) 
            key += case_;

        String[] arr = map.get(key);
        String result = arr[dd.getMonthOfYear()];

        return result;

    }
}
