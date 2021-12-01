/*********************************/
/* Author: Xingchen Wang         */
/* Email: xwang95@sheffield.ac.uk*/
/* I declare this is my own work.*/
/* 02 / 01/ 2015                 */
/*********************************/
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

//this class is used to get data from the web site, using url
//this class is written by learning the code from the last lecture 
public class WeatherConnection {
	ArrayList<WeatherData> weatherData = new ArrayList<WeatherData>();
	WeatherConnection(String url) throws IOException {
		System.out.println(url);
		
		// Open connection
		URL u = new URL(url);
		URLConnection connection = u.openConnection();

		// check to make sure the page exists
		HttpURLConnection httpConnection = (HttpURLConnection) connection;
		int code = httpConnection.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK)
			return;
		// Read server response
		InputStream instream = connection.getInputStream();
		Scanner in = new Scanner(instream);

		int count = -1;
		// display server response to console
		while (in.hasNextLine()) {
			String input = in.nextLine();
			String[] strArray = input.split(",");
			if(strArray[0].equals("No daily or hourly history data available<br />")){
				System.out.println("There is no data currently");
				return;
			}
			this.addressingData(strArray, count);
			System.out.println(input);
			count++;
		}
			String[] currentInfo = this.getCurrentInfo(url);
			new WeatherDisplay(weatherData,currentInfo);
	}

	//addressing data from string to double, and set them to an object of WeatherData arraylist
	public void addressingData(String[] strArray, int count) {
		if (count >= 1){
			//System.out.println("count: "+count);
				WeatherData data = new WeatherData();
				//addressing time data
				/* e.g.
				 * 8:00 PM -> 20:00 length 2
				 * timeStr12[0] equals: "8:00"
				 * timeStr12[1] equals: "PM"
				 * */
				String[] timeStr12 = strArray[0].split(" "); 
				
				/* e.g.
				 * timeStr24 length 2
				 * timeStr24[0] equals: "8" hour
				 * timeStr24[1] equals: "00" minutes
				 * */
				String[] timeStr24 = timeStr12[0].split(":");
				Double hour = 0.0;
				if(timeStr12[1].equals("PM")){
					// plus 12.0 so that 8.20 PM --> 8.3 --> 20.3 
					hour = Double.parseDouble(timeStr24[0])+ 12.0 + Double.parseDouble(timeStr24[1])/60.0;
					if(hour>=24){//12 PM
						hour = hour - 12.0;
					}
				}else{//AM
					hour = Double.parseDouble(timeStr24[0]) + Double.parseDouble(timeStr24[1])/60.0;
					if(hour>=12){
						hour = hour - 12.0;
					}
				}
				//addressing temperature, pressure, windspeed ,gustSpeed, precipitation
				double temperature = Double.parseDouble(strArray[1]); // C
				double pressure =  Double.parseDouble(strArray[4]); // Sea level pressure hPa
				double windspeed = 0.0;
				
				//addressing something strange: such as clam, -, N/A
				if(!strArray[7].equals("Calm")){
					windspeed =  Double.parseDouble(strArray[7]); // Km/h				
				}
				double gustSpeed = 0.0;
				if(!strArray[8].equals("-") && !strArray[8].equals("")){
					gustSpeed =  Double.parseDouble(strArray[8]); // Km/h				
				}
				double precipitation = 0.0;
				if(!strArray[9].equals("N/A") && !strArray[9].equals("")){
					precipitation =  Double.parseDouble(strArray[9]); // mm				
				}
				//addressing weather event, such as Snow, fog, rain
				String event = strArray[10];

				//setting data into an object of weatherData arraylist
				data.setTime(hour);
				data.setTemperature(temperature);
				data.setPressure(pressure);
				data.setWindspeed(windspeed);
				data.setGustSpeed(gustSpeed);
				data.setPrecipitation(precipitation);
				data.setEvent(event);
				weatherData.add(data);
		}
	}
	
	//get the current infomation, including day, month, year, ICAO code, weather event
	private String[] getCurrentInfo(String url){
		String[] result = new String[6];
		String[] tmp = url.split("/");
		result[0] = tmp[5]; // ICAO code
		result[1] = tmp[8]; // day
		result[2] = tmp[7]; // month
		result[3] = tmp[6]; // year
		
		double totolprec = 0.0;// Precipitation, unit: mm
		String[] weatherE = new String[weatherData.size()];
		String weaStr = "None";
		for (int i = 0; i < weatherE.length; i++) {
			weatherE[i] = weatherData.get(i).getEvent();
			totolprec+= weatherData.get(i).getPrecipitation();
		}
		for (int i = 0; i < weatherE.length; i++) {
			if(weatherE[i].equals("Rain")){
				weaStr = "Rain";
				break;
			}
		}
		for (int i = 0; i < weatherE.length; i++) {
			if(weatherE[i].equals("Fog")){
				weaStr = "Fog";
				break;
			}
		}
		for (int i = 0; i < weatherE.length; i++) {
			if(weatherE[i].equals("Snow")){
				weaStr = "Snow";
				break;
			}
		}
		result[4] = weaStr;//weather event: only track rainy, snow, fog
		result[5] = Double.toString(totolprec);
		return result;
	}

}
