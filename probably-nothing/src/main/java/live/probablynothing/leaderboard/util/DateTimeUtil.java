package live.probablynothing.leaderboard.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtil {

public static String getISO8601DateTimeInPST(String date) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		
		Date localDate = formatter.parse(date);
		
		OffsetDateTime offset = localDate.toInstant().atOffset(ZoneOffset.ofHours(-7));
		
		
		return offset.plusSeconds(1).toString();
	}
	
    public static String getISO8601DateTimeInPSTEnd(String date) throws ParseException {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
		formatter.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		
		Date localDate = formatter.parse(date);
		
		OffsetDateTime offset = localDate.toInstant().atOffset(ZoneOffset.ofHours(-7));
		
		
		return offset.plusHours(24).plusSeconds(1).toString();
	}
    
    public static boolean isDateFormatValid(String date) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd");
    	formatter.setLenient(false);
		
    	try {
    		formatter.parse(date);
		} catch (ParseException exc) {
			return false;
		}
    	
    	
    	return true;
    	
    	
    	
    }
}
