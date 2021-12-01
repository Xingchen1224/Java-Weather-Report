/*********************************/
/* Author: Xingchen Wang         */
/* Email: xwang95@sheffield.ac.uk*/
/* I declare this is my own work.*/
/* 02 / 01/ 2015                 */
/*********************************/
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WeatherDisplay extends JFrame {
	ArrayList<Double> timeData;
	public WeatherDisplay(ArrayList<WeatherData> weatherData,String[] currentInfo) {
		ArrayList<Double> temData = this.makeDataList(1, weatherData);
		ArrayList<Double> preData = this.makeDataList(2, weatherData);
		ArrayList<Double> winData = this.makeDataList(3, weatherData);
		ArrayList<Double> gustData = this.makeDataList(4, weatherData);
		
		//main panel of frame
		JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 20));
		this.add(mainPanel);

		//flag: 1 for temperature chart; 2 for pressure chart; 3 for windspeed chart
		mainPanel.add(new GraphPanel(temData,timeData,1)); // temperature panel for drawing temperature graph
		mainPanel.add(new GraphPanel(preData,timeData,2));// pressure panel for drawing pressure graph
		mainPanel.add(new GraphPanel(winData,gustData,timeData,3));// wind panel for drawing wind graph, including wind speed and gust speed data
		
		//infomation panel for displaying information
		JPanel bottomPanel = new InfoPanel(currentInfo,this);
		mainPanel.add(bottomPanel);

		this.setTitle("Display weather data");
		this.setSize(900, 700);
		this.setLocation(300, 0);
		this.setVisible(true);
	}

	/*
	 * flag: temperature flag = 1 , pressure flag = 2 , wind speed flag = 3 , wind gust flag = 4
	 */
	
	private ArrayList<Double> makeDataList(int flag, ArrayList<WeatherData> weatherData) {
		ArrayList<Double> data = new ArrayList<Double>();
		timeData = new ArrayList<Double>();
		for (int i = 0; i < weatherData.size(); i++) {
			timeData.add(weatherData.get(i).getTime());
			switch (flag) {
			case 1:
				data.add(weatherData.get(i).getTemperature());
				break;
			case 2:
				data.add(weatherData.get(i).getPressure());
				break;
			case 3:
				data.add(weatherData.get(i).getWindspeed());
				break;
			case 4:
				data.add(weatherData.get(i).getGustSpeed());
				break;
			default:
				break;
			}
			
		}
		
		return data;
	}
}
