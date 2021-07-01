import javax.swing.JFrame;

import java.io.File;    
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class ServerTest {
    public static void main(String[] args){
        Server runningServer = new Server();
        runningServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        runningServer.startRunning();
 
    }
}
