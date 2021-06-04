import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import edu.princeton.cs.algs4.ST;

public class Weight {
	
	Core c = new Core();
	
	Path fileP = Paths.get("weight.csv");
	String filePath = fileP.toAbsolutePath().toString();
	
	public ST<String,Double> weightTable = new ST<String,Double>();
	
	Double currentWeight;
	
	Preferences settings = Preferences.userRoot();
	
	public Weight() {
		if(settings.get("firstTime", "user").equals("false")) {
			//sees if there is a file at the given filePath
			try {
				BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
				String row;
				
				//puts data into a SymbolTable to store the data
				while((row = csvReader.readLine()) != null) {
					String[] fields = row.split(",");
					weightTable.put(fields[0], Double.valueOf(fields[1]));				
				}
				
				//sets the current weight to the weight last entered
				currentWeight = weightTable.get(weightTable.max());
				csvReader.close();
			}	
			catch(Exception e) { e.printStackTrace(); }
		}
	}
	
	public void writeWeight(String date, Double weight) {
		
		//sees if there is a file
		try {
			PrintWriter csvWriter = new PrintWriter(new FileWriter(filePath, true));
			
			//checks to see if the user already entered their weight for the day
			if(weightTable.contains(date)) {
				c.sendErrMsg("You Already Entered A Weight For Today.");
			}
			else {
				//writes the weight entered by the user to a CSV file
				if(settings.get("firstTime", "user").equals("false")) {
					csvWriter.write("\n" + date + "," + weight);
					weightTable.put(date, weight);
					currentWeight = weight;
				}
				else {
					csvWriter.write(date + "," + weight);
					weightTable.put(date, weight);
					currentWeight = weight;
				}
			}

			csvWriter.flush();
			csvWriter.close();

		}	
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public Double getCurrentWeight() {
		return currentWeight;
	}
	
	public ST<String, Double> getSymbolTable() {
		return weightTable;
	}
}
