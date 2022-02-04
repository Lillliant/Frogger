//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//Class Log creates and displays the individual log objects in the main game. The player must collide with it in order to not die when
//the frog is in the water zone. Each individual log also keeps track if it has a lady frog, and when
//it does have a lady frog object, Log class keeps track of how long the lady frog will remain on the log, 
//if the player had collided with it, and displays the ladyfrog on top of the log.
public class Log{
	private int dist; //the distance the object will travel in every loop
	private int lx, ly; //the position of the log (top left point)
	private int bound; //the boundary of which the object will repeat its journey when reached
	private int start; //the start of the path the object will go to after completing one lap
	private Image log_pic;
	private int width, height; //the width and height of the "rectangular" log
	
	private boolean ladyfrog = false; //the status of whether or not the log object has the lady frog right now
	private Image ladyfrogPic = new ImageIcon("graphic/ladyfrog.png").getImage();
	private int l_width = ladyfrogPic.getWidth(null), l_height = ladyfrogPic.getHeight(null);
	private int lfx, lfy; //the position of the lady frog
	private int count;//the number of time (in loops) the lady frog had remained on the log
	public final static int delay = 500; //the max number of time the lady frog will remain

	public static final int RIGHT = 1; //the direction the log will move in
	
	//setup the information when a log is created
	public Log(int x, int y, int distance,int boundary, int startpt, String name){
		dist=distance;
		lx=x;
		ly=y;
		bound=boundary;
		start=startpt;
		log_pic = new ImageIcon("log/log"+name+".png").getImage();
		width = log_pic.getWidth(null); //the log doesn't have animation, therefore its width and height are stable.
		height = log_pic.getHeight(null);
	}
	
	//move the log and lady frog
	public void move(){
		lx+=dist;
		lfx+=dist; //lady frog will move with the log at the same speed to remain on the log (relative velocity)
		
		//to restart the log and lady frog on the lap
		if (lx>bound){
			lx=start;
			lfx=start;
		}
	}
	
	//displays the log and lady frog
	public void draw(Graphics g){
		g.drawImage(log_pic, lx, ly, null);
		
		if (ladyfrog == true){
			update(); //update the time of which lady frog had remained on the log
			g.drawImage(ladyfrogPic, lfx, lfy, null);
		}
	}
	
	//when the ladyfrog had been on the log for 500 loops it will disappear if the user haven't collided it
	public void update(){
		count++;
		if (count == delay){
			count = 0;
			ladyfrog = false;
		}
	}
	
	//check if the user collided with this log
	public boolean checkCollide(Frog user){
		//collision is based on image; they are all rectangles
		Rectangle user_area = new Rectangle(user.getFx(), user.getFy(), user.getWidth(), user.getHeight());
		Rectangle log_area = new Rectangle(lx, ly, width, height);
		//check if the image collided with each other
		if (user_area.intersects(log_area)){
			return true;
		}
		return false;
	}
	
	//check if the user collided with the lady frog on the log if it's there
	public boolean checkLadyFrogCollide(Frog user){
		if (ladyfrog == true){
			//collision is based on image; they are all rectangles
			Rectangle frog_area = new Rectangle(lfx, lfy, l_width, l_height);
			Rectangle user_area = new Rectangle(user.getFx(), user.getFy(), user.getWidth(), user.getHeight());
			//check if the image collided with each other
			if (user_area.intersects(frog_area)){
				ladyfrog = false;
				return true;
			}
		}
		return false;
	}
	
	//when called, lady frog will now appear on the log for a certain amount of time.
	public void setLadyFrog(){
		ladyfrog = true;
		//the position of the lady frog based on the position of the log
		lfx = lx+(width%37)+37; //makes sure the user can reach it, since the user moved in a unit of 37
		lfy = ly+5; //center ladyfrog on the log
	}
	
	//GETTERS AND SETTERS
	
	//dist is the speed the log moves in per loop
	public int getSpeed(){
		return dist;
	}
	public int getDir(){
		return RIGHT; //there is only one direction for log - to the right
	}
	public int getLx(){
		return lx;
	}
	public int getLy(){
		return ly;
	}
}