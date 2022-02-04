//Frogger.java
//Christine Wong
//Frogger is a game which the user controls a frog to cross obstacles to reach their home destinations (home spots). The
//obstacles include the cars and water, and if the user frog collided with them, the frog will die. In the beginning of the game
//users have 3 lives, and after that the game over message will play that displays the user's score, then the game will exit.
//The game continues infinitely in levels - if the user manages to fill all 5 home spots then a level is cleared. The player
//will then be awarded 2 extra lives and extra points.
//For each frog there is also a time limit for them to reach their home spots: if they failed to do so before time runs out then the 
//players loses one life.
//Depending on how fast the player manages to reach the home spot, extra point is awarded. For each new block moved closer to 
//the home spot per frog the user is also awarded 10 points.
//There are also random events for user to gain bonus points - if there are flies and lady frog that the user manage to collide with
//and reach to home spot with without dying, then the player will be awarded 50 and 150 points respectively for fly and lady frog.

//import the libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.MouseInfo;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Scanner;

//Frogger Class - sets up the main frame of the pop-up window and host the main game. Control how fast the GamePanel updates itself.
//Also checks the gameover status of the game, and control when the game will automatically quit itself
public class Frogger extends JFrame{
    Timer myTimer; //keep track of how fast the game updates itself
	GamePanel game; //the main game object
	
	//setup the objects
    public Frogger() {
		super("Frogger");
		
		myTimer = new Timer(10, new TickListener()); //triggers every 100 ms
		game = new GamePanel(this);
		add(game);
		
		//setup the frame so that contents in the panel maintain their size across computers
		pack();
		
		//setup the settings of the windows
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true); //show the windows
    }

    class TickListener implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			//update game informations
			if(game!= null){
				//game.ready is when there is still more frogs to play with
				if (game.ready){
					game.move();
					game.check();
				}
				//doesn't get grouped in so it can display the gameover message
				game.repaint();
			}
			//game will display gameover message and write down possible high score for future reference
			//when there is no more frogs
			if (game.getLives()<0){
				game.ready = false; //no more frogs, and therefore don't continue the game
				//if the game score is the new highscore, store it in the data file
				if (game.getHighScore()<game.getScore()){
					game.writeHighScore(""+game.getScore());
				}
			}
			
			//exits the frame and windows if game over message display is complete
			if (game.quit == true){
				System.exit(0);
			}
		}
	}
	
	//start the timer
	public void start(){
		myTimer.start();
	}
	
	//the main method where the main game is called
    public static void main(String[] arguments) {
		Frogger frame = new Frogger();

    }
}

//GamePanel Class - where all the game interactions occur and the central hub for interactions to join.
//Moves the game objects and user objects, and monitor their interactions.
//Checks when the user collides with objects that will gain special effects/point bonuses, and when the game should exit and display
//game over messages. Keeps track of the high score of all time and user score for this run of the game. Keeps track of and create the
//random events that are happening in the games.
class GamePanel extends JPanel{
	public boolean ready=false;//if the game is still ready to be continued
	public boolean quit=false; //if the game/program is still running
	private boolean [] keys; //keep tracks of the keyboard input
	
	private Frogger mainFrame;
	//the different objects to keep track of the progresses, displays, and scores happening in game
	GameArt gameArt = new GameArt();
	GameCollide gameCollide = new GameCollide();
	Score scorekeeper = new Score();
	private GameSound gameSound;
	
	Timer countdown;
	Timer random_events;
	
	private int sec = 300; //how many 200 ms the user has before the frog for this round dies (40 seconds in total)
	private int lives = 3; //start with 3 lives and no score
	private int score = 0;
	private int highscore = readHighScore();

	//"map" of important positions for the game to take reference of
	private int water_boundY = 50+63;
	private int water_length = 185;
	private int crossroadY = water_boundY+water_length;
	private int crossroad_length = 37;
	private int pavementY = crossroadY+crossroad_length;
	private int pavement_length = 184;
	private int startroadY = pavementY+pavement_length;
	private int startroad_length = 37;
	public static final int LEFT=0, RIGHT=1, UP=2, DOWN=3;
		
	//different game objects
	//make a series of objects used in the game
	private Frog user = new Frog("frog");//represents the player
	private Car [][] cars = new Car[5][3];
	private Log [][] logs = new Log[3][3];
	private Turtle [][] turtles = new Turtle [2][4];
	private FinalPoint [] homes = new FinalPoint[5];
	
