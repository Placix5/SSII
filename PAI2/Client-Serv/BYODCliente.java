import java.io.*;

import javax.net.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class BYODCliente {


    public BYODCliente() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {

			//Construye el socket
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			//Establece el puerto usado en la comunicación
			SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);

			// crea un PrintWriter para enviar mensaje al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream()));
            //Introducimos mensaje
			String mensaje = JOptionPane.showInputDialog(null,
                    "Introduzca su mensaje:");
            //Introducimos usuario
            String usuario = JOptionPane.showInputDialog(null,
                    "Introduzca su usuario:");
            //Introducimos contraseña        
            String contrasenya = JOptionPane.showInputDialog(null,
					"Introduzca su contrasenia:");
			output.println(mensaje); // envio del mensaje al servidor
            output.println(usuario);//Envío de la usuario al servidor
            output.println(contrasenya);//Envío de la usuario al servidor
			output.flush();
			// crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
            String respuesta = input.readLine(); // lee la respuesta del servidor
            JOptionPane.showMessageDialog(null, respuesta);
			System.out.println(respuesta); // muestra la respuesta al cliente
			output.close();
			input.close();
			socket.close();
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		// Salida de la aplicacion
		finally {
			System.exit(0);
		}
	}

	// ejecución del cliente de verificación de la integridad
	public static void main(String args[]) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		new BYODCliente();
	}
}
