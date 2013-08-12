package aiml;
import robocode.*;
import java.awt.Color;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/****************************************************************************************
 * DeepThought - a robot by Jason Max Chalom 711985								
 * 																						
 * Algorithm:																			
 * 																						
 * The robot runs on a state machine									
 * 																						
 * 
 * 
 * There has been use of some wits samples since reinventing the wheel is not efficient										
 ****************************************************************************************/

//TODO
//fix wall hit in corners
//tracker
//bullet track
//track enemy
//life track ie bullet fire

public class DeepThought extends AdvancedRobot
{
	/**
	 * run: DeepThought's default behavior
	 */
	//global primitives
	//such as the defined states
	//defined number of states
	public String State[] = new String[8];
	public boolean SState[] = new boolean[8];
	public int NumTrack;
	public int HitCount;
	public double ExactRobotPosition;
	//get life from robot
	

	public void InitiateStates()
	{
		//set up states
		//The start state will be true at the beginning
		State[0] = "State: Start State"; SState[0] = true;
		State[1] = "State: Aggresive"; SState[1] = false;
		State[2] = "State: Scared"; SState[2] = false;
		State[3] = "State: Hunter"; SState[3] = false;
		State[4] = "State: Have Target"; SState[4] = false;
		State[5] = "State: Being Attacked"; SState[5] = false;
		//the only exit state or dead where code is gone
		State[6] = "State: Victory"; SState[6] = false;
		//In case states fail
		State[7] = "State: Default"; SState[7] = false;
		
		//also initialise variables
		NumTrack = 0;
		HitCount = 0;
		ExactRobotPosition = 0.00;
	}
	
	//functions used for behaviour
		public void TurnRadar(double degrees, String direction)
		{
			//The advanced features of turning radar will go here
			if(direction.equals("Right"))
			{
				turnRadarRight(degrees);
			}
			else if(direction.equals("Left"))
			{
				turnRadarLeft(degrees);
			}		
		}	
		
		public void TurnGun (double degrees, String direction)
		{
			//the advanced features of turning the gun will go here
			if(direction.equals("Right"))
			{
				turnGunRight(degrees);
			}
			else if(direction.equals("Left"))
			{
				turnGunLeft(degrees);
			}	
		}
		
		public void TurnBot (double degrees, String direction)
		{
			//the advanced features of turning the bot will go here
			if(direction.equals("Right"))
			{
				turnRight(degrees);
			}
			else if(direction.equals("Left"))
			{
				turnLeft(degrees);
			}	
		}
	
	
	//state classes
	
	//The start class will dictate movement and initial tracking
	public void CheckState()
	{
		//lists of states
		/*****************************************************
		State[0] = "State: Start State"; SState[0] = true;
		State[1] = "State: Aggressive"; SState[1] = false;
		State[2] = "State: Scared"; SState[2] = false;
		State[3] = "State: Hunter"; SState[3] = false;
		State[4] = "State: Have Target"; SState[4] = false;
		State[5] = "State: Being Attacked"; SState[5] = false;
		//the only exit state or dead where code is gone
		State[6] = "State: Victory"; SState[6] = false;
		//In case states fail
		State[7] = "State: Default"; SState[7] = false; 
		******************************************************/
		
		//this will check current state and variables
		//and will then change the state accordingly
		
		//sstate[0] is permanently false after the beginning sequence
		if(SState[0] == true)
		{
			SState[0]=false;
		}
		
		else if (getEnergy() > 50.00)
		{
			SState[1] = true;
			SState[2] = false;
		}
		
		else if (getEnergy() < 50.00)
		{
			SState[1] = false;
			SState[2] = true;
		}
		
		if (SState[4] == true)
		{
			SState[3] = true;
		}
		else
		{
			SState[3] = false;
		}
		
		if(SState[5] == true)
		{
			SState[2] = true;
			SState[1] = false;
		}
		
	}
	