	private boolean death = false; //if the frog/user died
	private int countDeath = 0; //number of loops the user is at during the play of death animation
	private int overcount = 0; //number of loops for game over message
    private int overdelay = 300; //max number of loops for the game over message before program exits

	public GamePanel(Frogger m){
		//setup interaction and display objects in the game
		gameSound = new GameSound();
		keys = new boolean[KeyEvent.KEY_LAST+1];
		mainFrame = m;
		setPreferredSize(new Dimension(516, 604));
		addKeyListener(new moveListener());
		
		//setup game objects
		setupCars();
		setupLogs();
		setupTurtles();
		setupHomes();
    	
    	//setup and start the timers for randomly triggered events and countdown
    	countdown = new Timer(200, new CountDown());//triggers every 200 ms
    	random_events = new Timer(500, new Chance());//triggers every half a second
    	countdown.start();
    	random_events.start();
	}
	
	public void addNotify() {
        super.addNotify();
        setFocusable(true);
        requestFocus();
        mainFrame.start();
        ready = true;
    }
	
	//GETTERS AND SETTERS
	public int getLives(){
		return lives;
	}
	public int getScore(){
		return score;
	}
	public int getHighScore(){
		return highscore;
	}
	
	//UTILITY METHOD
	//returns a randomly generated integer within a range which its high is included
	public static int randint(int low, int high){
	    return (int)(Math.random()*(high-low+1)+low);
	}
	
	//constrain a number within a range
	private int constrain(int x, int low, int high){
		if (x<low){
			return low;
		}
		else if (x>high){
			return high;
		}
		return x;
	}
	
	//setup the countdown timer
	class CountDown implements ActionListener{
		public void actionPerformed(ActionEvent evt){
			if (sec>-1){//<-1 so the display of time actually reaches 0
				sec--;
			}
			if (sec<0){
				death = true; //frog will die if it didn't do anything to change sec before it reaches 0
				sec = 300;
			}
		}
	}
	
	//generate random_events
	class Chance implements ActionListener{
		int TRUE = 25;
		int FALSE = 0;
		private String [] random_events = {"flies","lady frog"};
		public void actionPerformed(ActionEvent evt){
			if (randint(FALSE, TRUE)==TRUE){ //1 in 25 chance of spawning
				String event = random_events[randint(0, random_events.length-1)];
				setupEvent(event);//only setup the events when it is sure there is an event spawned
			}
		}
	}
	
	//setup the random events if it spawned
	public void setupEvent(String event){
		//only one event is setup at a time
		if (event.equals("flies")){
			int i = randint(0, homes.length-1);
			while (homes[i].getReached()==true){ //makes sure it doesn't spawn on home spots that are already reached
				i = randint(0, homes.length-1);
			}
			homes[i].setFly(true);
		}
		else if (event.equals("lady frog")){
			int x = randint(0, logs.length-1);
			int y = randint(0, logs[x].length-1);
			logs[x][y].setLadyFrog(); //setup lady frog on a random log
			
		}
	}
	
	//setup the large number of cars in an array organized by rows in position in Y
	public void setupCars(){
		int LEFT=-1;
		int dir = LEFT;
		int [] speed = {1,1,1,1,2};
		for (int x=0; x<cars.length; x++){
			for (int y = 0; y<cars[x].length; y++){ //max speed one can get is 2
				cars[x][y] = new Car (y*200, x*37+332, speed[4-x] , dir, ""+x); //+332 to keep distance between objects
			}
			dir*=LEFT; //the left of left is right; alternate between LEFT (-1) and RIGHT (1)
		}
	}

	//setup the large number of logs in an array organized by rows in position in Y
	public void setupLogs(){
		//each row all have different stats
		int [] pos = {112, 186, 223}; //for y-axis; needs an array because there is a space between the objects' rows
		int [] bound = {664,926,574};
		int [] start = {-182, -283, -137}; //have different bound and start to avoid collision due to different log length
		int [] dist = {282, 383, 237}; //the distance from the start of one object to the next
		int [] speed = {1,2,1};
		
		for (int x = 0; x<logs.length; x++){
			for (int y = 0; y<logs[x].length; y++){
				logs[x][y] = new Log (start[x]+y*dist[x], pos[x],speed[x],bound[x],start[x], ""+x);
			}
		}
	}
	
