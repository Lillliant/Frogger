//import libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//Class Log creates and displays the individual log objects in the main game. The player must collide with it in order to not die when
//the frog is in the water zone. Each individual log also keeps track of its animation state (submergence); if it has animation
//feature enabled, then when it is fully submerged the player on top of it will die. The class will keep track if the user
//had collided with the individual objects
public class Turtle{
	private int dist; //the distance the object will travel in every loop
	private int tx, ty; //the position of the log (top left point)
	private int height, width; //the width and height of the "rectangular" turtles
	private int bound; //the boundary of which the object will repeat its journey when reached
	private int start; //the start of the path the object will go to after completing one lap
	
	private Image turtle_pic;
	private Image[] turtle_pics = new Image[6]; //for when there is animation - stores the different stages of submergence
	private boolean animation; //whether if the turtles object have its animation features enabled
	
	private int count = 0; //the number of time, in loops, the turtle is in the loop of stage
	private int delay = 50; //the time of loop before blitting the next stage of picture
	private int frame; //the frame of reference for turtle_pics for each stage
	
	private boolean disappear = false; //the state when the turtle is fully submerged

	public static final int LEFT = 0; //the direction the turtles will move in
	
	//setup the information when an object is created
	public Turtle(int x, int y, int distance, int boundary, int startpt, boolean animation, String name){
		dist=distance;
		tx=x;
		ty=y;
		bound = boundary;
		start = startpt;
		this.animation = animation;
		//stores images in different arrays depending on the animation feature (enabled/disabled)
		if (animation==false){
			turtle_pic = new ImageIcon("turtle/turtle"+name+".png").getImage(); //only 1 pic when no animation
			//because there is no animation, w and h are stable
			width = 3*turtle_pic.getWidth(null); //there are 3 turtles in one object
			height = turtle_pic.getHeight(null);
		}
		else if (animation == true){
			for (int i = 0; i<turtle_pics.length; i++){
				turtle_pics[i] = new ImageIcon("turtle/turtle"+i+".png").getImage(); //multiple pics when animation
			}
			width = 3*turtle_pics[frame].getWidth(null); //there are 3 turtles in one object
			height = turtle_pics[frame].getHeight(null);
		}
	}
	
	//move the object
	public void move(){
		tx-=dist;
		if (tx<bound){
			tx=start; //restart after finishing one lap
		}
	}
	
	//displays the object
	public void draw(Graphics g){
		//split the function based on animation (enabled/disabled)
		if (animation ==true){
			update(); //update the time of which object had remained in the stage
			//need to update to avoid changes in w and h that are not documented
			width = 3*turtle_pics[frame].getWidth(null); //there are 3 turtles per object
			height = turtle_pics[frame].getHeight(null);
			for (int i=0; i<3; i++){
				g.drawImage(turtle_pics[frame], tx+i*48, ty, null);
			}
		}
		else if (animation == false){
			//because there is no animation, there is no need to update anything
			for (int i=0; i<3; i++){
				g.drawImage(turtle_pic, tx+i*48, ty, null); //there are 3 turtles per object, so loop 3 times
			}	
		}
	}
	
	//check if the user had collided with the turtle object
	public boolean checkCollide(Frog user){
		//collision is based on image; they are all rectangles
		Rectangle user_area = new Rectangle(user.getFx(), user.getFy(), user.getWidth(), user.getHeight());
		Rectangle turtle_area = new Rectangle(tx, ty, width, height);
		//check if the image collided with each other
		if (user_area.intersects(turtle_area)){
			return true;
		}
		return false;
	}
	
	//update the stage the turtles are in
	public void update(){
		count++;
		if (count%delay==0){ //each stage lasts for 50 loops, and the cycle will repeat over and over again
			frame++;
		}
		if (frame == 5){
			disappear = true; //the turtles are fully submerged at frame 5
		}
		if (frame == 6){ //avoid out of bound (only 5 stages)
			frame = 0;
			count = 0;
			disappear = false;
		}
	}
	
	//GETTERS AND SETTERS
	public int getSpeed(){
		return dist;
	}
	public int getDir(){
		return LEFT;
	}
	public boolean getDisappear(){
		return disappear;
	}
}