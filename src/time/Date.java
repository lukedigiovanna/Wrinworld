package time;

import java.io.Serializable;

import misc.MathUtils;

public class Date implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int second, minute, hour, day, month, year = 1400; //default year
	
	public Date(int time) {
		addTime(time);
	}
	
	public void updateTimes() {
		minute+=(second/60);
		hour+=(minute/60);
		day+=(hour/24);
		month+=(day/30);
		year+=(month/12);
		second%=60;
		minute%=60;
		hour%=24;
		day%=30;
		month%=12;
	}
	
	public String getMonth() {
		switch (month) {
		case 0: return "January";
		case 1: return "February";
		case 2: return "March";
		case 3: return "April";
		case 4: return "May";
		case 5: return "June";
		case 6: return "July";
		case 7: return "August";
		case 8: return "September";
		case 9: return "October";
		case 10: return "November";
		case 11: return "December";
		}
		return "Unknown";
	}
	
	public int getYear() {
		return year;
	}
	
	public void addTime(int amount) {
		second+=amount;
		updateTimes();
	}
	
	public double lightvalue() {
		//7 is sun rise.. 19 is sunset.. 12 hour difference. peak day at 12 pm
		//need a periodic function with a period of 24 and starts at 7
		return (5/8.0)*Math.sin((Math.PI*(minute/60.0+hour-6))/12.0)+6.0/8;
	}
	
	public void setDay() {
		if (hour >= 20 || hour < 7) {
			minute+=(60-minute);
			updateTimes();
			if (hour < 7)
				hour+=(7-hour);
			else
				hour+=(7+24-hour);
			updateTimes();
		}
	}
	
	public String toString() {
		String s = "";
		s+=(day+1)+misc.MathUtils.getSuffix(day+1)+" "+getMonth()+", "+MathUtils.romanNumerals(year)+" ";
		int h = hour%12;
		if (h == 0)
			s+=12;
		else
			s+=h;
		if (minute < 10)
			s+=":0"+minute;
		else
			s+=":"+minute;
		if (hour < 12)
			s+=" AM";
		else
			s+=" PM";
		return s;
	}
}
