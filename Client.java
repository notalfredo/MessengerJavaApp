import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.File;    
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Client extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
	SoundEffect se = new SoundEffect();
    String clickSound;


    //constructor

    public Client(String host){
        super("Client Side!");
        clickSound = ".//Server//Notification.wav";
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){

                    sendData(event.getActionCommand());
                    userText.setText("");
                    se.setFile(clickSound);
                    se.play();	

                    
                }
            }

        );
        add(userText , BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow) , BorderLayout.CENTER);
        setSize(500 , 250);
        setVisible(true);

    }

	//connect to server
	public void startRunning(){
		try{
			connectToServer();
			setupStreams();
			whileChatting();
		}catch(EOFException eof){
			showMessage("\n Client terminated the connection");
		}catch(IOException io){
			System.out.println("start running");
			io.printStackTrace();
		}finally{
			closeStreams();
		}
	}

	//connect to server
	private void connectToServer() throws IOException{
		showMessage("Attempting connection...");
		connection = new Socket(InetAddress.getByName(serverIP), 43612);     //6789 was the same number server used for server.java
		showMessage("Connected to: " + connection.getInetAddress().getHostName());   //This will show the server IP adress
	}
	
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are now set up! \n");
	}


	public void whileChatting() throws IOException{
		ableToType(true);
		do{
			
			try{
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch(ClassNotFoundException e){
				showMessage("\n I don't know that text type is");
			}
			
		}while(!message.equalsIgnoreCase("SERVER - END"));
	}


    public void closeStreams(){
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
    
    //send messages to server
    private void sendData(String text){
        try{
            output.writeObject("CLIENT - " + text);  //Will send out the message
            output.flush();
            showMessage("\nCLIENT - " + text);   //Shows the message in the text area box
        }catch(IOException exception){
            chatWindow.append("\n ERROR: you cannot send that message");
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