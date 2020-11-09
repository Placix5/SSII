import java.io.*;

import javax.net.*;
import javax.swing.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class IntegrityVerifierClient {

    public IntegrityVerifierClient() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory
					.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost",
					7070);
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			String mensaje = JOptionPane.showInputDialog(null,
					"Introduzca su mensaje:");
			output.println(mensaje); // envio del mensaje al servidor
            String key=")SG]t09{";//Key Compartida
			String macdelMensaje = MACGen.calculaMac(mensaje, key);
			output.println(macdelMensaje);//Envío de la MAC al servidor
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
		new IntegrityVerifierClient();
	}
}
