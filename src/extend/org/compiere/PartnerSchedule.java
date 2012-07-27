package extend.org.compiere;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.Merpsattendancelist;
import org.compiere.model.MerpsdaySchedule;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

public class PartnerSchedule extends Named {
	
	public static final String dayoffCode = "Вх"; 			// FREE	
	public static final String vacationCode = "О";			// VACATION
	public static final String withoutpayCode = "Б/С";		// WITHOUT PAY
	public static final String diseaseCode = "Б";			// DISEASE
	public static final String businessTripCode = "К";		// BusinessTrip
	public static final String educationTripCode = "У";		// EducationTrip
	public static final String undocumentedCode = "НН";		// Undocumented
	public static final String quickvacationCode = "?";		// Quick vacation
	public static final String maternityleaveCode = "Р";	// Maternity leave
	public static final String truancyCode = "П";			// Truancy
	public static final String studyleaveCode = "УД";		// Study leave
	public static final String holidayCode = "ПР";			// Holiday	

	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (PartnerSchedule.class);
	
	public static ParamSchedule getParam(int BPartner_ID, Timestamp date){
		
		int workOneDay = 0;
		int absence  = 0;
		int absenceType  = 0;
		int attendance  = 0;
		int attendanceType  = 0;
		int shiftOneDay  = 0;
		int holiday = 0;
		int ruleId = 0;

		//
		ParamSchedule param = new ParamSchedule();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// Employee data from the card
				String sql_ = "SELECT * FROM ERPS_SCHEDULESETTINGS_V WHERE AD_CLIENT_ID=? " +
						"AND C_BPARTNER_ID=? " +
						"AND DATEFROM = ?";
		try
		{
			pstmt = DB.prepareStatement(sql_,null);
			pstmt.setInt(1, Env.getAD_Client_ID(Env.getCtx()));
			pstmt.setInt(2, BPartner_ID);
			pstmt.setDate(3, new Date(date.getTime()));
			rs = pstmt.executeQuery();
			if(rs.next()){
				workOneDay = rs.getInt("workschedule_onedaytype_id");
				absence = rs.getInt("absence_id");
				absenceType = rs.getInt("absencetype");
				attendance = rs.getInt("attendance_id");
				attendanceType = rs.getInt("attendancetype");
				shiftOneDay = rs.getInt("shiftschedule_onedaytype_id");
				holiday = rs.getInt("holidays_id");
				ruleId = rs.getInt("shedulerule_id");
			}
				
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, "product", e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}	
		
		// shedule rule ID
		param.setRuleId(ruleId);
		
		MerpsdaySchedule daysheduleOriginal = new MerpsdaySchedule(Env.getCtx(), workOneDay, null);
		if(daysheduleOriginal.geterps_hourQuota().intValue() > 0){
			param.setDayOriginal(1);
			param.setHourOriginal(daysheduleOriginal.geterps_hourQuota().intValue());
		}
		
		MerpsdaySchedule dayshedule = null;
		
		// Shift schedule
		if(shiftOneDay !=0)
			dayshedule = new MerpsdaySchedule(Env.getCtx(), shiftOneDay, null);
		else
			dayshedule = daysheduleOriginal; // Work schedule
		
		String code = "";
		if(dayshedule.geterps_hourQuota().intValue() > 0){
			code = Integer.toString(dayshedule.geterps_hourQuota().intValue());
			param.setDay(1);
		}
		else
			code = dayoffCode;
		
		param.setHour(dayshedule.geterps_hourQuota().intValue());
		param.setCode(code);
		
		// holidays
		if(holiday != 0){
			//
			param.setDayOriginal(0); param.setHourOriginal(0);
			//
			param.setDay(0); param.setHour(0);
			param.setCode(holidayCode);
		}
		
		// Absence
		if(absence != 0){
			
			param.setDay(0);
			param.setHour(0);
			
			if(absenceType == 1)
				param.setCode(vacationCode);
			else if(absenceType == 2)
				param.setCode(withoutpayCode);
			else if(absenceType == 3)
				param.setCode(studyleaveCode);
			else if(absenceType == 4)
				param.setCode(quickvacationCode);
			else if(absenceType == 5)
				param.setCode(undocumentedCode);
			else if(absenceType == 6)
				param.setCode(diseaseCode);
			else if(absenceType == 7)
				param.setCode(dayoffCode);
			else if(absenceType == 8)
				param.setCode(truancyCode);
			else if(absenceType == 9){
				if(dayshedule.geterps_hourQuota().intValue() > 0){
					param.setCode(businessTripCode);
					param.setDay(1);
					param.setHour(dayshedule.geterps_hourQuota().intValue());
				}
			}
			else if(absenceType == 10)
				param.setCode(educationTripCode);
			else if(absenceType == 13)
				param.setCode(maternityleaveCode);
		}

		// Attendance
		if(attendance !=0){
			
			Merpsattendancelist at = new Merpsattendancelist(Env.getCtx(), attendance, null);
			// overtime
			if(attendanceType == 1){
				param.setDay(1); 
				param.setHour(dayshedule.geterps_hourQuota().intValue()+at.geterps_hourQuota().intValue());
				code = Integer.toString(dayshedule.geterps_hourQuota().intValue())+"/"+Integer.toString(at.geterps_hourQuota().intValue());
				param.setCode(code);
			}
			// over the weekend
			else if(attendanceType == 2){
				param.setDay(1); 
				param.setHour(at.geterps_hourQuota().intValue());
				param.setCode(Integer.toString(at.geterps_hourQuota().intValue()));
			}
		}
				
		
		return param;
	}
	
}
