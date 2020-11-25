import java.io.*;
import java.net.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.security.Security;

public class ServerThread extends Thread {

	private Socket skCliente;

	public ServerThread(Socket p_cliente){
		this.skCliente = p_cliente;
	}
	
	/*
	* Lee datos del socket. Supone que se le pasa un buffer con hueco 
	*	suficiente para los datos. Devuelve el numero de bytes leidos o
	* 0 si se cierra fichero o -1 si hay error.
	*/
	public String leeSocket (Socket p_sk, String p_Datos)	{
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
	public void escribeSocket (Socket p_sk, String p_Datos)	{
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
	
	public int sumar(int p_a, int p_b)	{
		return p_a+p_b;
	}

	public int multiplicar(int p_a, int p_b)	{
		return p_a*p_b;
	}

	public int realizarOperacion(String p_Cadena)	{
		String[] operacion = p_Cadena.split(",");
		int res=0;
		
		if(operacion.length !=1){
			System.out.println("SRV: El operando 1 es " + operacion[1] + " y el operando 2 es " + operacion[2]);
			
			if(operacion[0].compareTo("suma")==0)
				{
					res = sumar(Integer.parseInt(operacion[1]),Integer.parseInt(operacion[2]));
					System.out.println("SRV: El resultado es: " + res);
				}

			else if(operacion[0].compareTo("mult")==0)
				{
					res = multiplicar(Integer.parseInt(operacion[1]),Integer.parseInt(operacion[2]));	
					System.out.println("SRV: El resultado es: " + res);
				}
		
			
		}
		
		else res = -1;
			

		return (res);
	}
	
	
	
    public void run() {
		int resultado=0;
		String Cadena="";
		
        try {
			while (resultado != -1)
			{
				Cadena = this.leeSocket (skCliente, Cadena);
				/*
				* Se escribe en pantalla la informacion que se ha recibido del
				* cliente
				*/
				resultado = this.realizarOperacion(Cadena);
				Cadena = "" + resultado;
				this.escribeSocket (skCliente, Cadena);						
			}
			skCliente.close();
			//System.exit(0); No se debe poner esta sentencia, porque en ese caso el primer cliente que cierra rompe el socket 
			//				  y desconecta a todos				
        }
        catch (Exception e) {
          System.out.println("Error: " + e.toString());
        }
      }
}