	public void run() {
		// Initialization of the robot should be put here
		System.out.println("Hello World.");
		//do colouring
		setGunColor(Color.blue);
		setRadarColor(Color.black);
		setBodyColor(Color.black);
		setScanColor(Color.green);
		setBulletColor(Color.white);
		
		//environ variables
		setAdjustRadarForRobotTurn(false);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(false);
		
		//initialise states
		InitiateStates();
		
		// Robot main loop
		while(true) {
			//Main Movement
			//SState[2] = true;
			// Replace the next 4 lines with any behavior you would like
			turnLeft(Math.random()*100+1);
			ahead(Math.random()*100+1);
			//check if target has been found
			if(SState[4] == false)
			{
				//turnRadarLeft(360);
				turnGunRight(360);
			}
			
			
			//check if fired
			if (getGunHeat() != 0) 
			{
				turnGunRight(360);
				
			}
			turnGunRight(360);
				//scan();
			
			
				
			
			//check states
			CheckState();
			
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//depreciated kept for testing purposes
		//turnGunLeft(90+e.getBearing());
		//fire(1);
		
		//change the state
		SState[4] = true;
		out.println(State[4]);
		
		//the actual firing occurs in main thread in a state
		//track increases
		NumTrack++;
		//uses some 
		//turnGunLeft(e.getHeading());
		ExactRobotPosition = normalRelativeAngleDegrees((getGunHeading()+ e.getHeading())- getRadarHeading()*Math.sin(e.getBearingRadians()));
	
		//turnLeft(ExactRobotPosition);
		
		//turnRadarRight(ExactRobotPosition);
		turnGunLeft((360-ExactRobotPosition)-Math.abs(getGunHeading()-getRadarHeading()));
		
		//firing and other bits
				//then move gun and aim
				out.println("Exact Robot pos: "+ExactRobotPosition);
		if(SState[2] == true)
		{
			double Angles = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
			if (e.getBearing() < 90 || e.getBearing() < 270)
			{
				//TurnRadar (e.getBearing(), "Right");
				TurnGun (e.getBearing(), "Right");
				TurnBot (Angles, "Left");
			}
			else
			{
				//TurnRadar (e.getBearing(), "Left");
				TurnGun (e.getBearing(), "Left");
				TurnBot (90-Angles, "Right");
			}
			ahead(100);
		}
		else
		{
			//turnRadarLeft(ExactRobotPosition);
			
			turnGunRight((360-ExactRobotPosition)-Math.abs(getGunHeading()-getRadarHeading()));
			fire(2);
				double lifetmp = getEnergy();
				//check for any state where the bot will shoot and the nshoot with according power
				if (SState[0] == true)
				{
					//start state will shoot at power 1
					fire(1);
					SState[4]=false;
				}
				else if (SState[1] == true)
				{
					fire(2);
					SState[4]=false;
				}
				else if (SState[3] == true)
				{
					fire(1);
					SState[4]=false;
				}
				else if (SState[7] == true) //default will be a basic robot
				{
					fire(1);
					SState[4]=false;
				}
				
				if(lifetmp < getEnergy())
				{
					//hit target
					fire(0.5);
					turnRight(Math.random()*200+1);
					ahead(Math.random()*100+1);	
				}
				else
				{
					//missed
					turnLeft(360-ExactRobotPosition);
					back(15);
					//scan();
				}
				
		}
		
			
		
		//scan();
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(100);
		turnRight(100);
		//turnLeft(25);
		back(50);
		turnRight(20);
		ahead(100);
		
		back(100);
		turnLeft(100);
		//turnRight(50);
		back(50);
		turnLeft(20);
		ahead(100);
	}
	
	public void onHitRobot(HitRobotEvent e) {
		
		back(10);
		turnRight(100);
		//turnLeft(25);
		back(50);
		turnRight(20);
		ahead(100);
		
		back(10);
		turnLeft(100);
		//turnRight(50);
		back(50);
		turnLeft(20);
		ahead(100);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		out.print("Hit Wall!");
		//must move according to side of wall on
		//turnLeft(90-getHeading());
		//ahead(50);
		
			//turnLeft(90-e.getBearing());
			turnLeft(90-e.getBearing());
			ahead(50);
		
		
		
	}	
	
	//on victory do some cool effects
	//small and a waste of space but hey its cool
	public void Victory(WinEvent e)
	{
		SState[5] = true;
		out.println(State[5]);
		
		out.println("I have Won!");
		for (int i = 0; i < 25; i++)
		{
			//change colours
			setGunColor(Color.green);
			setRadarColor(Color.white);
			setBodyColor(Color.red);
			
			turnRight(360);
			ahead(5);
			
			//change colours
			setGunColor(Color.blue);
			setRadarColor(Color.black);
			setBodyColor(Color.black);
			
		}
	}	
}
								