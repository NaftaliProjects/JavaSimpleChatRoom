/*Author : Naftali Davidov
 * Ver :   26/02/2025
 
chatThread class 
a thread that manage each client connection to other clietns 
takes a client message and send it to every other clients 
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;

class ChatThread extends Thread{
	
		//attributes
		private Socket socket = null;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;
		private ArrayList<ObjectOutputStream> clientsOutStream;
		private String nickName = "";
		
		//constructor
		public ChatThread(Socket socket, ArrayList<ObjectOutputStream> clientsOutStream){
			this.socket = socket;
			this.clientsOutStream = clientsOutStream;
			
			try{				
				output = new ObjectOutputStream(socket.getOutputStream());
				output.flush();
				
				clientsOutStream.add(output);
				
				input = new ObjectInputStream(socket.getInputStream());
			}
			catch (UnknownHostException e) 
			{
				System.out.println("Don't know about host: host");
				System.exit(1);
			} 
			catch (IOException e) 
			{
				System.out.println("Couldn't get I/O for the connection to host ");
				System.exit(1);
			}
		}

		//broadcast a message
		synchronized public void sendMessage(String inputLine) throws IOException 
		{
			for (ObjectOutputStream out : clientsOutStream) {
				out.writeObject(this.nickName + ": " + inputLine);
				out.flush();
			}
		}
		
		// broadcast nickName from the client
		synchronized public void clientEnteredChat() throws IOException
		{
			for (ObjectOutputStream out : clientsOutStream) {
				out.writeObject(this.nickName + " has joined the chat! Everyone say hi :D");
				out.flush();
			}
		}
		
		//upadte everyone that you have left
		synchronized public void clientLeftChat() throws IOException{
			clientsOutStream.remove(this.output); 
			for (ObjectOutputStream out : clientsOutStream) {
				out.writeObject(this.nickName + " has left the chat! Everyone say bye :(");
				out.flush();
			}
		}
		
		//close the thread
		public void closeThread()
		{
			try 
			{
				clientLeftChat();
			} 
			catch (IOException e) {
				System.out.println("Error notifying clients of disconnection.");
			} 
			finally {
				try 
				{
					if (this.input != null) this.input.close();
					if (this.output != null) this.output.close();
					if (this.socket != null) this.socket.close();
				} 
				catch (IOException e) {
					System.out.println("Error closing client resources.");
				}
			}
		}
		
		//runnable thread
		public void run() {
			String inputLine;
			try {
				// Receive and broadcast the new client's nickname
				this.nickName = (String) this.input.readObject();
				this.clientEnteredChat();
				
				while ((inputLine = (String) this.input.readObject()) != null) {
					this.sendMessage(inputLine);
				}
				
			} 
			catch (ClassNotFoundException e) {
				System.err.println("Invalid data received from client.");
			} 
			catch (IOException e) {
				System.out.println(this.nickName + " disconnected unexpectedly.");
			} 
			finally {
				closeThread();
			}
		}

}