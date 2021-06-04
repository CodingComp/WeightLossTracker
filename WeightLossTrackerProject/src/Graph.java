import org.jfree.chart.ChartPanel;

import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;

import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import edu.princeton.cs.algs4.ST;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Graph {

	Weight w = new Weight();
	
	ChartPanel chartPanel;
	
	Preferences settings = Preferences.userRoot();
	
   	public Graph( String applicationTitle , String chartTitle , Weight weight) {
		JFreeChart lineChart = ChartFactory.createLineChart(
	         chartTitle,
	         "","",
	         createDataset(),
	         PlotOrientation.VERTICAL,
	         false,false,false);
         
      	chartPanel = new ChartPanel(lineChart);
      	CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
      	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
      	
      	//update from the starting weight and goal weight
      	rangeAxis.setRange(Double.parseDouble(settings.get("gWeight", "user")), 
      			Double.parseDouble(settings.get("sWeight", "user")));
      
      	chartPanel.setPreferredSize(new Dimension(500 , 500));
      	chartPanel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 100));
      	
      	chartPanel.setPreferredSize(new Dimension(560 , 367));
      	
      	chartPanel.setRangeZoomable(false);
      	chartPanel.setDomainZoomable(false);
	}

	private DefaultCategoryDataset createDataset() {
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	      
	      ST<String, Double> wTable = w.getSymbolTable();   
	      
	      String[] dataDateArr = new String[7];
	      Double[] dataWeightArr = new Double[7];
	      
	      try {
	    	  String indexDate = wTable.max();
	    	  for(int i = 0; i < 7; i++) {
	              SimpleDateFormat dFormat = new SimpleDateFormat("MM/dd/yyy");
	              
	        	  Date cDate = dFormat.parse(indexDate);
	        	  Calendar cal = Calendar.getInstance();
	        	  cal.setTime(cDate);
	        	  
	        	  indexDate = dFormat.format(cal.getTime());
	        	  
	        	  String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	        	  String dayOfWeek = "";
	        	  
	        	  switch(cal.get(Calendar.DAY_OF_WEEK)) {
		        	  case 1 :
		        		  dayOfWeek = "Sun";
		        		  break;
		        	  case 2 :
		        		  dayOfWeek = "Mon";
			        	  break;
		        	  case 3 :
		        		  dayOfWeek = "Tue";
			        	  break;
		        	  case 4 :
		        		  dayOfWeek = "Wen";
			        	  break;
		        	  case 5 :
		        		  dayOfWeek = "Thu";
			        	  break;
		        	  case 6 :
		        		  dayOfWeek = "Fri";
			        	  break;
		        	  case 7 :
		        		  dayOfWeek = "Sat";
			        	  break;
	        	  }
	        	  
	        	  dataDateArr[i] = (dayOfWeek + "-" + day);
	        	  dataWeightArr[i] = wTable.get(indexDate);
	        	  
	        	  cal.add(Calendar.DAY_OF_MONTH, -1);
	        	  indexDate = dFormat.format(cal.getTime());
	    	  }
	
	      } catch (ParseException e) {
			e.printStackTrace();
	      }
	      
	      for(int i = 6; i >= 0; i--) {
	    	  dataset.addValue( dataWeightArr[i]  , "weight" , dataDateArr[i]);
	      }
	      
	      return dataset;
   }

	public ChartPanel getChart() {
		return chartPanel;
	}
	
}