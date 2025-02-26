/*Author : Naftali Davidov
 * Ver :    26/02/2025
 
ListenThread class 
a thread that manage each client connection to other clietns 
takes a client nickName adds it to the list and update every other clients 
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;

class ListenThread extends Thread{
		private Socket socket = null;
		private ObjectInputStream input = null;
		private ObjectOutputStream output = null;
		private ArrayList<ObjectOutputStream> clientListOutStream ;
		private ArrayList<String> nameList ;
		private String nickName = "";
		

		
		
		public ListenThread(Socket socket, ArrayList<ObjectOutputStream> clientListOutStream, ArrayList<String> nameList){
			this.socket = socket;
			this.clientListOutStream = clientListOutStream;
			this.nameList = nameList;
			
			try{				
				output = new ObjectOutputStream(socket.getOutputStream());
				output.flush();
				
				clientListOutStream.add(output);
			
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
				
			System.out.println("Thread : finished settings I/O");
		}
		
		
		//adds name to list and broadcast it
		synchronized public void addNameToList() throws IOException
		{
			nameList.add(this.nickName);
			String newList = "";
			for(String name : nameList){
					newList += name + "\n";
			}
				
			for (ObjectOutputStream out : clientListOutStream) {
					out.writeObject(newList);
					out.flush();
				}
		}
		
		//removes name from list and broadcast it
		synchronized public void removeNameFromList() throws IOException
		{
			nameList.remove(this.nickName);
			String newList = "";
			for(String name : nameList){
					newList += name + "\n";
			}
				
			clientListOutStream.remove(this.output);
			for (ObjectOutputStream out : clientListOutStream) {
				out.writeObject(newList);
				out.flush();
			}
			
			
		}
		
		
		//close the thread
		public void closeThread()
		{
			try 
			{
				removeNameFromList();
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
		public void run(){
			String inputLine;
			try{
				System.out.println(" reading client name ");
				this.nickName = (String)this.input.readObject();
	
				addNameToList();
				
				//technically... blocks the thread until the client decides to exit
				inputLine = (String)this.input.readObject();
	
			}
			catch(ClassNotFoundException c)
			{
				 c.printStackTrace();
			}
			catch(IOException e){
				System.out.println("couldn't read from connection");
			}
			finally
			{
				closeThread();
			}
		}
}