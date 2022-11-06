// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String userID)
  {
    try 
    {
      client= new ChatClient(host, port, userID, this);
      
      
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      System.exit(1);
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        if( message.strip().startsWith("#")){
          command(message);
        }
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  public void command(String message) throws IOException {
    if(message.contains("#quit")) {

      System.out.println("Quitting");
      client.sendToServer("#quitting");
      client.quit();

    } else if (message.contains("#logoff")){

      System.out.println("Closing connection to server host "+ client.getHost() + " on port " + client.getPort()+ ".");
      client.sendToServer("#logoff");

    } else if (message.contains("#sethost")){

        if(client.isConnected()){
          System.out.println("Close connection to host before changing hostname");
        }
        else {
          String m = message.split("<")[1];
          m = m.split(">")[0];
          client.setHost(m);
          System.out.println("Host set to " + m + ".");
        }

    } else if (message.contains("#setport")){

      if(client.isConnected()){
        System.out.println("Close connection to host before changing port");
      }
      else {
        String m = message.split("<")[1];
        int n = Integer.parseInt(m.split(">")[0]);
        client.setPort(n);
        System.out.println("Port set to " + n + ".");
      }

    } else if (message.contains("#login")){

        if(client.isConnected()){
          System.out.println("You are already connected to server");
        }
        else{
          client.openConnection();
        }

    } else if (message.contains("#gethost")){

      System.out.println("Connected via host " + client.getHost());

    } else if (message.contains("#getport")){

      System.out.println("Connected via port " + client.getPort());

    }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host ;
    int port ;
    String loginID = null;

    try
    {
      loginID = args[0];
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      System.out.println("No loginID given");
      System.exit(1);
    }

    try
    {
      port = Integer.parseInt(args[2]);
    }
    catch (ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }

    try
    {
      host = args[1];
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost";
    }
    ClientConsole chat= new ClientConsole(host,port,loginID);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
