/*********************************/
/* Author: Xingchen Wang         */
/* Email: xwang95@sheffield.ac.uk*/
/* I declare this is my own work.*/
/* 02 / 01/ 2015                 */
/*********************************/
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//this is the entrance of the programme, show a window with drop boxes to interact with users.
@SuppressWarnings("serial")
public class WeatherGUI extends JFrame implements ActionListener {

	/*
	 * EGLL London Heathrow Airport 
	 * EGCC Manchester Airport 
	 * EGNT Newcastle Airport
	 * EGCN Robin Hood Airport Doncaster Sheffield
	 * ZSPD Shanghai Pudong International Airport
	 * ZSSS Shanghai Hongqiao International Airport
	 * ZYQQ Qiqihar Sanjiazi Airport
	 */
	String[] sAirports = { "EGLL", "EGCC", "EGNT","EGCN","ZSPD","ZSSS","ZYQQ" };
	ArrayList<String> arrayYears = new ArrayList<String>();
	ArrayList<String> arrayMonths = new ArrayList<String>();
	ArrayList<String> arrayDays = new ArrayList<String>();
	String url = "";
	String yearStr = "";
	String monthStr = "";
	String dayStr = "";
	String airportStr = "";
	
	/**********test data 1********/
	/*String yearStr = "2001";
	String monthStr = "10";
	String dayStr = "7";
	String airportStr = "EGCC";*/
	/**************************/
	
	/**********test data 2********/
	/*String yearStr = "2001";
	String monthStr = "12";
	String dayStr = "24";
	String airportStr = "EGCC";*/
	/**************************/
	
	/**********test data 3********/
	/*String yearStr = "2011";
	String monthStr = "3";
	String dayStr = "4";
	String airportStr = "EGLL";*/
	/**************************/
	@SuppressWarnings("rawtypes")
	JComboBox dayBox;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	WeatherGUI() {
		this.setTitle("WeatherGUI");
		this.setSize(400, 200);
		this.setLocation(300, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// initialize years : 2015 down to 2000, users maybe more interested in
		// recent years
		for (int y = 0; y < 16; y++) {
			arrayYears.add(y, Integer.toString(15 - y + 2000));
		}

		// initialize months : 1 to 12
		for (int m = 0; m < 12; m++) {
			arrayMonths.add(m, Integer.toString(m + 1));
		}

		// initialize the days: 30 days
		for (int d = 0; d < 30; d++) {
			arrayDays.add(d, Integer.toString(d + 1));
		}

		// overall panel
		JPanel overallPanel = new JPanel(new GridLayout(3, 5, 30, 30));
		this.add(overallPanel);
		// panel for drop down boxes: airport
		JPanel airPanel = new JPanel(new GridLayout(1, 1));

		JComboBox airportBox = new JComboBox(sAirports);
		airPanel.add(airportBox);
		airportBox.addActionListener(this);
		airportBox.setName("airport");
		airportBox.setSelectedIndex(-1);
		overallPanel.add(airPanel);
							
		// panel for drop down boxes: year, month, day
		JPanel datePanel = new JPanel(new GridLayout(1, 3, 20, 0));
		JComboBox yearBox = new JComboBox(arrayYears.toArray());
		JComboBox monthBox = new JComboBox(arrayMonths.toArray());
		dayBox = new JComboBox(arrayDays.toArray());
		yearBox.addActionListener(this);
		yearBox.setName("year");
		yearBox.setSelectedIndex(-1);
		monthBox.addActionListener(this);
		monthBox.setName("month");
		monthBox.setSelectedIndex(-1);
		dayBox.addActionListener(this);
		dayBox.setName("day");
		dayBox.setSelectedIndex(-1);
		datePanel.add(yearBox);
		datePanel.add(monthBox);
		datePanel.add(dayBox);
		overallPanel.add(datePanel);

		// panel for ok button and cancel button
		JPanel btnPanel = new JPanel(new GridLayout(1, 5, 0, 0));
		btnPanel.add(new JLabel());
		JButton okBtn = new JButton("OK");
		
		JButton closeBtn = new JButton("Close");
		btnPanel.add(okBtn);
		btnPanel.add(new JLabel());
		btnPanel.add(closeBtn);
		btnPanel.add(new JLabel());
		okBtn.addActionListener(this);
		closeBtn.addActionListener(this);
		overallPanel.add(btnPanel);

		this.setVisible(true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		// cope with different components: dropdown boxes and buttons

		if (cmd == "comboBoxChanged") {
			// cope with dropdown boxes : to generate the url
			JComboBox cb = (JComboBox) e.getSource();
			String cbFlag = cb.getName();
			String value = (String) cb.getSelectedItem();

			if (cbFlag == "airport" && value != null) {
				airportStr = value;
			}

			if (cbFlag == "year" && value != null) {
				yearStr = value;
				if(monthStr!=""){
					this.amendDays();
				}
			}

			if (cbFlag == "month" && value != null) {
				monthStr = value;
				if(yearStr!=""){
					this.amendDays();
				}
			}

			if (cbFlag == "day" && value != null) {
				dayStr = value;
			}

			this.geneURL();

		} else {
			// cope with buttons
			if (cmd == "OK") {
				// retrieve weather data
				this.retriveData(url);
			}
			if (cmd == "Close") {
				// exit program
				System.exit(0);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void amendDays() {
		/*
		 * if January , March, May, July, August, October, December: check the
		 * last node of arrayDays, if not "31" --> add node(s) if April, June,
		 * September, November: check the last node of arrayDays, if not "30"
		 * --> add or remove node(s) if February: check the last node of
		 * arrayDays and years, if not 28 or 29 days --> remove or add node(s)
		 */

		int currentLast = Integer.parseInt(arrayDays.get(arrayDays.size() - 1));
		int gap = 0;
		// January , March, May, July, August, October, December:
		if (monthStr.equals("1") | monthStr.equals("3") | monthStr.equals("5")
				| monthStr.equals("7") | monthStr.equals("8")
				|| monthStr.equals("10") | monthStr.equals("12")) {
			gap = 31 - currentLast;

			// February
		} else if (monthStr.equals("2") && yearStr != null) {
			int year = Integer.parseInt(yearStr);
			if (year % 4 == 0) {
				gap = 29 - currentLast;
			} else {
				gap = 28 - currentLast;
			}
		} else {
			// April, June, September, November
			gap = 30 - currentLast;
		}
		if (gap > 0) {
			for (int d = 1; d <= gap; d++) {
				String s = Integer.toString(currentLast + d);
				dayBox.addItem(s);
				arrayDays.add(s);
			}
		}
		if (gap < 0) {
			for (int d = 0; d < -gap; d++) {

				String s = Integer.toString(currentLast - d);
				dayBox.removeItem(s);
				arrayDays.remove(s);
			}
		}

	}

	public void geneURL() {
		// generate url with information of year, month, day and airport
		url = "http://english.wunderground.com/history/airport/" + airportStr
				+ "/" + yearStr + "/" + monthStr + "/" + dayStr
				+ "/DailyHistory.html?HideSpecis=1&format=1";
	}

	public void retriveData(String pUrl) {

		// ensure that the information of airport and date is not null
		if (airportStr != "" && yearStr != "" && monthStr != ""
				&& dayStr != "") {
			// connect to the weather server
			try {
				new WeatherConnection(pUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("Please check the airport and the date!");
		}
	}

	public static void main(String[] args) {
		new WeatherGUI();
	}

}
