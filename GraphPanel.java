/*********************************/
/* Author: Xingchen Wang         */
/* Email: xwang95@sheffield.ac.uk*/
/* I declare this is my own work.*/
/* 02 / 01/ 2015                 */
/*********************************/
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {
	Double graphHight;
	Double graphWidth;
	Double[] X_line;// x coordinates for line graph
	Double[] Y_line;// y coordinates for line graph
	double[] X_dots;// x coordinates for dots graph
	double[] Y_dots;// y coordinates for dots graph
	Double yMax; // maximum value to display the graph 
	Double yMin = 0.0;// minimum value to display the graph
	int flag; //flag to distinguish drawing temperature, pressure or wind graph
	Color graphColor;
	float shiftX; // the gap between graph and the left boundary	
	float shiftY; // the gap between graph and the top boundary
	Double unitY; // the unit of grid in vertical direction
	Double unitX; // the unit of grid in horizontal direction
	
	//constructor of temperature and pressure graph
	public GraphPanel(ArrayList<Double> windspeed,ArrayList<Double> pTime, int pFlag) {
		flag = pFlag;
		graphHight = 500.0 / 4.0;
		graphWidth = 700.0;
		shiftX = 30.0f;	
		shiftY = 10.0f;
		unitY = graphHight / 4.0;
		unitX = 700.0 / 24.0;
		
		switch (pFlag) {
		case 1:
			graphColor = Color.red;//temperature 
			break;
		case 2:
			graphColor = Color.green; // pressure
			break;
		default:
			System.out.println("check error!");
			break;
		}
		this.preProcess(flag, windspeed, null);
		this.geneCooridinateLine(windspeed, pTime);
	}
	
	//constructor of wind graph
	public GraphPanel(ArrayList<Double> windspeed,ArrayList<Double> gustspeed,ArrayList<Double> pTime,int pFlag) {
		flag = pFlag;
		graphHight = 500.0 / 4.0;
		graphWidth = 700.0;
		shiftX = 30.0f;	
		shiftY = 10.0f;
		unitY = graphHight / 4.0;
		unitX = 700.0 / 24.0;
		this.preProcess(flag, windspeed, gustspeed);
		this.geneCooridinateLine(windspeed, pTime);
		this.geneCooridinateDots(gustspeed, pTime);
	    graphColor = Color.orange;
	}

	//override the paintComponent methods to draw graphs, grid, label, icon etc. 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		this.drawGrid(g2);
		this.drawXAxis(g2);
		this.drawYAxis(g2);
		this.drawLabel(g2, flag);
		this.drawLineGrpah(g2);
		if(flag==3){
			this.drawDotsGraph(g2);
		}
		
	}
	
	// pre-processing data, to get the maximum and minimum value of the graph
	private void preProcess(int flag,ArrayList<Double> windspeed,ArrayList<Double> gustspeed){
		if(flag==3){//wind data
			@SuppressWarnings("unchecked")
			ArrayList<Double> tmpData = (ArrayList<Double>) gustspeed.clone(); 
			while (tmpData.contains(-9999.0)) {
				int index = tmpData.indexOf(-9999.0);
				tmpData.remove(index);
			}
			yMax = Collections.max(windspeed);
			yMin = Collections.min(windspeed);
			Double tmpYMax = Collections.max(gustspeed);
			Double tmpYMin = Collections.min(gustspeed);
			if(tmpYMax > yMax){
				yMax = tmpYMax;
			}
			if(tmpYMin < yMin){
				yMin = tmpYMin;
			}
		}else{
			@SuppressWarnings("unchecked")
			ArrayList<Double> tmpData = (ArrayList<Double>) windspeed.clone(); 
			while (tmpData.contains(-9999.0)) {
				int index = tmpData.indexOf(-9999.0);
				tmpData.remove(index);
			}
			yMax = Collections.max(tmpData);
			yMin = Collections.min(tmpData);
		}
		
	}
	
	//generate coordinate data for line graph
	private void geneCooridinateLine(ArrayList<Double> pData1,ArrayList<Double> pTime){
		X_line = new Double[pData1.size()];
		Y_line = new Double[pData1.size()];
		for (int i = 0; i < pData1.size(); i++) {
			X_line[i] = pTime.get(i)*graphWidth / 24.0 +shiftX;
			Y_line[i] = (yMax - pData1.get(i))*graphHight/(yMax - yMin) + shiftY; 
		}
	}
	
	//generate coordinate data for gust wind speed
	private void geneCooridinateDots(ArrayList<Double> pData2,ArrayList<Double> pTime){
		X_dots = new double[pData2.size()];
		Y_dots = new double[pData2.size()];
		for (int i = 0; i < pData2.size(); i++) {
			X_dots[i] = pTime.get(i)*graphWidth / 24.0 +shiftX;
			Y_dots[i] = (yMax - pData2.get(i))*graphHight/(yMax - yMin) + shiftY; 
		}
	}
	
	//line graph: temperature, pressure, wind speed 
	private void drawLineGrpah(Graphics2D g2){
		g2.setPaint(graphColor);
		g2.setStroke(new BasicStroke(2));
		Double startPointX = X_line[0];
		Double startPointY = Y_line[0];
		for (int i = 1; i < X_line.length; i++) {
			Double endPointX = X_line[i];
			Double endPointY = Y_line[i];
			if(Y_line[i]<150.0){
				g2.draw(new Line2D.Double(startPointX, startPointY, endPointX,endPointY));
				startPointX = endPointX;
				startPointY = endPointY;
				
			}else{
				
					startPointX = endPointX;
					//startPointY = Y1[i+1];
				
			}
		}
	}
	
	//dots graph: gust wind data graph 
	private void drawDotsGraph(Graphics2D g2){
		for (int i = 0; i < X_dots.length; i++) {
			if(Y_dots[i]!=135.0){// gust Windspeed not equals 0
				g2.setPaint(Color.blue);
				g2.setStroke(new BasicStroke(4));
				g2.drawOval((int)X_dots[i],(int)Y_dots[i], 4, 4);	
			}
		}
	}
	
	// the grid
	private void drawGrid(Graphics2D g2) {
		g2.setColor(Color.lightGray);
		g2.setStroke(new BasicStroke(1));
		for (int j = 0; j <= 4; j++) {
			g2.draw(new Line2D.Double(shiftX, shiftY + j * unitY, 700 + shiftX,
					shiftY + j * unitY));
		}
		for (int i = 0; i < 25; i++) {
			g2.draw(new Line2D.Double(shiftX + i * unitX, shiftY, shiftX + i
					* unitX, shiftY + graphHight));
		}
	}
	
	//information at the bottom of graph, i.e. the x axis 
	private void drawXAxis(Graphics2D g2) {
		g2.setColor(Color.black);

		for (int i = 0; i <= 12; i++) {
			if (i == 0) {
				g2.drawString("midnight", (int) (i * unitX), 145);
			} else if (i == 12) {
				g2.drawString("noon", (int) (shiftX / 2.0 + i * unitX), 145);
			} else if (i < 10) {
				g2.drawString(Integer.toString(i), (int) (shiftX * 0.9 + i
						* unitX), 145);

			} else {
				g2.drawString(Integer.toString(i), (int) (shiftX * 0.8 + i
						* unitX), 145);
			}
		}
		for (int i = 13; i <= 23; i++) {
			if (i - 12 < 10) {

				g2.drawString(Integer.toString(i - 12), (int) (shiftX * 0.9 + i
						* unitX), 145);
			} else {
				g2.drawString(Integer.toString(i - 12), (int) (shiftX * 0.8 + i
						* unitX), 145);
			}

		}
	}

	//information at the right of graph, i.e. the y axis
	private void drawYAxis(Graphics2D g2) {
		for (int i = 0; i <= 4; i++) {
			double value = (yMax - yMin) * (double) (4 - i) / 4.0 + yMin;
			BigDecimal bg = new BigDecimal(value);
			value = bg.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
			g2.drawString(Double.toString(value), 700 + shiftX + 2, (float) ((yMax - value)*graphHight/(yMax - yMin) + shiftY/*this.chartY(value)(int) (unitY * i + shiftY)*/));

		}
	}
	
	//label information at the right-hand side of the graph
	private void drawLabel(Graphics2D g2, int flag) {
		int labelX = (int) shiftX + 735;
		int labelY = 10;
		int unitY = labelY + 15;
		int lineY = unitY + 15;
		switch (flag) {
		case 1:
			g2.drawString("Temeprature Graph", labelX, labelY);
			g2.drawString("Units: C", labelX, unitY);
			break;
		case 2:
			labelX = labelX + 10;
			g2.drawString("Pressure Graph", labelX, labelY);
			g2.drawString("Units: hPa", labelX, unitY);
			break;
		case 3:
			labelX = labelX + 5;
			g2.drawString("Windspeed Graph", labelX, labelY);
			g2.drawString("Units: km/h", labelX, unitY);
			
			g2.drawString("Gustspeed Graph", labelX, labelY+60);
			g2.drawString("Units: km/h", labelX, unitY+60);
			g2.drawString("Color:", labelX, unitY+75);
			g2.setColor(Color.blue);
			g2.setStroke(new BasicStroke(4));
			g2.drawOval(labelX + 44, unitY+69, 4, 4);
			break;
		default:
			System.out.println("check error!");
			break;
		}
		g2.setColor(Color.black);
		g2.drawString("Color:",labelX, lineY);
		g2.setColor(graphColor);
		g2.setStroke(new BasicStroke(3));
		g2.drawLine(labelX + 42, lineY - 4, labelX + 80, lineY-4);
	}

}