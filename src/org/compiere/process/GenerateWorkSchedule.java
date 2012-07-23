package org.compiere.process;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;

import org.compiere.model.Merpsholidays;
import org.compiere.model.MerpssampleSchedule;
import org.compiere.model.MerpssampleScheduleLine;
import org.compiere.model.MerpsscheduleRule;
import org.compiere.model.MerpsworkSchedule;
import org.compiere.model.MerpsworkScheduleSettings;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.joda.time.DateTime;


/**
 * The generation of working hours, according to the rules
 * @author V.Sokolov
 *
 */
public class GenerateWorkSchedule extends SvrProcess {
	
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (GenerateWorkSchedule.class);
	/** */
	private int days = 0;
	Timestamp dStart;
	Timestamp dEnd;
	/** */
	private MerpsworkScheduleSettings sWorkSetting;
	/** */
	private MerpsscheduleRule sRule;
	/** */
	private MerpssampleSchedule sSample;
	/** */
	private MerpssampleScheduleLine[] s_lines;
	
	@Override
	protected void prepare() {
		
		//
		sWorkSetting = new MerpsworkScheduleSettings(getCtx(), getRecord_ID(), null);
		if(sWorkSetting == null){
			s_log.log(Level.SEVERE, "Not created a model Mhrmworkshedulesettings...");
			return;
		}
		
		dStart = sWorkSetting.getStartDate();
		dEnd = sWorkSetting.getEndDate();
		// number of days between two dates
		days = getDays(dStart, dEnd);
		// Rule generate
		sRule = new MerpsscheduleRule(getCtx(), sWorkSetting.geterps_scheduleRule_ID(), null);
		// Pattern generation
		sSample = new MerpssampleSchedule(getCtx(), sRule.geterps_sampleSchedule_ID(), null);
		// We define the position of the start
		s_lines = sSample.getLinesSample();
		
	}

	@Override
	protected String doIt() throws Exception {
		
		Merpsholidays holiday = null;
		
		int position = getPosition(sRule.getDateFrom(), dStart, s_lines.length, sRule.geterps_dayNumber());
		// Set the start date of generation
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
		calendar.setTimeInMillis(dStart.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		
		--position;
		for(int i = 0; i <= days; i++){
			
			if((position % s_lines.length) == 0 )
				position = 0;
			
			calendar.add(Calendar.DAY_OF_MONTH, +1);
			MerpsworkSchedule sWork = new MerpsworkSchedule(getCtx(), 0, get_TrxName());
			sWork.setAD_Org_ID(Env.getAD_Org_ID(getCtx()));
			sWork.seterps_workScheduleSettings_ID(getRecord_ID());
			sWork.setIsActive(true);
			sWork.seterps_scheduleRule_ID(sWorkSetting.geterps_scheduleRule_ID());
			sWork.setDateFrom(new Timestamp(calendar.getTime().getTime()));
			sWork.seterps_daySchedule_ID(s_lines[position].geterps_daySchedule_ID());
			
			int hol = getHolidayDayoff(new Timestamp(calendar.getTime().getTime()));
			if(hol != 0)
				holiday = new Merpsholidays(getCtx(), hol, null);
			if(holiday != null){
				sWork.seterps_holiday(holiday.iserps_holiday());
				sWork.seterps_dayoff(holiday.iserps_dayoff());
				sWork.seterps_holidays_ID(holiday.geterps_holidays_ID());
				holiday = null;
			}
			
			sWork.saveEx();
			
			position++;
		}
		
		//
		sWorkSetting.setProcessed(true);
		sWorkSetting.saveEx();
		
		return "Generated over";
	}
	
	/**
	 * On what day to start start
	 */
	private int getPosition(Timestamp startDate, Timestamp currentDate, int length, int position){
		
		int result = 0;
		
		int days = (int) ((currentDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000));
		int mod = (days % length);
		result = mod + position;
		
		if(result > length){
			result = result - length;
		}
		
		return result;
		
	}
	
	/**
	 * The number of days over a period of dates
	 * @param dbeg
	 * @param dend
	 * @return
	 */
	private int getDays(Timestamp dbeg, Timestamp dend){
		return (int) ((dend.getTime() - dbeg.getTime()) / (24 * 60 * 60 * 1000));
	}

	/**
	 * We define the identifier ID of the holiday or weekend
	 */
	private int getHolidayDayoff(Timestamp timestamp){
		
		DateTime dt = new DateTime(timestamp.getTime());
		int holidayID = 0;
		
		StringBuffer sql_ = new StringBuffer("select erps_holidays_id from erps_holidays_v where ");
		sql_.append(" ? = month and ");
		sql_.append("(").append(dt.getDayOfMonth()).append("-sdate <= countDays) and ");
		sql_.append("(").append(dt.getDayOfMonth()).append("-sdate >= 0) and ");
		sql_.append(" ? between startdate and enddate");
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql_.toString(), null);
			pstmt.setInt(1, dt.getMonthOfYear());
			pstmt.setDate(2, new Date(timestamp.getTime()));
			rs = pstmt.executeQuery();
			if (rs.next())
				holidayID = rs.getInt(1);
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, "product", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		return holidayID;
	}
	
}
