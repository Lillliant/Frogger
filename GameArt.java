//import the neccessary libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//GameArt - deals with the drawing and animations of the objects and texts in the game.
//It'll load and draw the specific objects, texts, when the respective functions are called.
public class GameArt {
	//load the fonts and static pictures
	private Image back = new ImageIcon("graphic/background.png").getImage();
	private Font fontLocal=null;
	private Image livesPic = new ImageIcon("graphic/lives.png").getImage();
	
	public GameArt(){
		//setup the fonts
		String fName = "graphic/font.ttf";
    	InputStream is = GamePanel.class.getResourceAsStream(fName);
    	try{
    		fontLocal = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(12f);
    	}
    	catch(IOException ex){
    		System.out.println(ex);	
    	}
    	catch(FontFormatException ex){
    		System.out.println(ex);	
    	}
	}
	
	//draws the car objects one by one
	public void drawCars(Car [][] cars, Graphics g){
		for (int x=0; x<cars.length; x++){
			for (int y = 0; y<cars[x].length; y++){
				cars[x][y].draw(g);
			}
		}
	}
	
	//draws the log objects one by one
	public void drawLogs(Log [][] logs, Graphics g){
		for (int x = 0; x<logs.length; x++){
			for (int y = 0; y<logs[x].length; y++){
				logs[x][y].draw(g);
			}
		}
	}
	
	//draws the turtle objects one by one
	public void drawTurtles(Turtle [][] turtles, Graphics g){
		for (int x=0; x<turtles.length; x++){
			for (int y = 0; y<turtles[x].length; y++){
				turtles[x][y].draw(g);
			}
		}
	}
	
	//draws the background of the game
	public void paintBackground(Graphics g){
    	g.setColor(Color.black);
    	
    	//draws the black area to display lives and messages
    	g.fillRect(0,0,516,50);
    	g.fillRect(0,554,516,50);
    	
    	//displays the background image
		g.drawImage(back, 0, 50, null);
	}
	
	//displays the score, high score, time, and other text messages that needs to be routinely drawn onto the game screen
	public void drawText(String score,String highscore, Graphics g){
		//set the font and colours to be used, and then displays the messages
		g.setFont(fontLocal);
		
		g.setColor(Color.BLUE);  
    	g.drawString("SCORE",10,20);
    	
    	g.setColor(Color.WHITE);
    	g.drawString("HIGH SCORE",250,20);
    	g.drawString("TIME",145,585);
    	g.drawString(score,10,35);
    	g.drawString(highscore,250,35);
	}
	
	//draw all the home spots one by one
	public void drawHomes(FinalPoint[] homes, Graphics g){
		for (int i = 0; i<homes.length; i++){
			homes[i].draw(g);
		}
	}
	
	//draws the remaining time bar in each round
	public void drawTime(int time_length, Graphics g){
		int x;
		int y;
		int height = 20;//creates a bar that has a static height of 20, but width will change dependent on the time remaining
		g.setColor(Color.BLUE);
		g.fillRect(200,570,time_length, height);
	}
	
	//draws the number of lives the user still have within the game
	public void drawLives(int lives_num, Graphics g){
		for (int i = 0; i< lives_num; i++){
			g.drawImage(livesPic, 10+i*livesPic.getWidth(null), 570, null);
		}
	}
	
	//draws the gameover message
	public void drawGameOver(int score, Graphics g){
		//setup the font and colours
		g.setFont(fontLocal);
		
		g.setColor(Color.BLACK);  
		g.fillRect(0,0,516+50, 506+100);

		g.setColor(Color.WHITE);
		
		//draws the specific messages
		//the positions are designed so that the message is centralized
    	g.drawString("GAMEOVER",200,250);
    	g.drawString("Your score is "+score,150,270);
    	g.drawString("It was an unfroggetable journey",70,290);
    	g.drawString("See you next toad!",150,310);
	}
}
