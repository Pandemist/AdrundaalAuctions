package pandemist.adrundaal.auctions.utils;

import java.util.Calendar;

public class TimeUtils {
	/*
	*   Converts a long (time) to the String time format
	 */
	public static String convertToTime(long time) {
		Calendar C=Calendar.getInstance();
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(time);
		int total=((int) (cal.getTimeInMillis()/1000)-(int) (C.getTimeInMillis()/1000));
		int D=0;
		int H=0;
		int M=0;
		int S=0;
		for(; total>86400; total-=86400, D++) ;
		for(; total>3600; total-=3600, H++) ;
		for(; total>60; total-=60, M++) ;
		S+=total;
		return D+"d "+H+"h "+M+"m "+S+"s ";
	}
	/*
	*   Gets the actual Time
	 */
	public static long getNowTime() {
		Calendar cal=Calendar.getInstance();
		return cal.getTimeInMillis();
	}
	/*
	*   Converts a Time as a String to a long (time)
	 */
	public static long convertToMill(String time) {
		Calendar cal=Calendar.getInstance();
		for(String i : time.split(" ")) {
			if(i.contains("D")||i.contains("d")) {
				cal.add(Calendar.DATE, Integer.parseInt(i.replaceAll("D", "").replaceAll("d", "")));
			}
			if(i.contains("H")||i.contains("h")) {
				cal.add(Calendar.HOUR, Integer.parseInt(i.replaceAll("H", "").replaceAll("h", "")));
			}
			if(i.contains("M")||i.contains("m")) {
				cal.add(Calendar.MINUTE, Integer.parseInt(i.replaceAll("M", "").replaceAll("m", "")));
			}
			if(i.contains("S")||i.contains("s")) {
				cal.add(Calendar.SECOND, Integer.parseInt(i.replaceAll("S", "").replaceAll("s", "")));
			}
		}
		return cal.getTimeInMillis();
	}
}
