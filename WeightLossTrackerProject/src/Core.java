import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Core {

	static GUI gui = new GUI();
	static CountdownTimer cdT = new CountdownTimer();
	static SideGuiOptions sGO = new SideGuiOptions();
	
	public static void main(String[] args) {
		Preferences settings = Preferences.userRoot();

//		Test code for resetting the settings
//		settings.put("firstTime", "true");
//		settings.put("gWeight", "");
//		settings.put("gDate", "");
//		settings.put("woTime", "");
//		settings.put("sWeight", "");
//		settings.put("sDate", "");
//		settings.put("woCBIndex", "");
//		
//		try {
//			settings.exportNode(new FileOutputStream("settings.xml"));
//		} catch (IOException | BackingStoreException e1) {
//			e1.printStackTrace();
//		}
		
		gui.main(args);
		
		if(settings.get("firstTime", "user").equals("true")) {
			gui.firstTimeSetup();
			//Waits for the user to finish the initial setup
			while(settings.get("firstTime", "user").equals("true")) {}
		}
		
		start();

	}
	
	public static void start() {
		while(true) {
			try {
				cdT.calcTime();
				sGO.calcTimeUntilWorkout();
				
				gui.updateCountdownTimer(cdT.getDays(),cdT.getHours(),cdT.getMins(), cdT.getSecs());
				gui.updateWorkoutTimer(sGO.getWOHours(), sGO.getWOMinutes(), sGO.getWOSeconds());

				gui.updateDaysPassed(Integer.toString(sGO.getDaysPassed()));
			}
			catch(NullPointerException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendErrMsg(String msg) {
		gui.showPopUpError(msg);
	}
}
