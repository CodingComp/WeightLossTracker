import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.jfree.chart.ChartPanel;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class GUI {

	private JFrame ftFrame;
	private JTextField ftGDEntry;
	private JTextField srtWgtEntry;
	private JTextField ftGWEntry;
	
	private JFrame sFrame;
	private JTextField cGDEntry;
	private JTextField cGWEntry;
	JComboBox<String> cWOTimeComboBox;
	
	private static JFrame frame;
	
	private JLabel mcdDLable;
	private JLabel mcdHLabel;
	private JLabel mcdMLabel;
	private JLabel mcdSLabel;
	
	private JLabel dpLabel;
	private JLabel gwLabel;
	
	private JLabel tuwHLabel;
	private JLabel tuwMLabel;
	private JLabel tuwSLabel;
	
	private JLabel gdLabel;
	private JLabel bdLabel;
	
	private JLabel cwLabel;
	private JLabel twlLabel;
	private JLabel pdwLabel;
	private JLabel wugLabel;
	
	private static JPanel graphPanel;
	
	static Weight w;
	static CountdownTimer cdT;
	static SideGuiOptions sGO;
	static BottomGuiOptions bGO;

	static Preferences settings = Preferences.userRoot();
	
	/**
	 * Launch the application.
	 */
	static GUI window;
	private JTextField weightTxtField;
	public static void main(String[] args) {
		window = new GUI();
		window.frame.setVisible(true);
		
		if(settings.get("firstTime","user").equals("false")) {
			w = new Weight();
			cdT = new CountdownTimer();
			sGO = new SideGuiOptions();
			bGO = new BottomGuiOptions();
			
			cdT.calcTime();
			
			setGraph();
			
			updateGoodDays(String.valueOf(sGO.getGoodDays()));
			updateBadDays(String.valueOf(sGO.getBadDays()));
			
			updateCurrentWeight(Double.toString(w.getCurrentWeight()));
			updatePDW(bGO.getPDW(cdT.getDays(),w.getCurrentWeight()));
			updateTotalWeightLost(bGO.getTotalWeightLost(w.getCurrentWeight()));
			updateWeightUntilGoal(bGO.getWeightUntilGoal(w.getCurrentWeight()));
			
			updateGoalWeight();
		}
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	private void initialize() {
		/*
		 * Initializing Main Frame
		 */
		frame = new JFrame("Weight Loss Helper");
		frame.setResizable(false);
		frame.getContentPane().setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setForeground(Color.WHITE);
		frame.setBounds(100, 100, 1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		/*
		 * Initializing settings Frame
		 */
		sFrame = new JFrame("Settings");
		sFrame.setResizable(false);
		sFrame.getContentPane().setBackground(Color.DARK_GRAY);
		sFrame.getContentPane().setForeground(Color.WHITE);
		sFrame.setSize(600, 300);
		sFrame.getContentPane().setLayout(null);
		
		JLabel cGWLabel = new JLabel("Change Goal Weight: ");
		cGWLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		cGWLabel.setForeground(Color.WHITE);
		cGWLabel.setBounds(10, 11, 262, 49);
		sFrame.getContentPane().add(cGWLabel);
		
		JLabel cGDLabel = new JLabel("Change Goal Date: ");
		cGDLabel.setForeground(Color.WHITE);
		cGDLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		cGDLabel.setBounds(10, 71, 262, 49);
		sFrame.getContentPane().add(cGDLabel);
		
		JLabel cWOTLabel = new JLabel("Change Workout Time: ");
		cWOTLabel.setForeground(Color.WHITE);
		cWOTLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		cWOTLabel.setBounds(10, 131, 296, 49);
		sFrame.getContentPane().add(cWOTLabel);
		
		cGDEntry = new JTextField();
		cGDEntry.setText("MM/dd/yyyy");
		cGDEntry.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		cGDEntry.setBounds(298, 79, 275, 35);
		sFrame.getContentPane().add(cGDEntry);
		cGDEntry.setColumns(10);
		
		cGWEntry = new JTextField();
		cGWEntry.setText("0.0");
		cGWEntry.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		cGWEntry.setColumns(10);
		cGWEntry.setBounds(298, 19, 275, 35);
		sFrame.getContentPane().add(cGWEntry);
		
		cWOTimeComboBox = new JComboBox<String>();
		cWOTimeComboBox.setMaximumRowCount(10);
		cWOTimeComboBox.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		cWOTimeComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"None" ,"12:00 midnight\t", "1:00 am\t", "2:00 am\t", "3:00 am", "4:00 am\t", "5:00 am", 
				"6:00 am", "7:00 am\t", "8:00 am\t", "9:00 am", "10:00 am", "11:00 am", "12:00 pm", "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm", "5:00 pm", 
				"6:00 pm", "7:00 pm", "8:00 pm", "9:00 pm", "10:00 pm", "11:00 pm"}));
		cWOTimeComboBox.setBounds(298, 139, 275, 35);
		sFrame.getContentPane().add(cWOTimeComboBox);
		
		JButton changesBtn_1 = new JButton("Open");
		changesBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runtime rt = Runtime.getRuntime();
				Path fileP = Paths.get("weight.csv");
				String filePath = fileP.toAbsolutePath().toString();
				
				System.out.println(filePath);
				
				try {
					Process p = rt.exec("notepad " + filePath);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		changesBtn_1.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 14));
		changesBtn_1.setBackground(Color.WHITE);
		changesBtn_1.setBounds(185, 188, 110, 20);
		sFrame.getContentPane().add(changesBtn_1);
		
		JLabel openFileLbl = new JLabel("Open Weight File: ");
		openFileLbl.setForeground(Color.WHITE);
		openFileLbl.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 20));
		openFileLbl.setBounds(10, 172, 296, 49);
		sFrame.getContentPane().add(openFileLbl);
		
		
		JButton changesBtn = new JButton("Submit Changes");
		changesBtn.setBackground(Color.WHITE);
		changesBtn.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		changesBtn.setBounds(10, 215, 563, 37);
		changesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {;
				try {
					//Tests to see if the enterd date is a correct date
					Date testDate = new SimpleDateFormat("MM/dd/yyyy").parse(cGDEntry.getText());
					
					settings.put("gWeight", cGWEntry.getText());
					settings.put("gDate", cGDEntry.getText());
					switch(cWOTimeComboBox.getSelectedIndex()) {
						case 0:
							settings.put("woTime", "None");
							break;
						case 1:
							settings.put("woTime", "0");
							break;
						case 2:
							settings.put("woTime", "1");
							break;
						case 3:
							settings.put("woTime", "2");
							break;
						case 4:
							settings.put("woTime", "3");
							break;
						case 5:
							settings.put("woTime", "4");
							break;
						case 6:
							settings.put("woTime", "5");
							break;
						case 7:
							settings.put("woTime", "6");
							break;
						case 8:
							settings.put("woTime", "7");
							break;
						case 9:
							settings.put("woTime", "8");
							break;
						case 10:
							settings.put("woTime", "9");
							break;
						case 11:
							settings.put("woTime", "10");
							break;
						case 12:
							settings.put("woTime", "11");
							break;
						case 13:
							settings.put("woTime", "12");
							break;
						case 14:
							settings.put("woTime", "13");
							break;
						case 15:
							settings.put("woTime", "14");
							break;
						case 16:
							settings.put("woTime", "15");
							break;
						case 17:
							settings.put("woTime", "16");
							break;
						case 18:
							settings.put("woTime", "17");
							break;
						case 19:
							settings.put("woTime", "18");
							break;
						case 20:
							settings.put("woTime", "19");
							break;
						case 21:
							settings.put("woTime", "20");
							break;
						case 22:
							settings.put("woTime", "21");
							break;
						case 23:
							settings.put("woTime", "22");
							break;
						case 24:
							settings.put("woTime", "23");
							break;
					}

					try {
						settings.exportNode(new FileOutputStream("settings.xml"));
					} catch (IOException | BackingStoreException e1) {
						e1.printStackTrace();
					}
					
					bGO.updateGoalWeight();
					
					updateWeightUntilGoal(bGO.getWeightUntilGoal(w.getCurrentWeight()));
					setGraph();
					updateGoalWeight();
				}
				catch(NumberFormatException numEx ) {
					showPopUpError("One Of The Weight Fields Entered Is Incorrect");
				}
				catch(ParseException dateEx) {
					showPopUpError("The Date Field Is Incorrect \nmm/dd/yyyy (Month/Day/Year)");
				}
			}
		});
		sFrame.getContentPane().add(changesBtn);
		
		/*
		 * Initializing First Time Setup Frame
		 */
		
		ftFrame = new JFrame("Setup");
		ftFrame.setResizable(false);
		ftFrame.getContentPane().setBackground(Color.DARK_GRAY);
		ftFrame.getContentPane().setForeground(Color.WHITE);
		ftFrame.setSize(600, 350);
		ftFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ftFrame.getContentPane().setLayout(null);
		
		JLabel ftGWLbl = new JLabel("Goal Weight: ");
		ftGWLbl.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		ftGWLbl.setForeground(Color.WHITE);
		ftGWLbl.setBounds(10, 69, 262, 49);
		ftFrame.getContentPane().add(ftGWLbl);
		
		JLabel ftGDLbl = new JLabel("Goal Date: ");
		ftGDLbl.setForeground(Color.WHITE);
		ftGDLbl.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		ftGDLbl.setBounds(10, 129, 262, 49);
		ftFrame.getContentPane().add(ftGDLbl);
		
		JLabel ftWOTimeLbl = new JLabel("Workout Time: ");
		ftWOTimeLbl.setForeground(Color.WHITE);
		ftWOTimeLbl.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		ftWOTimeLbl.setBounds(10, 189, 296, 49);
		ftFrame.getContentPane().add(ftWOTimeLbl);
		
		ftGDEntry = new JTextField();
		ftGDEntry.setText("MM/dd/yyyy");
		ftGDEntry.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		ftGDEntry.setBounds(298, 137, 275, 35);
		ftFrame.getContentPane().add(ftGDEntry);
		ftGDEntry.setColumns(10);
		
		ftGWEntry = new JTextField();
		ftGWEntry.setText("0.0");
		ftGWEntry.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		ftGWEntry.setColumns(10);
		ftGWEntry.setBounds(298, 77, 275, 35);
		ftFrame.getContentPane().add(ftGWEntry);
		
		JLabel strWgtLbl = new JLabel("Starting Weight");
		strWgtLbl.setForeground(Color.WHITE);
		strWgtLbl.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 25));
		strWgtLbl.setBounds(10, 11, 262, 49);
		ftFrame.getContentPane().add(strWgtLbl);
		
		srtWgtEntry = new JTextField();
		srtWgtEntry.setText("0.0");
		srtWgtEntry.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		srtWgtEntry.setColumns(10);
		srtWgtEntry.setBounds(298, 19, 275, 35);
		ftFrame.getContentPane().add(srtWgtEntry);
		
		JComboBox<String> ftWOTimeCB = new JComboBox<String>();
		ftWOTimeCB.setMaximumRowCount(10);
		ftWOTimeCB.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		ftWOTimeCB.setModel(new DefaultComboBoxModel<String>(new String[] {"None" ,"12:00 midnight\t", "1:00 am\t", "2:00 am\t", "3:00 am", "4:00 am\t", "5:00 am", 
				"6:00 am", "7:00 am\t", "8:00 am\t", "9:00 am", "10:00 am", "11:00 am", "12:00 pm", "1:00 pm", "2:00 pm", "3:00 pm", "4:00 pm", "5:00 pm", 
				"6:00 pm", "7:00 pm", "8:00 pm", "9:00 pm", "10:00 pm", "11:00 pm"}));
		ftWOTimeCB.setBounds(298, 197, 275, 35);
		ftFrame.getContentPane().add(ftWOTimeCB);
		
		JButton ftSetupBtn = new JButton("Submit");
		ftSetupBtn.setBackground(Color.WHITE);
		ftSetupBtn.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		ftSetupBtn.setBounds(10, 262, 563, 37);
		ftSetupBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Tests to see if the enterd date is a correct date
					Date testDate = new SimpleDateFormat("MM/dd/yyyy").parse(ftGDEntry.getText());
					
					w = new Weight();
					
					settings.put("sWeight", srtWgtEntry.getText());
					settings.put("gWeight", ftGWEntry.getText());
					settings.put("gDate", ftGDEntry.getText());
					
					switch(ftWOTimeCB.getSelectedIndex()) {
						case 0:
							settings.put("woTime", "None");
							break;
						case 1:
							settings.put("woTime", "0");
							break;
						case 2:
							settings.put("woTime", "1");
							break;
						case 3:
							settings.put("woTime", "2");
							break;
						case 4:
							settings.put("woTime", "3");
							break;
						case 5:
							settings.put("woTime", "4");
							break;
						case 6:
							settings.put("woTime", "5");
							break;
						case 7:
							settings.put("woTime", "6");
							break;
						case 8:
							settings.put("woTime", "7");
							break;
						case 9:
							settings.put("woTime", "8");
							break;
						case 10:
							settings.put("woTime", "9");
							break;
						case 11:
							settings.put("woTime", "10");
							break;
						case 12:
							settings.put("woTime", "11");
							break;
						case 13:
							settings.put("woTime", "12");
							break;
						case 14:
							settings.put("woTime", "13");
							break;
						case 15:
							settings.put("woTime", "14");
							break;
						case 16:
							settings.put("woTime", "15");
							break;
						case 17:
							settings.put("woTime", "16");
							break;
						case 18:
							settings.put("woTime", "17");
							break;
						case 19:
							settings.put("woTime", "18");
							break;
						case 20:
							settings.put("woTime", "19");
							break;
						case 21:
							settings.put("woTime", "20");
							break;
						case 22:
							settings.put("woTime", "21");
							break;
						case 23:
							settings.put("woTime", "22");
							break;
						case 24:
							settings.put("woTime", "23");
							break;
					}
					
					cdT = new CountdownTimer();

					settings.put("sDate", cdT.getDate().toString());
					
					w.writeWeight(cdT.getDate(), Double.valueOf(settings.get("sWeight", "user")));

					sGO = new SideGuiOptions();
					bGO = new BottomGuiOptions();
					
					bGO.setCurrentWeight();
					bGO.updateGoalWeight();
					
					settings.put("firstTime", "false");
					settings.put("woCBIndex", String.valueOf(ftWOTimeCB.getSelectedIndex()));
					
					try {
						settings.exportNode(new FileOutputStream("settings.xml"));
					} catch (IOException | BackingStoreException e1) {
						e1.printStackTrace();
					}
					
					cdT.calcTime();
					
					setGraph();
					
					updateGoodDays(String.valueOf(sGO.getGoodDays()));
					updateBadDays(String.valueOf(sGO.getBadDays()));
					
					updateCurrentWeight(Double.toString(w.getCurrentWeight()));
					updatePDW(bGO.getPDW(cdT.getDays(),w.getCurrentWeight()));
					
					updateTotalWeightLost(bGO.getTotalWeightLost(w.getCurrentWeight()));
					updateWeightUntilGoal(bGO.getWeightUntilGoal(w.getCurrentWeight()));
					
					
					updateGoalWeight();
					
					ftFrame.setVisible(false);
				}
				catch(NumberFormatException numEx ) {
					showPopUpError("One Of The Weight Fields Entered Is Incorrect");
				}
				catch(ParseException dateEx) {
					showPopUpError("The Date Field Is Incorrect \nmm/dd/yyyy (Month/Day/Year)");
				}
			}
		});
		ftFrame.getContentPane().add(ftSetupBtn);
		
		/*
		 * MAIN COUNTDOWN ELEMENTS - START
		 */
		
		JLabel mcdSeconds = new JLabel("Seconds");
		mcdSeconds.setHorizontalAlignment(SwingConstants.CENTER);
		mcdSeconds.setForeground(Color.WHITE);
		mcdSeconds.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		mcdSeconds.setBounds(731, 530, 313, 41);
		frame.getContentPane().add(mcdSeconds);
		
		JLabel mcdHours = new JLabel("Hours");
		mcdHours.setHorizontalAlignment(SwingConstants.CENTER);
		mcdHours.setForeground(Color.WHITE);
		mcdHours.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		mcdHours.setBounds(30, 530, 285, 41);
		frame.getContentPane().add(mcdHours);
		
		JLabel mcdMinutes = new JLabel("Minutes");
		mcdMinutes.setHorizontalAlignment(SwingConstants.CENTER);
		mcdMinutes.setForeground(Color.WHITE);
		mcdMinutes.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		mcdMinutes.setBounds(406, 530, 269, 41);
		frame.getContentPane().add(mcdMinutes);
		
		JLabel mcdDaysLabel = new JLabel("Days");
		mcdDaysLabel.setForeground(Color.WHITE);
		mcdDaysLabel.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		mcdDaysLabel.setBounds(500, 330, 61, 41);
		frame.getContentPane().add(mcdDaysLabel);
		
		graphPanel = new JPanel();
		graphPanel.setBackground(Color.WHITE);
		graphPanel.setBounds(1065, 11, 829, 566);
		frame.getContentPane().add(graphPanel);
		graphPanel.setLayout(new BorderLayout(0, 0));
		

		mcdDLable = new JLabel("00\r\n");
		mcdDLable.setHorizontalAlignment(SwingConstants.CENTER);
		mcdDLable.setForeground(Color.WHITE);
		mcdDLable.setFont(new Font("Segoe UI Semibold", mcdDLable.getFont().getStyle(), 380));
		mcdDLable.setBounds(10, 0, 1039, 344);
		frame.getContentPane().add(mcdDLable);
		
		mcdHLabel = new JLabel("00");
		mcdHLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mcdHLabel.setForeground(Color.WHITE);
		mcdHLabel.setFont(new Font("Yu Gothic UI Semibold", mcdHLabel.getFont().getStyle(), 180));
		mcdHLabel.setBounds(10, 321, 312, 267);
		frame.getContentPane().add(mcdHLabel);
		
		JLabel mcdHMColon = new JLabel(":");
		mcdHMColon.setForeground(Color.WHITE);
		mcdHMColon.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 99));
		mcdHMColon.setBounds(325, 399, 22, 132);
		frame.getContentPane().add(mcdHMColon);
		
		JLabel mcdMSColon = new JLabel(":");
		mcdMSColon.setForeground(Color.WHITE);
		mcdMSColon.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 99));
		mcdMSColon.setBounds(685, 399, 22, 132);
		frame.getContentPane().add(mcdMSColon);
		
		mcdSLabel = new JLabel("00");
		mcdSLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mcdSLabel.setForeground(Color.WHITE);
		mcdSLabel.setFont(new Font("Yu Gothic UI Semibold", mcdHLabel.getFont().getStyle(), 180));
		mcdSLabel.setBounds(725, 321, 324, 267);
		frame.getContentPane().add(mcdSLabel);
		
		mcdMLabel = new JLabel("00");
		mcdMLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mcdMLabel.setForeground(Color.WHITE);
		mcdMLabel.setFont(new Font("Yu Gothic UI Semibold", mcdHLabel.getFont().getStyle(), 180));
		mcdMLabel.setBounds(10, 321, 1051, 267);
		frame.getContentPane().add(mcdMLabel);
		
		JLabel mainTimeDivider = new JLabel("_________________________________________________________________");
		mainTimeDivider.setHorizontalAlignment(SwingConstants.LEFT);
		mainTimeDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		mainTimeDivider.setForeground(Color.WHITE);
		mainTimeDivider.setBounds(10, 528, 1051, 60);
		frame.getContentPane().add(mainTimeDivider);
		
		
		/*
		 * MAIN COUNTDOWN ELEMENTS - END
		 */
		
		//============================================
		
		/*
		 * DAYS PASSED ELEMENTS - START
		 */
		
		
		JLabel dpDaysPassed = new JLabel("Days Passed");
		dpDaysPassed.setHorizontalAlignment(SwingConstants.LEFT);
		dpDaysPassed.setForeground(Color.WHITE);
		dpDaysPassed.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		dpDaysPassed.setBounds(1069, 581, 831, 41);
		frame.getContentPane().add(dpDaysPassed);
		
		JLabel dpDivider = new JLabel("________________");
		dpDivider.setHorizontalAlignment(SwingConstants.CENTER);
		dpDivider.setForeground(Color.WHITE);
		dpDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		dpDivider.setBounds(1055, 599, 280, 37);
		frame.getContentPane().add(dpDivider);
		
		dpLabel = new JLabel("0");
		dpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dpLabel.setForeground(Color.WHITE);
		dpLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		dpLabel.setBounds(1055, 634, 280, 96);
		frame.getContentPane().add(dpLabel);
		
		
		/*
		 * DAYS PASSED ELEMENTS - END
		 */
		
		//============================================
		
		/*
		 * GOOD DAYS ELEMENTS - START
		 */
		
		
		JLabel gdGoodDay = new JLabel("Good Days");
		gdGoodDay.setHorizontalAlignment(SwingConstants.LEFT);
		gdGoodDay.setForeground(Color.WHITE);
		gdGoodDay.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		gdGoodDay.setBounds(1344, 581, 556, 41);
		frame.getContentPane().add(gdGoodDay);
		
		JLabel gdDivider = new JLabel("________________");
		gdDivider.setHorizontalAlignment(SwingConstants.CENTER);
		gdDivider.setForeground(Color.WHITE);
		gdDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		gdDivider.setBounds(1340, 599, 270, 37);
		frame.getContentPane().add(gdDivider);
		
		gdLabel = new JLabel("0");
		gdLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gdLabel.setForeground(Color.WHITE);
		gdLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		gdLabel.setBounds(1330, 634, 280, 96);
		frame.getContentPane().add(gdLabel);
		
		
		/*
		 * GOOD DAYS ELEMENTS - END
		 */
		
		//============================================
		
		/*
		 * BAD DAYS ELEMENTS - START
		 */
		
		
		JLabel bdBadDays = new JLabel("Bad Days");
		bdBadDays.setHorizontalAlignment(SwingConstants.LEFT);
		bdBadDays.setForeground(Color.WHITE);
		bdBadDays.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		bdBadDays.setBounds(1634, 581, 266, 41);
		frame.getContentPane().add(bdBadDays);
		
		JLabel bdDivider = new JLabel("________________");
		bdDivider.setHorizontalAlignment(SwingConstants.CENTER);
		bdDivider.setForeground(Color.WHITE);
		bdDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		bdDivider.setBounds(1620, 599, 280, 37);
		frame.getContentPane().add(bdDivider);
		
		bdLabel = new JLabel("0");
		bdLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bdLabel.setForeground(Color.WHITE);
		bdLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		bdLabel.setBounds(1620, 629, 280, 96);
		frame.getContentPane().add(bdLabel);
		
		
		/*
		 * BAD DAYS ELEMENTS - END
		 */
		
		//============================================
		
		/*
		 * TIME UNTIL WORKOUT GUI ELEMENTS - START
		 */
		
		
		JLabel tuwTimeUntilWorkout = new JLabel("Time Until Workout");
		tuwTimeUntilWorkout.setHorizontalAlignment(SwingConstants.LEFT);
		tuwTimeUntilWorkout.setForeground(Color.WHITE);
		tuwTimeUntilWorkout.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		tuwTimeUntilWorkout.setBounds(1065, 735, 829, 41);
		frame.getContentPane().add(tuwTimeUntilWorkout);
		
		JLabel tuwDivider = new JLabel("___________________________________________________");
		tuwDivider.setHorizontalAlignment(SwingConstants.CENTER);
		tuwDivider.setForeground(Color.WHITE);
		tuwDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		tuwDivider.setBounds(1059, 752, 835, 37);
		frame.getContentPane().add(tuwDivider);
		
		tuwHLabel = new JLabel("00");
		tuwHLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tuwHLabel.setForeground(Color.WHITE);
		tuwHLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		tuwHLabel.setBounds(1265, 787, 130, 94);
		frame.getContentPane().add(tuwHLabel);
		
		JLabel tuwHMColon = new JLabel(":");
		tuwHMColon.setForeground(Color.WHITE);
		tuwHMColon.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 40));
		tuwHMColon.setBounds(1395, 802, 11, 67);
		frame.getContentPane().add(tuwHMColon);
		
		tuwMLabel = new JLabel("00");
		tuwMLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tuwMLabel.setForeground(Color.WHITE);
		tuwMLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		tuwMLabel.setBounds(1405, 788, 130, 94);
		frame.getContentPane().add(tuwMLabel);
		
		JLabel tuwMSColon = new JLabel(":");
		tuwMSColon.setForeground(Color.WHITE);
		tuwMSColon.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 40));
		tuwMSColon.setBounds(1535, 802, 11, 67);
		frame.getContentPane().add(tuwMSColon);
		
		tuwSLabel = new JLabel("00");
		tuwSLabel.setHorizontalAlignment(SwingConstants.CENTER);
		tuwSLabel.setForeground(Color.WHITE);
		tuwSLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		tuwSLabel.setBounds(1545, 788, 130, 94);
		frame.getContentPane().add(tuwSLabel);
		
		
		
		
		
		/*
		 * GRAPH GUI ELEMENTS - END
		 */
		
		//============================================
		
		/*
		 * ENTER WEIGHT GUI ELEMENTS - START
		 */
		
		
		weightTxtField = new JTextField();
		weightTxtField.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		weightTxtField.setBackground(Color.WHITE);
		weightTxtField.setBounds(10, 925, 1039, 43);
		frame.getContentPane().add(weightTxtField);
		weightTxtField.setColumns(10);
		
		JButton enterBtn = new JButton("Submit Weight");
		enterBtn.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		enterBtn.setBackground(Color.WHITE);
		enterBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					w.writeWeight(cdT.getDate(), Double.valueOf(weightTxtField.getText()));
					
					setGraph();
					
					updateGoodDays(String.valueOf(sGO.getGoodDays()));
					updateBadDays(String.valueOf(sGO.getBadDays()));

					updateCurrentWeight(Double.toString(w.getCurrentWeight()));
					updatePDW(bGO.getPDW(cdT.getDays(),w.getCurrentWeight()));
					updateTotalWeightLost(bGO.getTotalWeightLost(w.getCurrentWeight()));
					updateWeightUntilGoal(bGO.getWeightUntilGoal(w.getCurrentWeight()));
				} 
				catch (NumberFormatException e1) { showPopUpError("Input: (" + weightTxtField.getText() + ") Is Not A Valid Weight \nPlease Enter A Valid Weight"); }
			}
			
		});
		enterBtn.setBounds(10, 979, 729, 45);
		frame.getContentPane().add(enterBtn);
		
		JLabel cwCurrentWeight = new JLabel("Current Weight");
		cwCurrentWeight.setHorizontalAlignment(SwingConstants.LEFT);
		cwCurrentWeight.setForeground(Color.WHITE);
		cwCurrentWeight.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		cwCurrentWeight.setBounds(10, 584, 500, 41);
		frame.getContentPane().add(cwCurrentWeight);
		
		JLabel cwDivider = new JLabel("_______________________________");
		cwDivider.setHorizontalAlignment(SwingConstants.LEFT);
		cwDivider.setForeground(Color.WHITE);
		cwDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		cwDivider.setBounds(10, 599, 500, 37);
		frame.getContentPane().add(cwDivider);
		
		cwLabel = new JLabel("0.00 lbs");
		cwLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cwLabel.setForeground(Color.WHITE);
		cwLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		cwLabel.setBounds(20, 634, 500, 96);
		frame.getContentPane().add(cwLabel);
		
		twlLabel = new JLabel("0.00 lbs");
		twlLabel.setHorizontalAlignment(SwingConstants.CENTER);
		twlLabel.setForeground(Color.WHITE);
		twlLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		twlLabel.setBounds(544, 789, 505, 96);
		frame.getContentPane().add(twlLabel);
		
		JLabel twlDivider = new JLabel("_______________________________");
		twlDivider.setHorizontalAlignment(SwingConstants.LEFT);
		twlDivider.setForeground(Color.WHITE);
		twlDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		twlDivider.setBounds(544, 752, 505, 37);
		frame.getContentPane().add(twlDivider);
		
		JLabel twlTotalWeightLost = new JLabel("Total Weight Lost");
		twlTotalWeightLost.setHorizontalAlignment(SwingConstants.LEFT);
		twlTotalWeightLost.setForeground(Color.WHITE);
		twlTotalWeightLost.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		twlTotalWeightLost.setBounds(544, 737, 505, 41);
		frame.getContentPane().add(twlTotalWeightLost);
		
		pdwLabel = new JLabel("0.00 lbs");
		pdwLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pdwLabel.setForeground(Color.WHITE);
		pdwLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		pdwLabel.setBounds(20, 789, 500, 96);
		frame.getContentPane().add(pdwLabel);
		
		JLabel pdwDivider = new JLabel("_______________________________");
		pdwDivider.setHorizontalAlignment(SwingConstants.LEFT);
		pdwDivider.setForeground(Color.WHITE);
		pdwDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		pdwDivider.setBounds(10, 752, 500, 37);
		frame.getContentPane().add(pdwDivider);
		
		JLabel pdwPerDayWeightLoss = new JLabel("Per Day Weight Loss For Goal");
		pdwPerDayWeightLoss.setHorizontalAlignment(SwingConstants.LEFT);
		pdwPerDayWeightLoss.setForeground(Color.WHITE);
		pdwPerDayWeightLoss.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		pdwPerDayWeightLoss.setBounds(10, 737, 379, 41);
		frame.getContentPane().add(pdwPerDayWeightLoss);
		
		JLabel wugDivider = new JLabel("____________________________________________________");
		wugDivider.setHorizontalAlignment(SwingConstants.LEFT);
		wugDivider.setForeground(Color.WHITE);
		wugDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		wugDivider.setBounds(1059, 894, 841, 37);
		frame.getContentPane().add(wugDivider);
		
		wugLabel = new JLabel("00.0 Lbs");
		wugLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wugLabel.setForeground(Color.WHITE);
		wugLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		wugLabel.setBounds(1059, 931, 841, 79);
		frame.getContentPane().add(wugLabel);
		
		JLabel wugWeightUntilGoal = new JLabel("Weight Until Goal");
		wugWeightUntilGoal.setHorizontalAlignment(SwingConstants.LEFT);
		wugWeightUntilGoal.setForeground(Color.WHITE);
		wugWeightUntilGoal.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		wugWeightUntilGoal.setBounds(1059, 879, 841, 41);
		frame.getContentPane().add(wugWeightUntilGoal);
		
		JLabel gwDivider = new JLabel("_______________________________");
		gwDivider.setHorizontalAlignment(SwingConstants.LEFT);
		gwDivider.setForeground(Color.WHITE);
		gwDivider.setFont(new Font("Tahoma", Font.PLAIN, 30));
		gwDivider.setBounds(544, 599, 510, 37);
		frame.getContentPane().add(gwDivider);
		
		gwLabel = new JLabel("0.00 lbs");
		gwLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gwLabel.setForeground(Color.WHITE);
		gwLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 90));
		gwLabel.setBounds(554, 636, 500, 96);
		frame.getContentPane().add(gwLabel);
		
		JLabel gwGoalWeight = new JLabel("Goal Weight");
		gwGoalWeight.setHorizontalAlignment(SwingConstants.LEFT);
		gwGoalWeight.setForeground(Color.WHITE);
		gwGoalWeight.setFont(new Font("Segoe UI Light", Font.PLAIN, 30));
		gwGoalWeight.setBounds(544, 584, 510, 41);
		frame.getContentPane().add(gwGoalWeight);
		
		JButton settingsBtn = new JButton("Settings");
		settingsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsFrame();
			}
		});
		settingsBtn.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
		settingsBtn.setBackground(Color.WHITE);
		settingsBtn.setBounds(746, 979, 303, 45);
		frame.getContentPane().add(settingsBtn);
	}
	
	public void firstTimeSetup() {
		ftFrame.setVisible(true);
	}
	
	private void settingsFrame() {
		//	private JTextField cGDEntry;
		//private JTextField cGWEntry;
		//JComboBox<String> cWOTimeComboBox
		sFrame.setVisible(true);
		cGWEntry.setText(settings.get("gWeight", "user"));
		cGDEntry.setText(settings.get("gDate", "user"));
		cWOTimeComboBox.setSelectedIndex(Integer.parseInt(settings.get("woCBIndex", "user")));
	}
	
	public void showPopUpError(String msg) {
		JOptionPane.showMessageDialog(frame, msg);
	}
	
	public static void setGraph() {
		Graph chart = new Graph("Weight Chart" ,"", w);
		
		ChartPanel cP = chart.getChart();

		graphPanel.removeAll();
		graphPanel.add(cP, BorderLayout.CENTER);
	}
	
	public void updateCountdownTimer(int day, int hour, int min, int sec) {
		DecimalFormat df = new DecimalFormat("00");
		
		String dayF = df.format(day);
		String hourF = df.format(hour);
		String minF = df.format(min);
		String secF = df.format(sec);
		
		window.mcdDLable.setText(dayF);
		window.mcdHLabel.setText(hourF);
		window.mcdMLabel.setText(minF);
		window.mcdSLabel.setText(secF);
	}
	
	public void updateDaysPassed(String days) {
		window.dpLabel.setText(days);
	}
	
	public static void updateGoodDays(String goodDays) {
		window.gdLabel.setText(goodDays);
	}
	
	public static void updateBadDays(String badDays) {
		window.bdLabel.setText(badDays);
	}
	
	public void updateWorkoutTimer(int hour, int min, int sec) {
		DecimalFormat df = new DecimalFormat("00");
		
		String hourF = df.format(hour);
		String minF = df.format(min);
		String secF = df.format(sec);
		
		window.tuwHLabel.setText(hourF);
		window.tuwMLabel.setText(minF);
		window.tuwSLabel.setText(secF);
	}
	
	public static void updateCurrentWeight(String currentWeight) {
		window.cwLabel.setText(currentWeight + " lbs");
	}
	
	public static void updateGoalWeight() {
		window.gwLabel.setText(settings.get("gWeight", "user") + " lbs");
	}
	
	public static void updatePDW(Double perDayWeight) {
		DecimalFormat df = new DecimalFormat(".00");
		String pDWF = df.format(perDayWeight);
		
		window.pdwLabel.setText(pDWF + " lbs");
	}
	
	public static void updateTotalWeightLost(Double lostWeight) {
		DecimalFormat df = new DecimalFormat(".0");
		String lostWeightF = df.format(lostWeight);
		
		window.twlLabel.setText(lostWeightF + " lbs");
	}
	
	public static void updateWeightUntilGoal(Double weightLeft) {
		DecimalFormat df = new DecimalFormat(".00");
		String weightLeftF = df.format(weightLeft);
		
		window.wugLabel.setText(weightLeftF + " lbs");
	}
}
