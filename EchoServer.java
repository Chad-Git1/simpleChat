// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import client.ChatClient;
import ocsf.server.*;

import java.io.IOException;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client) {

    if (msg.toString().contains("#login") && client.getInfo("loginID") == null){
      String m = msg.toString().split(" ")[1];
      client.setInfo("loginID",m);
      System.out.println("A new client has connected to the server.");
      sendToAllClients(client.getId()+" has logged on");
    }
    else if (msg.toString().contains("#login") && msg.toString().length() > 8){
      System.out.println("loginID already set, closing connection to server");
      try {client.close();} catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    else if (msg.toString().contains("#login")){
      //
    }

    else if (msg.toString().contains( "#quitting")){
      System.out.println( client.getInfo("loginID") + " disconnected");
    }

    else if (msg.toString().contains("#logoff")){
      try {
        client.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    else {
      System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
      this.sendToAllClients( client.getInfo("loginID")+" > "+ msg);
    }

  }

  public void handleMessageFromServer(Object msg)
  {
    if (!msg.toString().contains("#")) {
      this.sendToAllClients("<SERVER MSG> : " + msg);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Message received: #login "+ client.getId()
            +  " from " + client.getInfo("loginID") +". "+ client.getId() +  "  has logged on.");
  }

  @Override
  protected synchronized void clientDisconnected(ConnectionToClient client) {
    System.out.println("Client "+ client.toString() +" a deconnecte au serveur.");
  }

  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {

    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
