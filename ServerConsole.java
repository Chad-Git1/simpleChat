// This file contains material supporting section 3.7 of the textbook:
// "Object-Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import java.util.Scanner;

import client.*;
import ocsf.server.*;
import common.*;

/**
 * This class constructs the UI for a server client. It implements the
 * chat interface in order to activate the display() method.
 * Some code is cloned from ClientConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF
{
    //Class variables *************************************************

    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    //Instance variables **********************************************

    /**
     * The instance of the server that created this Console.
     */
    EchoServer server;



    /**
     * Scanner to read from the console
     */
    Scanner fromConsole;


    //Constructors ****************************************************

    /**
     * Constructs an instance of the ServerConsole UI.
     *
     * @param port The port to connect on.
     */
    public ServerConsole(int port)
    {
        server = new EchoServer(port);


        // Create scanner object to read from console
        fromConsole = new Scanner(System.in);
    }


    //Instance methods ************************************************

    /**
     * This method waits for input from the console.  Once it is
     * received, it sends it to the server's message handler.
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
                server.handleMessageFromServer(message);
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

            System.out.println("Shutting down server.");
            server.sendToAllClients("The server has shut down");
            server.stopListening();
            server.close();

        } else if (message.contains("#stop")){

            server.stopListening();

        } else if (message.contains("#close")){

            System.out.println("Closing server.");
            server.sendToAllClients("The server has shut down");
            server.close();

        } else if (message.contains("#setport")){

            String m = message.split(" ")[1] ;
            if ( server.isListening() ) {
                System.out.println("Port set to " + m + ". Change will be affected once the server is close");
            }
            else {
                System.out.println("Port set to " + m + ".");
                server.setPort(Integer.parseInt(m));
            }

        } else if (message.contains("#start")){

            server.listen();

        } else if (message.contains("#getport")){

            System.out.println(server.getPort());

        }
    }

    //Class methods ***************************************************

    /**
     * This method is responsible for the creation of the Server UI.
     *
     */
    public static void main(String[] args)
    {
        String host;
        int port;

        try
        {
            port = Integer.parseInt(args[1]);
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            port = DEFAULT_PORT;
        }

        try
        {
            host = args[0];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            host = "localhost";
        }
        ServerConsole admin = new ServerConsole(port);
        admin.accept();  //Wait for console data
    }
}
//End of ConsoleChat class
