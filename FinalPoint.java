//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//Final Point - the home lily pad the user frog aims to go to. This class creates the individual home lily pad objects.
//It keeps track whether if user had collided with the individual objects (user cannot collide with the same home spot in the same
//level). It also keeps track if it has a flies inside the object, and when it does have the fly, display the fly in the game and
//check if the user had collide with it.
public class FinalPoint{
	private int px, py; //the position of the object (top left corner)
	private int distx, disty; //the width and height of the object (in terms of x- and y- axis)
	
	private int count; //the number of time, in loops, the fly had stay on the home spot
	private int delay = 400; //the max number of time, in loops, the fly will stay in the spot
	
	private Image homefrogPic = new ImageIcon("graphic/homefrog.png").getImage(); //picture to display and slert user the home spot had been reached in this level
	private Image flyPic = new ImageIcon("graphic/fly.png").getImage(); //picture for the fly
	
	private boolean reached = false; //whether if the spot had been reached
	private boolean fly = false; //whether if there is a fly
	
	public FinalPoint(int x, int y, int distx, int disty){
		px = x;
		py = y;
		this.distx = distx;
		this.disty = disty;
	}
	
	//setup the information when a homespot is created
	public void draw(Graphics g){
		if (reached == true){
			g.drawImage(homefrogPic, px+12, py+5, null); //add 12 & 5 so that the picture appears at the centre of the FinalPoint
		}
		if (fly == true){
			update();
			g.drawImage(flyPic, px+12, py+5, null); //add 12 & 5 so that the picture appears at the centre of the FinalPoint
		}
	}
	
	//checks if the user had collided with the individual finalpoint object
	public boolean checkCollide(Frog user){
		//collision is based on image; they are all rectangles
		Rectangle user_area = new Rectangle(user.getFx(), user.getFy(), user.getWidth(), user.getHeight());
		Rectangle home_area = new Rectangle(px, py, distx, disty);
		//check if the image intersects with each other
		if (user_area.intersects(home_area) && reached == false){
			return true;
		}
		return false;
	}
	
	//reset the status of the home spot when a new level starts
	public void reset(){
		fly = false;
		reached = false;
	}
	
	//keep track of how long the fly had and will stay on the home spot
	public void update(){
		count++;
		if (count == delay){
			count = 0;
			fly = false; //fly will now disappear
		}
	}
	
	//GETTERS AND SETTERS
	public void setReached(boolean state){
		reached = state;
	}
	public boolean getReached(){
		return reached;
	}
	
	public boolean getFly(){
		return fly;
	}
	public void setFly(boolean state){
		fly = state;
	}
}