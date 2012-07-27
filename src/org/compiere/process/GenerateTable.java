package org.compiere.process;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;

import jxl.Workbook;
import jxl.write.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Number;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MAttachmentEntry;
import org.compiere.model.MBPartner;
import org.compiere.model.Merpstimesheet;
import org.compiere.model.X_C_BPartner;
import org.eevolution.model.X_HR_Employee;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.model.X_HR_Department;
import org.eevolution.model.X_HR_Job;

import extend.org.compiere.ExcelCell;
import extend.org.compiere.ParamSchedule;
import extend.org.compiere.PartnerSchedule;
import extend.org.compiere.Utils;

public class GenerateTable extends SvrProcess {
	
	/** Current context					*/
	private Properties m_ctx;
	/** */
	private Timestamp dateFrom;
	/** */
	private String path;
	/** */
	private BigDecimal departmentID;
	/** */
	private BigDecimal bpartnerID;
	/**	 */
	private ProcessInfo pi;
	/**	Employee List	*/
	private MBPartner[] m_lines;
	
	@Override
	protected void prepare() {
		
		log.info("");
		m_ctx = Env.getCtx();
		pi = getProcessInfo();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (name.toLowerCase().equals("datefrom"))
				dateFrom = (Timestamp) para[i].getParameter();
			else if (name.toLowerCase().endsWith("directory"))
				path = (String)para[i].getParameter();
			else if (name.equals("HR_Department_ID"))
				departmentID = (BigDecimal)para[i].getParameter();
			else if (name.equals("C_BPartner_ID"))
				bpartnerID = (BigDecimal)para[i].getParameter();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

	}

	@Override
	protected String doIt() throws Exception {
		
		// Types of cells for
		Label label;
		Number number;
		WritableCellFormat cellStyle;
		org.joda.time.DateTime dt = new org.joda.time.DateTime(dateFrom.getTime());
		// There are checks for a specified pattern
		String templateName = "adempiere";
		
		//
		MAttachmentEntry entry = Utils.getAttachment(pi, m_ctx, templateName);
		if(entry == null)
			throw new AdempiereException(Msg.translate(m_ctx, "NotFoundTemplate")+" "+templateName);
		// Determine the number of days in the interval of dates
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
		if(dt.getDayOfMonth() <= 1)
			calendar.setTime(dt.plusDays(5).toDate());
		else
			calendar.setTime(dt.toDate());
		int lastDate = calendar.getActualMaximum(Calendar.DATE); // number of days
		int firstDate = calendar.getActualMinimum(Calendar.DATE);
		
		// We define a path to generate
		String fileExtension = entry.getName().substring(entry.getName().lastIndexOf("."),entry.getName().length());
		StringBuffer fullPath = new StringBuffer(path); 
		fullPath.append("\\Table_").append(dt.getMonthOfYear()).append(".").append(dt.getYear()).append(fileExtension); 
		// 
        File templateCopy = new File(fullPath.toString());
		Workbook tableBook = Workbook.getWorkbook(entry.getFile());
		WritableWorkbook copy = Workbook.createWorkbook(templateCopy, tableBook);
		WritableSheet sheet = null;
        try {
            sheet = copy.getSheet(0);
        } catch (Exception e) {
            throw new AdempiereException("Error tableBook. Page not found. " + e.toString());
        }

       ExcelCell cellStart =  Utils.getCellStart(tableBook,">>");
       // Standart style cell
       WritableCellFormat borderStyle = new WritableCellFormat();
       borderStyle.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
       borderStyle.setAlignment(Alignment.CENTRE);
       borderStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
       borderStyle.setWrap(true);
       // Date style cell
       DateFormat customDateFormat = new DateFormat ("dd MMM yyyy"); 
       WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat); 
       dateFormat.setAlignment(Alignment.CENTRE);
       dateFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
       // Holiday style cell
       WritableCellFormat cellHoliday = new WritableCellFormat();
       cellHoliday.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
       cellHoliday.setAlignment(Alignment.CENTRE);
       cellHoliday.setVerticalAlignment(VerticalAlignment.CENTRE);
       cellHoliday.setBackground(Colour.GRAY_25);
       // Deprtment style cell
       WritableFont fontNormal = new WritableFont (WritableFont.TIMES, 12, WritableFont.BOLD);
       WritableCellFormat cellDeprtment = new WritableCellFormat(fontNormal);
       cellDeprtment.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
       cellDeprtment.setAlignment(Alignment.CENTRE);
       cellDeprtment.setVerticalAlignment(VerticalAlignment.CENTRE);
       // Title style cell
       WritableFont fontTitle = new WritableFont (WritableFont.TIMES, 14, WritableFont.BOLD);
       WritableCellFormat titleStyle = new WritableCellFormat(fontTitle);
       titleStyle.setAlignment(Alignment.CENTRE);
       titleStyle.setVerticalAlignment(VerticalAlignment.CENTRE);
       Object[] param = new Object[]{Utils.getMonthName(new Timestamp(dt.getMillis()), Env.getAD_Language(getCtx()), false), Integer.toString(dt.getYear())};
       label = new Label(1, 4, sheet.getCell(1, 4).getContents()+MessageFormat.format(" за {0} месяц {1} года", param), titleStyle);
       sheet.addCell(label);
       
