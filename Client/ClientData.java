/*Author : Naftali Davidov
 * Ver :   26/02/2025   
 
 ClientData class :
 manage the connection to the server 
 using 2 sockets 
 one for chat messaging 
 and one for user-list updating 
 */


import java.io.*;
import java.net.*;
import java.lang.Thread;
public class ClientData {
	
	//attributes 
	private Socket chatSocket = null;
	private ObjectInputStream chatInput = null;
	private ObjectOutputStream chatOutput = null;
	
	private Socket listSocket = null;
	private ObjectInputStream listInput = null;
	private ObjectOutputStream listOutput = null;
	
	private String chatServer = "localhost";
	private String nickName = "Anonymous";
	
	
	//constructor 
	public ClientData(String host , String nickName)
	{
		System.out.println("Client : started constructor ");
		this.chatServer = host;
		this.nickName = nickName;
		
		try {
			this.chatSocket = new Socket(host, 7777);
			this.chatOutput = new ObjectOutputStream(chatSocket.getOutputStream());
			this.chatOutput.flush();
			this.chatInput = new ObjectInputStream(chatSocket.getInputStream());
			
			this.listSocket = new Socket(host, 6666);
			this.listOutput = new ObjectOutputStream(listSocket.getOutputStream());
			this.listOutput.flush();
			this.listInput = new ObjectInputStream(listSocket.getInputStream());
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("Don't know about host: host");
			System.exit(1);
		} 
		catch (IOException e) 
		{
			System.out.println("Couldn't get I/O for the connection to host");
			System.exit(1);
		}
	}
	
	
	//lsiten for incoming messages from server on port 7777
	public String listenForMessage()
	{
		try
		{
			String message = (String)this.chatInput.readObject();
			return message;
		}
		catch(ClassNotFoundException c)
		{
			return c.toString();
		}
		catch (IOException e)
		{
			return e.toString();
		}
	}
	
	//sending messages to server on port 7777
	public void sendMessage(String message)
	{
		try
		{
			this.chatOutput.writeObject(message);
			this.chatOutput.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	//lsiten for incoming messages from server on port 6666
	public String listenForNickName()
	{
		try
		{
			String message = (String)this.listInput.readObject();
			return message;
		}
		catch(ClassNotFoundException c)
		{
			return c.toString();
		}
		catch (IOException e)
		{
			return e.toString();
		}
	}
	
	//sending the nickName to server on port 6666
	public void sendNameToList(String message)
	{
		try
		{
			this.listOutput.writeObject(message);
			this.listOutput.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//close the client
	public void closeClient()
	{
		try
		{
			if (chatOutput != null) chatOutput.close();
			if (chatInput != null) chatInput.close();
			if (chatSocket != null) chatSocket.close();
			
			if (listOutput != null) listOutput.close();
			if (listInput != null) listInput.close();
			if (listSocket != null) listSocket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getName()
	{
		return this.nickName;
	}
	
}



