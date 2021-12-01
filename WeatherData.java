/*********************************/
/* Author: Xingchen Wang         */
/* Email: xwang95@sheffield.ac.uk*/
/* I declare this is my own work.*/
/* 02 / 01/ 2015                 */
/*********************************/
/*
 * WeatherData is the data types which is needed by the program, including temperature,
 * atmospheric pressure, wind data and total precipitation etc.
 */
public class WeatherData {
	Double time;  //e.g. 8.3 h easy to calculate for x coordinate
	Double temperature; //C
	Double pressure; // Sea level pressure hPa
	Double windspeed; //Km/h
	Double gustSpeed; //Km/h
	Double precipitation; //mm
	String event; // weather event: Rain, Snow, Fog, etc.
	
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Double getPressure() {
		return pressure;
	}
	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}
	public Double getWindspeed() {
		return windspeed;
	}
	public void setWindspeed(Double windspeed) {
		this.windspeed = windspeed;
	}
	public Double getGustSpeed() {
		return gustSpeed;
	}
	public void setGustSpeed(Double gustSpeed) {
		this.gustSpeed = gustSpeed;
	}
	public Double getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(Double precipitation) {
		this.precipitation = precipitation;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public Double getTime() {
		return time;
	}
	public void setTime(Double time) {
		this.time = time;
	}
	
}
