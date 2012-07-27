package extend.org.compiere;

public class ParamSchedule extends Named {

	private int hour = 0;    			// standard hours
	private int hourOriginal = 0;    	// original hours
	private int day = 0;	 			// if the working day = 1, otherwise = 0
	private int dayOriginal = 0;	 	// if the original working day = 1, otherwise = 0
	private String code = ""; 			// conditional code in report card
	private int ruleId = 0;				// shedule rule ID
	
	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public int getHourOriginal() {
		return hourOriginal;
	}

	public void setHourOriginal(int hourOriginal) {
		this.hourOriginal = hourOriginal;
	}

	public int getDayOriginal() {
		return dayOriginal;
	}

	public void setDayOriginal(int dayOriginal) {
		this.dayOriginal = dayOriginal;
	}
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}
	
	public void setHour(int hour) {
		this.hour = hour;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}


}
