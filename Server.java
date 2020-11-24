import java.io.*;
import java.net.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Security;


public class Server 
{

    /*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (SSLSocket p_sk, String p_Datos){
		try
		{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = new String();
			p_Datos = flujo.readUTF();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
      return p_Datos;
	}



	/*
	* Escribe dato en el socket cliente. Devuelve numero de bytes escritos,
	* o -1 si hay error.
	*/
	public void escribeSocket (SSLSocket p_sk, String p_Datos){
		try
		{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.toString());
		}
		return;
	}
	


	public int sumar(int p_a, int p_b){
		return p_a+p_b;
	}


	public int multiplicar(int p_a, int p_b){
		return p_a*p_b;
	}


    public int realizarOperacion(String p_Cadena){
		String[] operacion = p_Cadena.split(",");
		int res=0;
		
		System.out.println("SRV: La operacion es: " + operacion[0]);
		if(operacion.length != 1)
		{
			System.out.println("SRV: El operando 1 es " + operacion[1] + " y el operando 2 es " + operacion[2]);
			if(operacion[0].compareTo("suma")==0)
			{
				res = sumar(Integer.parseInt(operacion[1]),Integer.parseInt(operacion[2]));
			}
			else
			{
				if(operacion[0].compareTo("mult")==0)
				{
					res = multiplicar(Integer.parseInt(operacion[1]),Integer.parseInt(operacion[2]));	
				}	
				else
				{
					
					res = -1;
				}
			}
			System.out.println("SRV: El resultado es: " + res);
		}else
		{
			res = -1;
		}	
		return (res);
    }

    public static void main(String args[])
    {
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
            //ServerSocket sslServerSocket = new ServerSocket(port);

            System.out.println("Echo Server Started & Ready to accept Client Connection");

            String Cadena="";

            //Mantenemos la comunicación con el cliente
            for(;;){
                //Versión cifrada
                //Wait for the SSL client to connect to this server
                SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();
                
                //Versión no cifrada
                //Socket sslSocket = sslServerSocket.accept();

                System.out.println("Sirviendo cliente...");

                int resultado = 0;
                while (resultado != -1){
                    Cadena = sr.leeSocket (sslSocket, Cadena);
					/*
					* Se escribe en pantalla la informacion que se ha recibido del
					* cliente
					*/
					resultado = sr.realizarOperacion(Cadena);
					Cadena = Integer.toString(resultado);
					sr.escribeSocket (sslSocket, Cadena);						
                }
                sslSocket.close();
				System.exit(0);
            }            
            
        }
        catch(Exception ex)
        {
            System.err.println("Error Happened : "+ex.toString());
        }
    }
}