       //
       sheet.setColumnView(4, 35);
       sheet.setColumnView(4, 30);
       
       //
       X_C_BPartner bp = new X_C_BPartner(getCtx(), 0, null);
       X_HR_Employee he = null;
       X_HR_Department department = null;
       m_lines = bp.getPartnerEmployee(bpartnerID, departmentID);
       int col = cellStart.getC()+4; // offset by the number of first columns
       int row = cellStart.getR();
	   int n = 0;
	   int num = 1;
	   int dep = 0;
	   int dId = 0; // Departmnet ID
       for(int j = row; j < (row+m_lines.length); j++){
    	   
    	   // Title
	       he = X_HR_Employee.getHR_Employee(m_lines[n].getC_BPartner_ID()); 
	       X_HR_Job job = new X_HR_Job(getCtx(), he.getHR_Job_ID(), null);
	       department = new X_HR_Department(getCtx(), he.getHR_Department_ID(), null);
	       if(dId != he.getHR_Department_ID()){
	    	   sheet.insertRow(j+dep);
	    	   sheet.mergeCells(1, j+dep, 37, j+dep);
	    	   // Department
	    	   label = new Label(1, j+dep, department.getName(), cellDeprtment); 
		       sheet.addCell(label);
		       department = null;
	    	   dId = he.getHR_Department_ID();
	    	   dep++;
	       }
    	  
	       //
    	   sheet.setRowView(j+dep, 700);
    	   // №
		   number = new Number(1, j+dep, num, borderStyle); 
	       sheet.addCell(number);
	       // Table number
	       number = new Number(2, j+dep, m_lines[n].geterps_tableNumber(), borderStyle); 
	       sheet.addCell(number);
	       // Initials
		   label = new Label(3, j+dep, m_lines[n].getLastName()+" "+m_lines[n].getFirstName()+" "+m_lines[n].getMiddleName(), borderStyle); 
	       sheet.addCell(label);
	       // Job
		   label = new Label(4, j+dep, job.getName(), borderStyle); 
	       sheet.addCell(label);
	       job = null;
	       // 
	       if(dt.getDayOfMonth() <= 1)
				calendar.setTime(dt.plusDays(5).toDate());
			else
				calendar.setTime(dt.toDate());
	       calendar.set(Calendar.DAY_OF_MONTH, firstDate-1);
	       int hour = 0;
	       int day = 0;
	       int ch = 1;
    	   for(int i = col; i <= (31+col-1); i++){
    		   if(ch <= lastDate){
	    		   
	    		   ParamSchedule p = PartnerSchedule.getParam(m_lines[n].getC_BPartner_ID(), new Timestamp(calendar.getTime().getTime()));
	    		   //
	    		   if(p.getCode().equals(PartnerSchedule.holidayCode) 
	    				   || p.getCode().equals(PartnerSchedule.dayoffCode))
    				   cellStyle = cellHoliday;
    			   else
    				   cellStyle = borderStyle;

	    		   // is not Number
	    		   if(!p.getCode().matches("[0-9]*")){
	    			   label = new Label(i, j+dep, p.getCode(), cellStyle); 
	    			   sheet.addCell(label);
	    		   }
	    		   else{
	    			   number = new Number(i, j+dep, Integer.parseInt(p.getCode()), cellStyle); 
	    			   sheet.addCell(number);
	    		   }

	    	       hour += p.getHour();
	    	       day += p.getDay();
	    	       
	    	       // insert or update Timesheet for the payroll 
	    	       Merpstimesheet timeSheet = Merpstimesheet.getTimeSheet(m_lines[n].getC_BPartner_ID(), new Timestamp(calendar.getTime().getTime()));
	    	       timeSheet.setC_BPartner_ID(m_lines[n].getC_BPartner_ID());
	    	       timeSheet.setDateFrom(new Timestamp(calendar.getTime().getTime()));
	    	       timeSheet.seterps_hourQuota(p.getHour());
	    	       timeSheet.seterps_scheduleRule_ID(p.getRuleId());
	    	       timeSheet.saveEx();

	    	       p = null;
    		   }else{
    			   label = new Label(i, j+dep, "-", borderStyle); 
	    	       sheet.addCell(label);
    		   }
    		   calendar.add(Calendar.DAY_OF_MONTH, +1);
    	       ch++;
    	   }
    	   number = new Number(36, j+dep, hour, borderStyle); 
	       sheet.addCell(number);
	       number = new Number(37, j+dep, day, borderStyle); 
	       sheet.addCell(number);
	       
	       // shift the basement
	       sheet.insertRow(j+dep+1);
	       
	       n++;
	       num++;
       }
       
       // completion
       copy.write();
       copy.close();
       tableBook.close();
		
       return Msg.translate(m_ctx, "Success");
	}
	

}
