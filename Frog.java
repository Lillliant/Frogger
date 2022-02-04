//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//Frog - the player object. The Frog class creates and displays the frog which will be used by the player to move and interact with
//other objects in the game. The user can control where the frog moves.
//It also keeps track of the special status (i.e. whether or not a lady frog was on its back) and 
//displays images according to the situation for which its respective function is called, for lady frog and for its "death".
//This frog is reused every time it dies and gets "resurrected".
public class Frog{
	//public instead of private because acts like a flag for other classes, like game.ready
	public boolean moveReady = true; //whether or not the user can move; during death time the frog can't move
	private int fx = 265, fy = 530; //the position of the frog (top left corner) - starts at the bottom of the game
	private int sx = 265, sy = 530; //memory position for every reset
	
	private Image[] pics; //the images for frame along X and Y axis
	private int dir = UP, pic_dir = Y; //keeps track of what direction and axis the frog is in
	private boolean ladyFrog = false; //whether if the frog has collided before with a lady frog
	
	public static final int LEFT=0, RIGHT=1, UP=2, DOWN=3, WAIT=4;
	public static final int X=0, Y=1;
	public static final int UNIT=37; //the unit the frog moves every time (One "block")
	
	private Image[] ladyFrogPics = {new ImageIcon("graphic/ladyFrog1.png").getImage(),new ImageIcon("graphic/ladyFrog.png").getImage()};
	//images for when a lady frog is on the frog
	
	private Image deathPic = new ImageIcon("graphic/death.png").getImage();
	//death pic

	//setup the image when a frog is created
	public Frog(String name){

		pics = new Image [2]; //2 axis - x and y for movement

		//loads the picture for each frame of movement
		for (int i=0; i < pics.length; i++){
			pics[X] = new ImageIcon(name+"/"+name+"1.png").getImage();
			pics[Y] = new ImageIcon(name+"/"+name+"0.png").getImage();
		}
	}
	
	//moves the frog when the user uses keyboard
	public void move(int direction){
		//left and right is on the x-axis, up and down is on the y-axis
		if(direction==LEFT){
			pic_dir=X;
			dir=LEFT;
			fx-=UNIT;
		}
		else if (direction==RIGHT){
			pic_dir=X;
			dir=RIGHT;
			fx+=UNIT;
		}
		else if (direction==UP){
			pic_dir=Y;
			dir=UP;
			fy-=UNIT;
		}
		else if (direction==DOWN){
			pic_dir=Y;
			dir=DOWN;
			fy+=UNIT;
		}
		
	}
	
	//move the frog when it is in the water zone - move with the object it is on (relative velocity)
	public void move(int direction, int unit){
		//move with the same unit (int unit) the object the frog is on
		//left and right is on the x-axis, up and down is on the y-axis
		if(direction==LEFT){
			fx-=unit;
		}
		else if (direction==RIGHT){
			fx+=unit;
		}
		else if (direction==UP){
			fy-=unit;
		}
		else if (direction==DOWN){
			fy+=unit;
		}
	}

	public void draw(Graphics g){
		//w & h, lw & lh, the respective width and height of the pictures for frog and ladyfrog, help "rotate" the frame into the right direction
		int w = pics[pic_dir].getWidth(null);
		int h = pics[pic_dir].getHeight(null);
		int lw = ladyFrogPics[pic_dir].getWidth(null);
		int lh = ladyFrogPics[pic_dir].getHeight(null);
		
		if (dir==UP){
			g.drawImage(pics[pic_dir], fx, fy, null);
			//draws lady frog only when it is present
			if (ladyFrog == true){
				g.drawImage(ladyFrogPics[pic_dir], fx, fy, null);
			}
		}
		else if (dir==RIGHT){
			g.drawImage(pics[pic_dir], fx, fy-(h-w), w, h, null); 
			//draws lady frog only when it is present
			if (ladyFrog == true){
				g.drawImage(ladyFrogPics[pic_dir], fx, fy-(lh-lw), lw, lh, null); // (h-w) and (lh-lw) to correct the difference of pos due to
				//rectangular rotation
			}
		}
		else if (dir==DOWN){
			g.drawImage(pics[pic_dir], fx, fy+h, w, -h, null);
			//draws lady frog only when it is present
			if (ladyFrog == true){
				g.drawImage(ladyFrogPics[pic_dir], fx, fy+lh, lw, -lh, null);
			}
		}
		else if (dir==LEFT){
			g.drawImage(pics[pic_dir], fx+w, fy-(h-w), -w, h, null);
			//draws lady frog only when it is present
			if (ladyFrog == true){
				g.drawImage(ladyFrogPics[pic_dir], fx+lw, fy-(lh-lw), -lw, lh, null); // (h-w) and (lh-lw) to correct the difference of pos due to
			}
		}	
	}
	
	//draws the death "animation"
	public void drawDeath(Graphics g){
		g.drawImage(deathPic, fx, fy, null);
	}
	
	//resets the frog's position - put it back to the bottom of gamescreen everytime it dies/reaches home
	public void resetPos(){
		fx = sx;
		fy = sy;
	}
	
	//GETTERS AND SETTERS
	
	//for the positions
	public int getFx(){
		return fx;
	}
	public int getFy(){
		return fy;
	}
	public void setFy(int y){
		fy = y;
	}
	public void setFx(int x){
		fx = x;
	}
	
	//for the image size information and directional information
	public int getWidth(){
		return pics[pic_dir].getWidth(null);
	}
	public int getHeight(){
		return pics[pic_dir].getHeight(null);
	}
	public int getDir(){
		return dir;
	}
	public void setDir(int direction){
		dir = direction;
		//determines what the pic orientation will be based on direction (left/right - X, up/down - Y)
		if (dir==UP || dir == DOWN){
			pic_dir = Y;
		}
		else{
			pic_dir = X;
		}
	}
	
	//deals with setting and getting the state of the ladyfrog and whether or not it's on the frog's back
	public void setLadyFrog(boolean state){
		ladyFrog = state;
	}
	public boolean getLadyFrog(){
		return ladyFrog;
	}
}
