/*Author : Naftali Davidov
 * Ver :   26/02/2025
 
 ClientController 
 control the GUI part of the client-side chatRoom
 send and recive messages via ClientData class
 the controller is also splitted into 2 threads 
 ClientChatListen : listen for inconming chat messages 
 ClientListListen : listen for user-list updates 
 */

import javafx.fxml.FXML;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.nio.charset.StandardCharsets;
import java.lang.Thread;

public class ClientController {

    @FXML
    private TextArea chatDisplay;

	@FXML
    private TextArea usersList;


    @FXML
    private TextField textField;
	
	@FXML
    private Button exitBtn;
	
	@FXML
    private Button heartBtn;

    @FXML
    private Button smileBtn;

	
	private ClientData clientData;
	
	private String chatContent = "";
	
	private Thread listListenThread;
	private Thread chatListenThread;
	

	
	@FXML
void initialize() {
    System.out.println("controller : initialize has started");

    // Prompt the user to enter a nickname
    chatDisplay.setText("Please enter your nickname:");

    // Start with the first input step (nickname)
    textField.setOnAction(event -> handleNicknameInput());
	
	
}

void handleNicknameInput() {
    String nickname = textField.getText().trim();

    if (!nickname.isEmpty()) {
        
        // Prompt for the hostname
        chatDisplay.setText("Hello, " + nickname + "!    please enter the server hostname: (eg, 127,0,0,1 or \"localhost\")");

        textField.clear();
        textField.setOnAction(event -> handleHostnameInput(nickname));
    } else {
        chatDisplay.setText("Nickname cannot be empty. Please enter a nickname:");
    }
}

void handleHostnameInput(String nickname) {
    String hostname = textField.getText().trim();

    if (!hostname.isEmpty()) {
        System.out.println("controller: received hostname: " + hostname);

        // Initialize ClientData with the provided nickname and hostname
        clientData = new ClientData(hostname, nickname);
        System.out.println("controller: created ClientData class with nickname: " + nickname + " and hostname: " + hostname);

        // Clear the textField and update the display
        textField.clear();
        chatDisplay.setText("Connection established with server at " + hostname + ". You can now chat!");

        // Start the listening threads
        ClientChatListen chatListen = new ClientChatListen(chatDisplay, chatContent, clientData);
        chatListenThread = new Thread(chatListen);
		chatListenThread.setDaemon(true); 
        chatListenThread.start();
		
		ClientListListen listListen = new ClientListListen(usersList, clientData);
        listListenThread = new Thread(listListen);
		listListenThread.setDaemon(true);
        listListenThread.start();

        //restore normal message-sending listener
        textField.setOnAction(this::textEntered);
		
		clientData.sendMessage(clientData.getName());
		clientData.sendNameToList(clientData.getName());
    } else {
        chatDisplay.setText("Hostname cannot be empty. Please enter the server hostname:");
    }
}


    @FXML
    void textEntered(ActionEvent event) {
		clientData.sendMessage(textField.getText());
		chatContent += textField.getText() + "\n" ;
		chatDisplay.setText(chatContent);
		textField.clear();
    }
	
	@FXML
    void PressedSmile(ActionEvent event) {
		String smile = new String("☺".getBytes(), StandardCharsets.UTF_8);
		textField.setText(textField.getText() + smile);
		textField.positionCaret(textField.getText().length());
    }
	
	@FXML
    void PressedHeart(ActionEvent event) {
		String heart = new String("♥".getBytes(), StandardCharsets.UTF_8);
		textField.setText(textField.getText() + heart);
		textField.positionCaret(textField.getText().length());
    }
	
    @FXML
    void pressedExit(ActionEvent event) {
		 try 
		 {
        
			// Interrupt threads
			if (chatListenThread != null && chatListenThread.isAlive()) {
				chatListenThread.interrupt();
			}

			if (listListenThread != null && listListenThread.isAlive()) {
				listListenThread.interrupt();
			}
			
			if (clientData != null) {
				clientData.closeClient(); 
			}
	
			
			//terminate
			Platform.exit();
			System.exit(0);
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
    }

    
	
	
	
}