	//setup the large number of home spots in an array organized by rows in position in Y
	public void setupHomes(){
		for (int i = 0; i<homes.length; i++){
			homes[i] = new FinalPoint(6+i*63+i*48, 50+15, 63, 48); //48 & 63 is the distance between the start of one home spot to the next
		}
	}

	//setup the large number of turtles in an array organized by rows in position in Y
	public void setupTurtles(){
		boolean animation = false;
		int [] pos_y = {149, 260}; //needs an array because there is a space between the objects' rows
		for (int x = 0; x<turtles.length; x++){
			for (int y=0; y<turtles[x].length; y++){
				turtles[x][y] = new Turtle(-253+y*253, pos_y[x], 2-x, -253, 759, animation, "0");
				animation = !animation; //every other object has animation enabled
			}
		}
	}

	//move all the individual cars in the car array
	public void moveCars(){
		for (int x=0; x<cars.length; x++){
			for (int y = 0; y<cars[x].length; y++){
				cars[x][y].move();
			}
		}
	}
	
	//move all the individual turtles in the turtle array
	public void moveTurtles(){
		for (int x=0; x<turtles.length; x++){
			for (int y = 0; y<turtles[x].length; y++){
				turtles[x][y].move();
			}
		}
	}

	//move all the individual logs in the log array
	public void moveLogs(){
		for (int x = 0; x<logs.length; x++){
			for (int y = 0; y<logs[x].length; y++){
				logs[x][y].move();
			}
		}
	}

	//check if there is anything that would result in the death of a frog
	public boolean checkDeathCollision(){
		boolean collided; //if there is a collision/anti-collision that'll result in death
		//check specific collision based on frog's y position
    	if (user.getFy()>pavementY && user.getFy()<startroadY){
    		collided = gameCollide.roadCollide(user, cars);//check for car collision in pavement
    		if (collided==false && death == false){//makes sure the frog doesn't get point if the original collision source moved away
    			score+=scorekeeper.moveScore(collided, user.getFy());//reward if there is no death
    		}
    		return collided;
    	}
    	else if (user.getFy()>crossroadY && user.getFy()<pavementY){
    		score+=scorekeeper.moveScore(false, user.getFy());//reward point w/out condition because there can't be collision
    	}
    	else if (user.getFy()>water_boundY && user.getFy()<crossroadY){//in water zone, if collided with water results in death
    		collided = gameCollide.waterCollide(user, logs, turtles);
    		if (collided==false && death == false){//makes sure the frog doesn't get point if the original collision source moved away
    			score+=scorekeeper.moveScore(collided, user.getFy());//reward if there is no death
    		}
    		return collided;
    	}
    	return false;
    }
    
    //checks for all interactions in the game
    public void checkInteraction(){
    	if (checkDeathCollision()){//if there are anything that'll result in death from collision happened, the frog is dead
    		death = true;
    	}
    	else if (user.getFy()>65 && user.getFy()<water_boundY){//checks if the frog reach the home spot
    		boolean checkReached = false;
    		for (int i = 0; i< homes.length; i++){
    			if (homes[i].checkCollide(user)){//if the home spot is free and collided with user
    				score+=scorekeeper.homeScore(sec);//automatic point for reaching there
    				homes[i].setReached(true);
    				checkReached = true;
	    			if (user.getLadyFrog()){//award extra point & remove lady frog if the frog has lady frog
	    				score+=scorekeeper.ladyFrogScore();
	    				user.setLadyFrog(false);
	    			}
	    			if (homes[i].getFly()){//award extra point & remove fly if the frog collided with a fly
    					score+=scorekeeper.flyScore();
    					homes[i].setFly(false);
	    			}
    				scorekeeper.moveReset();//reset for the new frog in score availability, position, and time remaining
    				user.resetPos();
    				sec = 300;
    			}
    		}
    		if (checkReached == false){
    			death = true;//kills the frog if it is out of bound but didn't reach a homespot
    		}
    	}
    	else if (user.getFy()>crossroadY && user.getFy()<pavementY){
    		if (user.getFx()<0 || user.getFx()>516){
    			death = true;//kills the frog if the user gets carried out of bound by the log and turtles objects
    		}
    	}
    }
    
    //check if a level if finished (all 5 spots filled)
    public boolean checkFinished(){
    	for (int i = 0; i<homes.length; i++){
    		if (homes[i].getReached()==false){
    			return false;
    		}
    	}
    	return true;
    }
    
