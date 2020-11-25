//@author: ocl5
import java.io.*;
import java.net.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Security;

public class ConcurrentServer {

	/**
	 * @param args
	 */
	@SuppressWarnings("resource")

	public static void main(String[] args) {
        //The Port number through which this server will accept client connections
        String port;

        Server sr = new Server();

        if (args.length < 1) {
            System.out.println("Debe indicar el puerto de escucha del servidor");
            System.out.println("$./Servidor puerto_servidor");
            System.exit (1);
        }
        port = args[0];

        try
        {
            /*Adding the JSSE (Java Secure Socket Extension) provider which provides SSL and TLS protocols
            and includes functionality for data encryption, server authentication, message integrity, 
            and optional client authentication.*/
            
            //specifing the keystore file which contains the certificate/public key and the private key
            System.setProperty("javax.net.ssl.keyStore","myKeyStore.jks");
            //specifing the password of the keystore file
            System.setProperty("javax.net.ssl.keyStorePassword","123456");
            //This optional and it is just to show the dump of the details of the handshake process 
            //System.setProperty("javax.net.debug","all");

            //SSLServerSocketFactory establishes the ssl context and and creates SSLServerSocket 
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            
            //Versión cifrada
            //Create SSLServerSocket using SSLServerSocketFactory established ssl context
            SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(Integer.parseInt(port));

            //Versión no cifrada
            //ServerSocket sslServerSocket = new ServerSocket(Integer.parseInt(port));

            System.out.println("Echo Server Started & Ready to accept Client Connection");

            String Cadena="";

		
			for(;;)
			{
                //Versión cifrada
                //Wait for the SSL client to connect to this server
                SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
                
                //Versión no cifrada
                //Socket sslSocket = sslServerSocket.accept();
				
                System.out.println("Sirviendo cliente...");

		        Thread t = new ServerThread(sslSocket);
		        t.start();
			}
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}

	}

}