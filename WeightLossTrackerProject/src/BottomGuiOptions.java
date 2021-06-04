import java.util.prefs.Preferences;

public class BottomGuiOptions {
	Double startingWeight = 0.0; //GET FROM SETTINGS LATER
	Double goalWeight = 0.0; //GET FROM SETTINGS LATER
	
	Preferences settings = Preferences.userRoot();

	public BottomGuiOptions() {
		if(settings.get("firstTime", "user").equals("false")) {
			startingWeight = Double.valueOf(settings.get("sWeight", "user"));
			goalWeight = Double.valueOf(settings.get("gWeight", "user"));
		}
	}

	//Per Day Weight Loss
	public Double getPDW(int daysLeft,Double currentWeight) {
		Double weightDifference = currentWeight - goalWeight;
		Double weightPerDay = weightDifference / daysLeft;
		
		return weightPerDay;
	}
	
	public Double getTotalWeightLost(Double currentWeight) {
		return startingWeight - currentWeight;
	}
	
	public Double getWeightUntilGoal(Double currentWeight) {
		return currentWeight - goalWeight;
	}
	
	public void setCurrentWeight() {
		startingWeight = Double.valueOf(settings.get("sWeight", "user"));
	}
	
	public void updateGoalWeight() {
		goalWeight = Double.valueOf(settings.get("gWeight", "user"));
	}
	
}
