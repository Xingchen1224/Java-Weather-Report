/*********************************/
/* Author: Xingchen Wang         */
/* Email: xwang95@sheffield.ac.uk*/
/* I declare this is my own work.*/
/* 02 / 01/ 2015                 */
/*********************************/
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {

	public InfoPanel(String[] currentInfo, final JFrame paraentFrame) {
		super(new GridLayout(1, 4));

		/*
		 * currentInfo[]: currentInfo[0]: ICAO code 
		 * currentInfo[1]: Day
		 * currentInfo[2]: Month 
		 * currentInfo[3]: Year
		 * currentInfo[4]: weather event image
		 * currentInfo[5]: precipitation 
		 */

		JPanel leftPanel = new JPanel(new GridLayout(4, 1, 20, 0));
		this.add(leftPanel);
		leftPanel.add(new JLabel("      Date: " + currentInfo[1] + " / "
				+ currentInfo[2] + " / " + currentInfo[3]));
		leftPanel.add(new JLabel("      ICAO code: " + currentInfo[0]));
		leftPanel.add(new JLabel(this.getAirport(currentInfo[0])));
		leftPanel.add(new JLabel("      Author: Xingchen Wang"));

		//middle panel is divided into two parts: 
		//1.left panel for displaying total precipitation
		JPanel middleLeftPanel = new JPanel();
		this.add(middleLeftPanel);
		middleLeftPanel.add(new JLabel("Total precipitation: "+currentInfo[5]+" mm"));
		
		//2.right panel for display weather icon
		JPanel middleRightPanel = new JPanel();
		this.add(middleRightPanel);
		/*display an icon of weather: only considering
		 * snow fog rain, else using none.png
		 * */ 
		ImageIcon img = new ImageIcon(currentInfo[4]+".png");
		middleRightPanel.add(new JLabel(img));

		//the right panel has two button: 
		//clicking back button to the weatherGUI 
		JPanel rightPanel = new JPanel(new GridLayout(2,3,0,20));
		this.add(rightPanel);
		rightPanel.add(new JLabel());
		JButton backBtn = new JButton("Back");
		
		//close parent frame, i.e. the object of WeatherDisplay
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paraentFrame.dispose();
			}
		});
		rightPanel.add(backBtn);
		rightPanel.add(new JLabel());
		
		rightPanel.add(new JLabel());
		// close button to quit the whole programme.
		JButton closeBtn = new JButton("Close");
		closeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		rightPanel.add(closeBtn);
		rightPanel.add(new JLabel());
	}

	private String getAirport(String code){
		//Mapping ICAO code to airport string.
		String result = "";
		switch(code){
			case "EGCC":
				result = "      Manchester Airport";
				break;
			case "EGLL":
				result = "      London Heathrow Airport";
				break;
			case "EGNT":
				result = "      Newcastle Airport";
				break;
			case "EGCN":
				result = "      Robin Hood Airport Doncaster Sheffield";
				break;
			case "ZSPD":
				result = "      Shanghai Pudong International Airport";
				break;
			case "ZSSS":
				result = "      Shanghai Hongqiao International Airport";
				break;
			case "ZYQQ":
				result = "      Qiqihar Sanjiazi Airport";
				break;
				
				 
		}
		return result;
	}
	
}
