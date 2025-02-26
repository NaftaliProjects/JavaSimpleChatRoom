/*Author : Naftali Davidov
 * 26/02/2025
 * Ver :    
 
Server
the main server part 
listen for incoming client (2 parts sockets) 
creates 2 seperated threads for each client   {chatTHread, ListThread}
 */


import java.net.*;
import java.io.*;
import java.util.ArrayList;


public class Server {
	public static void main(String[] args) throws IOException {
		boolean listening=true;
		
		//for chatThread
		ServerSocket chatSocket = null;
		ArrayList<ObjectOutputStream> clientsOutStream = new ArrayList<ObjectOutputStream>();
		
		//for ListThread
		ServerSocket usersListenSocket = null;
		ArrayList<ObjectOutputStream> clientListOutStream = new ArrayList<ObjectOutputStream>();
		ArrayList<String> nameList = new ArrayList<String>();
		
		//create server sockets 
		try {
			chatSocket = new ServerSocket(7777);
			usersListenSocket = new ServerSocket(6666);
		} 
		catch (IOException e) {
			System.err.println("Could not listen on port: 7777 or 6666");
			System.exit(1);
		}
		
		System.out.println("server ready");
		
		//create 2 sockets and 2 threads for each client 
		Socket socketForChat = null;
		Socket socketForList = null;
		while (listening){
			try {
				socketForChat = chatSocket.accept();
				System.out.println("a socket has been accepted");
				new ChatThread(socketForChat, clientsOutStream).start();
				
				socketForList = usersListenSocket.accept();
				System.out.println("a socket has been accepted");
				new ListenThread(socketForList, clientListOutStream, nameList).start();
				
			}
			catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		}
		chatSocket.close();
		usersListenSocket.close();
	}
}