//import libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import javax.sound.midi.*;

//GameSound deals with the short- and long-duration music within the game. It loads the different sound files
//required in the constructor, and also starts the background music (long-duration) when constructor is call.
//It will also play the different specified sound files when the corresponding function is called.
public class GameSound {
	//object for the background music
	private static Sequencer midiPlayer;
	
	//setup the different short-term sound objects and their pathways
	File deathFile = new File("graphic/death.wav");
	File jumpFile = new File("graphic/jump2.wav");
  	AudioClip deathSound;
  	AudioClip jumpSound;
	
	//load the music
    public GameSound() {
    	try{
    		deathSound = Applet.newAudioClip(deathFile.toURL());
    		jumpSound = Applet.newAudioClip(jumpFile.toURL());
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	startMidi("graphic/Lullaby.mid");     //load the file and start the midi player
    }
    
   //load, activate and play the music 
   public static void startMidi(String midFilename) {
      try {
         File midiFile = new File(midFilename);
         Sequence song = MidiSystem.getSequence(midiFile); //get the music data from midiFile
         midiPlayer = MidiSystem.getSequencer();
         midiPlayer.open();
         midiPlayer.setSequence(song);
         midiPlayer.setLoopCount(-1); //repeat the music infinitely
         midiPlayer.start();
      } catch (MidiUnavailableException e) {
         e.printStackTrace();
      } catch (InvalidMidiDataException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   	}	
    
    //sound for when the frog is jumping/moving
    public void playJump(){
    	jumpSound.play();
    }
    
    //sound for when the frog dies
    public void playDeath(){
    	deathSound.play();
    }
}