/*Author : Naftali Davidov
 * Ver :   26/02/2025
 * 
 * client main program for the client GUI
 */
 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Client extends Application{
	@Override
	public void start(Stage stage) throws Exception{
	Parent root = FXMLLoader.load(getClass().getResource("Client.fxml"));
	
	Scene scene = new Scene(root);
	stage.setTitle("Client");
	stage.setScene(scene);
	stage.show();
	
}
public static void main(String[] args) {
	
	launch(args);
	
		
	}
}