import java.io.*;
import java.net.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.security.Security;


public class Client{

    public String leeSocket(Socket p_sk, String p_Datos){

        try	{
			InputStream aux = p_sk.getInputStream();
			DataInputStream flujo = new DataInputStream( aux );
			p_Datos = flujo.readUTF();
		}catch (Exception e){
			System.out.println("Error: " + e.toString());
		}

        return p_Datos;     
    }



    public void escribeSocket(Socket p_sk, String p_Datos){

        try{
			OutputStream aux = p_sk.getOutputStream();
			DataOutputStream flujo= new DataOutputStream( aux );
			flujo.writeUTF(p_Datos);      
		}
		catch (Exception e)	{
			System.out.println("Error: " + e.toString());
		}

		return;
    }



    public int pedirNumeros(String p_operacion, int p_resultado, String p_Cadena, Socket p_Socket_Con_Servidor)	{
		int op1 = 10;
		int op2 = 10;
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);

		try	{	
			System.out.println(p_operacion);
			
			while (op1 < 0 || op1 > 9){
				System.out.print("Introduzca el primer operando [0-9]:");
				op1 = Integer.parseInt(br.readLine());
			}

			while (op2 < 0 || op2 > 9){
				System.out.print("Introduzca el segundo operando [0-9]:");
				op2 = Integer.parseInt(br.readLine());
			}							

			p_Cadena = p_operacion + "," + op1 + "," + op2; 

			escribeSocket (p_Socket_Con_Servidor, p_Cadena);
			p_Cadena = "";
			p_Cadena = leeSocket (p_Socket_Con_Servidor, p_Cadena);
		 	p_resultado = Integer.parseInt(p_Cadena);
			
		}catch(Exception e)	{
			System.out.println("Error: " + e.toString());
		}

		return p_resultado;
	}



    public void pedirOperacion(String serverName, String serverPort){
        int operacion;
		int salir = 0;
		int resultado = 0;
		char resp = 'x';

		/*
		* Descriptor del socket y buffer para datos
		*/
		String Cadena = "";
		String  op = "";
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader (isr);
			
		/*
		* Se abre la conexion con el servidor, pasando el nombre del ordenador
		* y el servicio solicitado.
		*/
		try	{
            
            //specifing the trustStore file which contains the certificate & public of the server
            System.setProperty("javax.net.ssl.trustStore","myTrustStore.jts");
            
			//specifing the password of the trustStore file
            System.setProperty("javax.net.ssl.trustStorePassword","123456");
            
			//This optional and it is just to show the dump of the details of the handshake process 
            //System.setProperty("javax.net.debug","all");


			//SSLSSocketFactory establishes the ssl context and and creates SSLSocket 
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

            //Versión cifrada
            //Create SSLSocket using SSLServerFactory already established ssl context and connect to server
            SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(serverName,Integer.parseInt(serverPort));

            //Version no cifrada
            //Socket sslSocket = new Socket(serverName,Integer.parseInt(serverPort));

			
            while (salir == 0){
				operacion = 0;

				while (operacion !=1 && operacion !=2 && operacion !=3)	{
					System.out.println("------------------------");
					System.out.println("OPERACIONES");
					System.out.println("[1] Sumar");
					System.out.println("[2] Multiplicar");
					System.out.println("------------------------");
					System.out.print("Indica la operacion a realizar: ");
					operacion = Integer.parseInt(br.readLine());
				}

				switch(operacion){
					case 1: op="suma";
					break;

					case 2: op="mult";
					break;
				}

				resultado = pedirNumeros(op, resultado, Cadena, sslSocket);
				resp='x';

				while(resp != 's' && resp != 'n'){
					System.out.println("El resultado es: " + resultado);
					System.out.println("------------------------");
					System.out.print("Desea realizar otra operacion? [s,n]: ");
					resp = br.readLine().charAt(0);					
				}

				if (resp != 's'){
					salir = 1;
					/*
					* Se cierra el socket con el servidor
					*/
					
					escribeSocket (sslSocket, "fin");
					
					Cadena = leeSocket (sslSocket, Cadena);
					resultado = Integer.parseInt(Cadena);
		 			
					if (resultado == -1){
						sslSocket.close();
						System.out.println("Conexion cerrada.");
						System.exit(0);	
					}
				}
				Cadena = "";
				op = "";
			}

		}catch(ConnectException e){
			System.out.println("El servidor no está disponible. Intentelo mas tarde");
			System.out.println("------------------------");
		}
		catch(Exception e)	{
			System.out.println("Error: " + e.toString());
		}

		return;
    }

    public void menu(String serverName, String serverPort){
        int opc = 0;
		try	{
			
			while (opc!=1 && opc!=2) {
				System.out.println("OPCIONES");
				System.out.println("[1] Realizar operacion");
				System.out.println("[2] Salir");
				System.out.println("------------------------");
				System.out.print("Indique la opcion a realizar:");
		
					
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader (isr);

				opc = Integer.parseInt(br.readLine());
			}

			if (opc == 1)
				pedirOperacion(serverName,serverPort);
			
			else System.exit(0);
			
		}catch(Exception e)	{
			System.out.println("Error " + e.toString());
		}

		return;
    }


    public static void main(String args[])  {
        Client cl = new Client();

        String serverName;
		String serverPort;

		if (args.length < 2) {
			System.out.println ("Debe indicar la direccion del servidor y el puerto");
			System.out.println ("$./Cliente nombre_servidor puerto_servidor");
			System.exit(-1);
		}

		serverName = args[0];
		serverPort = args[1];

		while(true)	{
			cl.menu(serverName,serverPort);
		}
    }
}
