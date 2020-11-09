import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.*;

public class IntegrityVerifierServer {
	private ServerSocket serverSocket;

	// Constructor
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
		// creación de un objeto ServerSocket (se establece el puerto)
		serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		Integer a=0;
		Integer f=0;
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
				// se lee del cliente el mensaje y el macdelMensajeEnviado
				String mensaje = input.readLine();
				String macdelMensajeEnviado = input.readLine();
				// a continuación habría que verificar el MAC
				String key= ")SG]t09{";
				String macdelMensajeCalculado = MACGen.calculaMac(mensaje, key);//
				System.err.println(mensaje);
				
				if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
					output.println("Mensaje enviado integro ");
					a++;
				} else {
					output.println("Mensaje enviado no integro.");
					File archivo= new File("./logf.txt");
					BufferedWriter bw;
					Date date =new Date();
					DateFormat h= new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
					if (archivo.exists()){
						bw= new BufferedWriter(new FileWriter(archivo,true));
					}else{
						bw= new BufferedWriter(new FileWriter(archivo,false));	
					}
					f++;
					bw.write("["+h.format(date)+"]"+"Mensaje enviado no integro---- El ratio de fallo es de: "+(f/f+a)*100+"%\n");
					bw.close();

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
