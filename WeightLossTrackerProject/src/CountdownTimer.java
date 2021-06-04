import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

public class CountdownTimer {
	
	public static int SECONDS_IN_A_DAY = 24 * 60 * 60;  
	
	private int days;
	private int hours;
	private int minutes;
	private int seconds;
	
	Preferences settings = Preferences.userRoot();
	
	//calculates the time until the goal date
	public void calcTime() {
		try {
			Date d = new SimpleDateFormat("MM/dd/yyyy").parse(settings.get("gDate", "user"));
			Calendar thatDay = Calendar.getInstance();
			thatDay.setTime(d);

			Calendar today = Calendar.getInstance();
		       
		    long diff =  thatDay.getTimeInMillis() - today.getTimeInMillis(); 
		    long diffSec = (diff - 18000000) / 1000;
		 
		    days = (int) (diffSec / SECONDS_IN_A_DAY);
		    int secondsDay = (int) (diffSec % SECONDS_IN_A_DAY);
		    seconds = secondsDay % 60;
		    minutes = (secondsDay / 60) % 60;
		    hours = (int) (secondsDay / 3600); 
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
		
	public int getDays() {
		return days;
	} 
	
	public int getHours() {
		return hours;
	}
	
	public int getMins() {
		return minutes;
	}
	
	public int getSecs() {
		return seconds;
	}	
	
	public String getDate() {
		 Calendar date = Calendar.getInstance();
		 
		 SimpleDateFormat sdF = new SimpleDateFormat("MM/dd/yyyy");
		 String fDate = sdF.format(date.getTime());
		 
		 return fDate;
	}
		
}
