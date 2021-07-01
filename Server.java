import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;    
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Server extends JFrame {

    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
	SoundEffect se = new SoundEffect();
    String clickSound;




    //constructor
    public Server(){
        super("Alfredos instant Messenger App");




		clickSound = ".//Server//Notification.wav";

        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");

                    se.setFile(clickSound);
                    se.play();			

                    
                }


            }


        );
        add(userText , BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(500 , 250);
        setVisible(true);
    }

    //Where we will set up and run the server
    public void startRunning(){
        try{
            server = new ServerSocket(43612 , 100);
            while(true){
                try{
                    // where we will connect and have conversation
                    waitForConnection();
                    setupStreams();
                    whileChatting();



                }catch(EOFException eofException){
                    showMessage("\n Server ended the connection!");
                }finally{
                    closeCrap();
                }
            }




        }catch(IOException ioException){
            ioException.printStackTrace();
        }

    }

	private void waitForConnection() throws IOException{
		showMessage(" Waiting for someone to connect... \n");
		connection = server.accept();
		showMessage(" Now connected to " + connection.getInetAddress().getHostName());
	}

    //Where we will get stream to send and receive the data
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
		input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Stream are now setup! \n");
    }

    //Chat conversation
	private void whileChatting() throws IOException{
		String message = "You are now connected!";
		sendMessage(message);
		ableToType(true);
		do{
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException notFoundException){
				showMessage("\n I don't know what that user sent");
			}
		}while(!message.equals("CLIENT - END"));
	}
    // will close after done chatting
    private void closeCrap(){
        showMessage("\n Closing connections... \n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();

        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }



    //send a message to the client
    private void sendMessage(String message){
        try{

            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        }catch(IOException ioException){
            chatWindow.append("\n ERROR : WAS NOT ABLE TO SEND MESSAGE");
        }

    }
    public void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        chatWindow.append(text);
                    }
                }
        );
    }

    private void ableToType(final boolean ableToType){
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run(){
                        userText.setEditable(ableToType);
                    }
                }
        );
    }



	public class SoundEffect {
		
		Clip clip;
		
		public void setFile(String soundFileName){
			
			try{
				File file = new File(soundFileName);
				AudioInputStream sound = AudioSystem.getAudioInputStream(file);	
				clip = AudioSystem.getClip();
				clip.open(sound);
			}
			catch(Exception e){
				
			}
		}
		
		public void play(){
			
			clip.setFramePosition(0);
			clip.start();
		}

	}











}