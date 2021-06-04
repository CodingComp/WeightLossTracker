import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.prefs.Preferences;
import java.util.stream.Stream;

public class SideGuiOptions {
	String filePath = "D:\\Coding Stuff\\Eclipse Workshop\\Now\\weight.csv";
	
	//VARIABLES FOR CALCULATING TIME UNTIL NEXT WORKOUT
	public static int SECONDS_IN_A_DAY = 24 * 60 * 60;  
	private int days;
	
	private int woHours;
	private int woMinutes;
	private int woSeconds;
	
	Preferences settings = Preferences.userRoot();
	
	//returns the days passed from the starting date
	public int getDaysPassed() {
		try {
			Date d = new SimpleDateFormat("MM/dd/yyyy").parse(settings.get("sDate", "user"));
			Calendar thatDay = Calendar.getInstance();
			thatDay.setTime(d);
			thatDay.set(Calendar.HOUR, -12);

			Calendar today = Calendar.getInstance();

			long diff =  today.getTimeInMillis() - thatDay.getTimeInMillis(); 
		    long diffSec = diff / 1000;
		 
		    days = (int) (diffSec / SECONDS_IN_A_DAY);
		    return days;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	//returns good days / days where weight was lost
	public int getGoodDays() {
		
		int goodDays = 0;

		try{
			BufferedReader bR = new BufferedReader(new FileReader(filePath));
			Stream<String> weightStream = bR.lines();
			Iterator<String> tI = weightStream.iterator();
			
			String currentLine = tI.next();
			while(tI.hasNext()) {
				String compareLine = "";
				
				try {
					compareLine = tI.next();
				}
				catch(NoSuchElementException e) {
					break;
				}

				String[] cFields = currentLine.split(",");
				String[] compFields = compareLine.split(",");
						
				if(Double.valueOf(cFields[1]) > Double.valueOf(compFields[1])) {
					goodDays += 1;
				}
				
				currentLine = compareLine;
			}
			
			bR.close();
		}	
		catch(Exception e) { e.printStackTrace(); }
	
		return goodDays;
	}
	
	//Returns the bad / days where weight was gained
	public int getBadDays() {
		
		int badDays = 0;

		try{
			BufferedReader bR = new BufferedReader(new FileReader(filePath));
			Stream<String> weightStream = bR.lines();
			Iterator<String> tI = weightStream.iterator();
			
			String currentLine = tI.next();
			while(tI.hasNext()) {
				String compareLine = "";
				
				try {
					compareLine = tI.next();
				}
				catch(NoSuchElementException e) {
					break;
				}

				String[] cFields = currentLine.split(",");
				String[] compFields = compareLine.split(",");
						
				if(Double.valueOf(cFields[1]) < Double.valueOf(compFields[1])) {
					badDays += 1;
				}
				
				currentLine = compareLine;
			}
			
			bR.close();
		}	
		catch(Exception e) { e.printStackTrace(); }
	
		return badDays;
	}
	
	//gets the time until the next workout
	public void calcTimeUntilWorkout() {
		if(!settings.get("woTime", "user").equals("None")) {
			Calendar woToday = Calendar.getInstance();
			Calendar woDate = Calendar.getInstance();
			
			//convert to hours
			woDate.add(Calendar.DAY_OF_MONTH, 1);
			woDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(settings.get("woTime", "user")));
			woDate.set(Calendar.MINUTE, 0);
			woDate.set(Calendar.SECOND, 0);
			
			
		    long diff =  woDate.getTimeInMillis() - woToday.getTimeInMillis(); 
		    long diffSec = (diff / 1000) - 1;
		    
		    days = (int) (diffSec / SECONDS_IN_A_DAY);
		    int secondsDay = (int) (diffSec % SECONDS_IN_A_DAY);
		    woSeconds = secondsDay % 60;
		    woMinutes = (secondsDay / 60) % 60;
		    woHours = (int) (secondsDay / 3600);
		}
	}
	
	public int getWOHours() {
		return woHours;
	}
	public int getWOMinutes() {
			return woMinutes;
	}
	public int getWOSeconds() {
		return woSeconds;
	}

}
