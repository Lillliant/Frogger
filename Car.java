//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//Class Car creates and displays the individual car objects in the main game. It is a basic object that
//moves in a certain speed along a certain direction in a certain position. If it collides with the 
//player the player object will die.
public class Car{
	private int dist; //the distance the object will travel in every loop
	private int cx, cy; //the position of the object
	private int dir; //the direction the cars will move in
	private Image car_pic;
	private int width, height; //the width and height of the "rectangular" car

	//the directions the car can move in
	public static final int LEFT = -1, RIGHT = 1;
	
	//setup the information when a car is created
	public Car(int x, int y, int distance, int direction, String name){
		dist=distance;
		cx=x;
		cy=y;
		dir=direction;
		car_pic = new ImageIcon("cars/car"+name+".png").getImage(); //there are different types of car images, ordered by numbers
		width = car_pic.getWidth(null); //the car doesn't have animation, therefore its width and height are stable.
		height = car_pic.getHeight(null);
	}
	
	//move the car object
	public void move(){
		cx+=(dist*dir);
		//all of the car objects have the same boundary and start (respectively 500 and -50 if RIGHT, -50 and 500 if LEFT)
		if (cx>550 && dir == RIGHT){
			cx=-50;
		}
		else if (cx<-50 && dir == LEFT){
			cx=550;
		}
	}
	
	//displays the car image
	public void draw(Graphics g){
		g.drawImage(car_pic, cx, cy, null);
	}
	 
	//checks if the user had collided with the individual car object
	public boolean checkCollide(Frog user){
		//collision is based on image; they are all rectangles
		Rectangle user_area = new Rectangle(user.getFx(), user.getFy(), user.getWidth(), user.getHeight());
		Rectangle car_area = new Rectangle(cx, cy, width, height);
		//check if the image intersects with each other
		if (user_area.intersects(car_area)){
			return true;
		}
		return false;
	}
}