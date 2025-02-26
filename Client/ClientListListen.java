/*Author : Naftali Davidov
 * Ver :   26/02/2025

 ClientListListen : listen for user-list updates 
 */

import javafx.scene.control.TextArea;
import javafx.fxml.FXML;
public class ClientListListen implements Runnable {
	@FXML
    private TextArea listDisplay;
	
	private ClientData clientData;

	
	ClientListListen(TextArea textArea ,ClientData client)
	{
		listDisplay = textArea;
		clientData = client;
	}
	
	
	@Override
    public void run() {
        try {
			String messageRecived = "";
            while (!Thread.currentThread().isInterrupted()) { // Check for interrupt signal              
                if (messageRecived != null) {
                    messageRecived = clientData.listenForNickName();
					listDisplay.setText(messageRecived);
                }
            }
        } 
		catch (Exception e) {
            if (!Thread.currentThread().isInterrupted()) {
                e.printStackTrace();
            }
        } finally {
            System.out.println("ChatListListen thread cleanup.");
        }
    }
	
	
	
	
}