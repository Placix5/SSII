import java.io.*;
import java.net.*;

import javax.net.*;

public class IntegrityVerifierServer {

	private ServerSocket serverSocket;

	// Constructor
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory
				.getDefault();
		// creación de un objeto ServerSocket escuchando peticiones en el puerto
		// 7071
		serverSocket = (ServerSocket) socketFactory.createServerSocket(7071);
	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.print("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				// abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				// abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				// se lee del cliente el mensaje y el mac del MensajeEnviado
				String mensaje = input.readLine();
				String macdelMensajeEnviado = input.readLine();
				// a continuación habría que calcular el mac del MensajeEnviado
				// que podría ser //macdelMensajeCalculado y tener en cuenta los
				// nonces para evitar los ataques de replay
				
				
				
				//Formato del fichero: Mensaje - Mac del mensaje
				
				String macdelMensajeCalculado = "";
				System.err.println(mensaje);
				if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
					output.println("Mensaje enviado integro ");
				} else {
					output.println("Mensaje enviado no integro.");
				}
				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	// ejecucion del servidor
	public static void main(String args[]) throws Exception {
		IntegrityVerifierServer server = new IntegrityVerifierServer();
		server.runServer();
	}
}
