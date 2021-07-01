import javax.swing.JFrame;

import java.io.File;    
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ClientTest {
		
    public static void main(String[] args) {
			
        Client Alfredo;
        Alfredo = new Client("127.0.0.1"); 
        /*make sure you are connected to the Servers IP adress*/
        Alfredo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Alfredo.startRunning();

        
    }    





}
