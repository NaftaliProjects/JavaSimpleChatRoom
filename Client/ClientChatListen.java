/*Author : Naftali Davidov
 * Ver :    26/02/2025
 
 ClientChatListen : listen for chat incoming messages
 */

import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
public class ClientChatListen implements Runnable {
	@FXML
    private TextArea chatDisplay;

	private ClientData clientData;
	private String chatContent = "";
	
	
	
	ClientChatListen(TextArea textArea, String content ,ClientData client)
	{
		chatDisplay = textArea;
		clientData = client;
		chatContent = content;
	}
	
	
	
	@Override
    public void run() {
        try {
			String messageRecived = "";
            while (!Thread.currentThread().isInterrupted()) { // Check for interrupt signal
                System.out.println("trying to recive message from server : ");
				if(messageRecived != null)
				{
					messageRecived = clientData.listenForMessage();
					chatContent += messageRecived + "\n";
					chatDisplay.setText(chatContent);
				}
            }
        } 
		catch (Exception e) {
            if (!Thread.currentThread().isInterrupted()) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("ChatListen thread cleanup.");
        }
    }
	
	
}