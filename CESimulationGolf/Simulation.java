import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

import org.math.plot.*;
import org.math.plot.plotObjects.*;
/**
* @author Spiros Mavroidakos
* @version V4
* Program to simulate the trajectory of a golf ball 
*
**/

public class Simulation
{
    	private static final double radius = 0.04267/2; //the radius of the balls
	private static final double BallMass = 0.04593; //kilograms for the mass of the golf ball
	private static final double AirDensity = 1.225; //kg/m^3 air density at room temperature

		//Constantly changing values
	public static double XDragforce = 0; // placeholder for the value of the Drag force in the x direction
	public static double YDragforce = 0; // placeholder for the value of the Drag force in the y direction
	public static double ZDragforce = 0;

	public static double XLiftforce = 0; // placeholder for the value of the lift force in the x direction
	public static double YLiftforce = 0; // placeholder for the value of the Lift force in the y direction
	public static double Xspin = 0;
	public static double Yspin, Zspin = 0;



	public static double Xballvel,Yballvel,Zballvel = 0; // component acceleration
	public static double angle = 0;
	public static int temp = 0;

	public static double dt = 0.01;//time step
	public static double Xacclr, Yacclr, Zacclr = 0; // component acceleration

		// Distances
	public static double Xdistance = 0;
	public static double Ydistance = 0;
	public static double BallVel = 0;

		//data points
	public static double [] Xdata = new double [15000]; //Data points in the x
	public static double [] Ydata = new double [15000]; //Data points in the y
	public static double [] xdist = new double [15000];
	public static double [] ydist = new double [15000];

	public static int counter = 0;
	
    public static void main(String[] args)
    {
    PrintWriter outputFile = null;
    try
    {
        outputFile = new PrintWriter(new FileOutputStream("position.txt",false));
    }
     catch(FileNotFoundException e)
    {
        System.out.println("File error.  Program aborted.");
         System.exit(0);
    }
	for (int i = 0; i < 240; i++ ) {

		angle += 0.00436;// is equal to 0.25 degrees
		BallVelocityImp ();// Gets initial Velocity from collision b/w Ball and golf club

		while (ydist[i] >= 0)
		{ // condition to continue loop as long as the ball does not touch the ground

		BallVelocity ();
		DragFactor(); //update drag
		Backspin();

		Acceleration(); //update acceleration

		xdist[i] += Xballvel*(dt) + 0.5*(Xacclr)*(Math.pow(dt,2));
		ydist [i] += Yballvel*(dt) + 0.5*(Yacclr)*(Math.pow(dt,2));

		VelUped (); //updates the velocity

		}
		if (i >= 1){

			if (xdist [i] >= xdist[i-1]){
				temp = i + 1;

				}

			}

	}



			angle = temp * 0.00436;
			BallVelocityImp ();// Gets initial Velocity from collision b/w Ball and golf club


		System.out.println("********** Golf Simulation **********" + "\n");
		System.out.println("Velocity of Ball: 70m/s");
		System.out.println ("The ball is hit at an angle of:" + angle + " radians");
		System.out.println("Initial Ball velocity in: ");
		System.out.println("X-direction: " + Xballvel + " Y-direction: " + Yballvel);

					while (Ydistance >= 0){ // condition to continue loop as long as the ball does not touch the ground

					Xdata[counter] = Xdistance;//fills the x position data point
					Ydata[counter] = Ydistance;//Fills the y position data point

					BallVelocity ();
					DragFactor(); //update drag
					Backspin();

					Acceleration(); //update acceleration
					distance(); //updates distance in x and y
					VelUped (); //updates the velocity



					counter++;

			}

			Xdata[counter] = Xdistance;
			Ydata[counter] = Ydistance;


			System.out.println("\n" + "********** End Values **********" + "\n");
			System.out.println("Final velocity in: ");
			System.out.println("X-direction: " + Xballvel + " Y-direction: " + Yballvel);
			System.out.println("Total distance travelled (m): " + Xdistance + "\n");
			System.out.println("********** End of Simulation **********");


			////////////////////////

			for(int i=0;i<(15000);i++) {

						outputFile.println(Xdata[i]+ "	" + Ydata[i]);
			}

			Plot2DPanel plot2 = new Plot2DPanel();

			// define the legend position
			plot2.addLegend("SOUTH");

			// add a line plot to the PlotPanel
			plot2.addScatterPlot(" Position of ball (m)", Xdata, Ydata);


			//set axis labels
			plot2.setAxisLabel(0,"X position (m)");


			//set title
			BaseLabel title2 = new BaseLabel("Position of a golf ball", Color.BLACK, 0.5, 1.1);
			title2.setFont(new Font("Courier", Font.BOLD, 14));
			plot2.addPlotable(title2);

			// put the PlotPanel in a JFrame like a JPanel
			JFrame frame2 = new JFrame("a plot panel");
			frame2.setSize(600, 600);
			frame2.setContentPane(plot2);
			frame2.setVisible(true);
			outputFile.close();
		}

	public static void BallVelocity (){ //calcuates the impact velocity of the ball

			BallVel = Math.sqrt ((Math.pow (Xballvel,2)) + (Math.pow (Yballvel,2)));

	}
	public static void DragFactor(){ // calculates the effect of drag for both the x and y direction

	if (BallVel <= 14){
			double Cd = 0.5;

			XDragforce = Cd * AirDensity *(Math.PI * Math.pow(radius,2))* (BallVel * Xballvel);//Drag equation
			YDragforce = Cd * AirDensity *(Math.PI * Math.pow(radius,2))* (BallVel * Yballvel);//Drag equation


		} else {
			double Cd = 7/BallVel;

			XDragforce =  Cd * AirDensity *(Math.PI * Math.pow(radius,2))* (BallVel * Xballvel);//Drag equation
			YDragforce = Cd * AirDensity *(Math.PI * Math.pow(radius,2))* (BallVel * Yballvel);//Drag equation

		}

		}



public static void VelUped(){
			Xballvel += Xacclr*dt;
			Yballvel += Yacclr*dt;
			//Zballvel += Zacclr*dt;
		}
public static void distance (){

			Xdistance += Xballvel*(dt) + 0.5*(Xacclr)*(Math.pow(dt,2));
			Ydistance += Yballvel*(dt) + 0.5*(Yacclr)*(Math.pow(dt,2));

		}


public static void Acceleration(){
			Xacclr = -(XDragforce/BallMass)+ Xspin;
			Yacclr = -(YDragforce/BallMass)- 9.8 + Yspin;
			//Zacclr = -(ZDragforce/BallMass)+ Zspin;
		}
public static void BallVelocityImp () { //calculates the velocity of the golf ball moving 70 m/s

		Xballvel = 70 * Math.cos(angle);
	    Yballvel = 70 * Math.sin(angle);



		}

public static void Backspin(){

		double x = 0;
		double y = 0;
		double z = -1; // adds the backspin effect
		Xspin = 0.25 * (z*Yballvel - y*Zballvel); // multiplies a constant with the cross product of the angular velocity and the velocity of the ball
		Yspin = 0.25 * (x*Zballvel - z*Xballvel); // same as above
		//Zspin = 0.25 * (y*Xballvel - x*Yballvel);

	}

}
