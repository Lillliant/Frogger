//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//GameCollide - deals with the situation where the user object physically interacts/collides with another
//object in the game. Checks through the individual objects and user frog based on their image.
//If their respective image intersects with each other in th
public class GameCollide {
	public static final int LEFT=0, RIGHT=1, UP=2, DOWN=3;
	
    public GameCollide() {
    }
    
    //checks if the user touches a log. If not, and the user is not on any turtles, then the user frog will die
    //if it's in the water zone
    public boolean waterCollide(Frog user, Log [][] logs, Turtle [][] turtles){
    	//checks if the user collided with a log
    	for (int x = 0; x < logs.length; x++){
    		for (int y = 0; y < logs[x].length; y++){
    			if (logs[x][y].checkCollide(user)){
    				user.move(logs[x][y].getDir(), logs[x][y].getSpeed());//if the user is on a log the user will move with the log
    				return false;
    			}
    		}
    	}
    	//checks if the user collided with a turtle
    	for (int x = 0; x < turtles.length; x++){
    		for (int y = 0; y < turtles[x].length; y++){
    			if (turtles[x][y].checkCollide(user) && turtles[x][y].getDisappear()==false){
    				user.move(turtles[x][y].getDir(), turtles[x][y].getSpeed());//if the user is on a turtle the user will move with the turtle
    				return false;
    			}
    		}
    	}
    	return true;
    }
    
    //if the user is on the pavement and it collided with a car object, then the frog will die (by return collided = true to the gamepanel class)
    public boolean roadCollide(Frog user, Car[][] cars){
    	for (int x = 0; x < cars.length; x++){
    		for (int y = 0; y < cars[x].length; y++){
    			if (cars[x][y].checkCollide(user)){
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    //check through all the home spots to see if the user had collided with a fly.
    public boolean flyCollide(Frog user, FinalPoint[] homes){
    	//checks only the home spots because lady frog only spawn on home spots
    	for (int i = 0; i< homes.length; i++){
    		if (homes[i].checkCollide(user)){
    			if (homes[i].getFly()==true){
    				return true;//true only happens if the user is also on the same home spot (when checking collision with user is true)
    			}
    		}
    	}
    	return false;
    }
    
    //check through all the logs to see if the user had collided with a lady frog.
    public boolean LadyFrogCollide(Frog user, Log[][] logs){
    	//checks only the log because lady frog only spawn on logs
    	for (int x = 0; x < logs.length; x++){
    		for (int y = 0; y < logs[x].length; y++){
    			if (logs[x][y].checkLadyFrogCollide(user)){
    				return true;//true only happens if the user is also on the same log (when checking collision with user is true)
    			}
    		}
    	}
    	return false;
    }
}