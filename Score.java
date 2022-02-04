//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//Score - the scorekeeping system. It assigns the respective points for
//the different tasks completed, when the respective function for each task
//is called. It also determines the validity of some the tasks completed.
class Score{
	int [] moved = new int[11];//keeps track of the number of new "blocks" the frog had moved up of
	public static final int NO = 0, YES = 1;
	
	//whenever the frog had moved towards home by one block that it had never go before
	public int moveScore(boolean collided, int y){
		//assigns point if it hadn't collided and died
		if (collided==false && moved[(y-123)/37] == NO){
			//the first block is the closest to the final point (home lily pad)
			moved[(y-123)/37]=YES; //the difference between the first block and the last block is 123, and the frog moves up by 37 at a time
			return 10; //rewards 10 points per block
		}
		else if (collided == true){
			//don't award point if it had collided
			moved[(y-123)/37]=YES;
		}
		return 0;
	}
	
	//whenever the frog reached the home lily pad, award 100 points and for every time unit remaining (200 ms), award one point extra
	//the faster the frog reached home there are more points
	public int homeScore(int time){
		return 100 + 1*time;
	}
	
	//if the frog reached home when there is a fly there award 50 extra points
	public int flyScore(){
		return 50;
	}
	
	//if the frog had collided with a ladyfrog and brought it to home w/out dying award 150 extra points
	public int ladyFrogScore(){
		return 150;
	}
	
	//for every level finished (carried 5 frogs home without gameover), award 500 extra points
	public int levelScore(){
		return 500;
	}
	
	//erase the old frog's track of movement through the blocks to make way for the new frog
	//since moveScore is awarded for every frog's new movement upwards, regardless of 
	//previous progress
	public void moveReset(){
		for (int i = 0; i< moved.length; i++){
			moved[i] = NO;
		}
	}
}