    //if the level is finished
    public void checkFinishedLevel(){
    	if (checkFinished()){
    		score+=scorekeeper.levelScore();//award extra points for staying alive
    		lives+=2; //award 2 lives
    		sec=300; //reset time
    		user.resetPos(); //reset the home objects and user positon & score availability for the frog to continue playing
    		scorekeeper.moveReset();
    		resetHomes();
    	}
    }
    
    //remove all frogs from the previously filled spots when level is completed
    public void resetHomes(){
    	for (int i = 0; i<homes.length; i++){
    		homes[i].reset();
    	}
    }
    
    //check if the user collided with the randomly spawned ladyfrog
    public void checkRandomEvents(){
    	if (gameCollide.LadyFrogCollide(user, logs)){
    		user.setLadyFrog(true);
    	}
    }
	
	//move all the non-player objects in game
	public void move(){
		moveCars();
		moveLogs();
		moveTurtles();
		repaint();
	}
	
	//check for every possible interaction and deaths in the game
	public void check(){
		checkFinishedLevel();
		checkInteraction();
		checkRandomEvents();
		if (death == true){
			user.moveReady = false;//user can move when it's dead
			gameSound.playDeath();//alert user that frog is dead
		}
	}
	
	//read the high score from datafile
	public static int readHighScore(){
		String fileName = "graphic/highscore.txt";
		try{
			Scanner inFile = new Scanner(new BufferedReader(new FileReader(fileName)));
			int highscore = Integer.parseInt(inFile.nextLine());
			inFile.close();
			return highscore;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	//clears and writes the new high score into the datafile
	public void writeHighScore(String score){
		String fileName = "graphic/highscore.txt";
		try{
			PrintWriter outFile = new PrintWriter(new BufferedWriter (new FileWriter (fileName)));
			outFile.print(score);
			outFile.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//for when the user moves the frog using keypad
    class moveListener implements KeyListener{
	    public void keyTyped(KeyEvent e) {}

	    public void keyPressed(KeyEvent e) {
	    	//the frog must be not dead for the user to control it
			if(e.getKeyCode() == KeyEvent.VK_RIGHT && user.moveReady){
				user.move(RIGHT);
				gameSound.playJump();//play audio to alert user
			}
			else if(e.getKeyCode()==KeyEvent.VK_LEFT && user.moveReady){
				user.move(LEFT);
				gameSound.playJump();//play audio to alert user
			}
			else if(e.getKeyCode()==KeyEvent.VK_UP && user.moveReady){
				user.move(UP);
				gameSound.playJump();//play audio to alert user
			}
			else if(e.getKeyCode()==KeyEvent.VK_DOWN && user.moveReady){
				user.move(DOWN);
				gameSound.playJump();//play audio to alert user
			}
			
			user.setFy(constrain(user.getFy(), 65, 530));
			
			if (user.getFy()>crossroadY && user.getFy()<startroadY+startroad_length){
				user.setFx(constrain(user.getFx(), 6, 494));
    		}

	    }

	    public void keyReleased(KeyEvent e) {
	    }
	    
    }
    
    //draws everything that can be displayed in the game
    public void paintComponent(Graphics g){
    	//draws background, objects, and other visual elements
    	gameArt.paintBackground(g);
    	gameArt.drawCars(cars, g);
    	gameArt.drawLogs(logs, g);
    	gameArt.drawTurtles(turtles, g);
    	gameArt.drawText(""+score,""+highscore, g);
    	gameArt.drawTime(sec, g);
    	gameArt.drawLives(lives, g);
    	gameArt.drawHomes(homes, g);
    	//special animations
    	if (death == true){
    		countDeath++;//update the time the animation had played
    		user.drawDeath(g);
    		if (countDeath==100){//animation lasts for 100 loops before the game resumes
    			finishDeath();
    		}
    	}
    	else if (death == false){
    		user.draw(g);//death and user frog can't co-exist
    	}
    	if (ready == false){//draws the game over message
    		overcount++;//updated the time the message had played
    		gameArt.drawGameOver(score, g);
    		if (overcount == overdelay){//the message lasts for a certain num of loops before the program exits
    			overcount = 0;
    			quit = true;//program will exit
    		}
    	}
    }
    
    //reset everything for the new frog after the death animation stops playing
    private void finishDeath(){
    	countDeath = 0; //reset for new animation
    	death = false;
    	
    	lives--;
    	user.moveReady = true; //user can now move
    	user.resetPos();
    	user.setDir(UP);
    	
    	scorekeeper.moveReset();
    	sec = 300; //reset time for new frog
    }
}