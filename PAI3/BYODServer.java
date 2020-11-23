import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;


public class BYODServer {

    private SSLServerSocket serverSocket;
        //private static final String[] protocols = new String[]{"TLSv1.3"};
        //private static final String[] cipher_suites = new String[]{"TLS_AES_128_GCM_SHA256"};
	// Constructor
	public BYODServer() throws Exception {

		// ServerSocketFactory para construir los ServerSockets
		SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		// creación de un objeto ServerSocket (se establece el puerto)
        serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);
        //serverSocket.setEnabledProtocols(protocols);
        //serverSocket.setEnabledCipherSuites(cipher_suites);
	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		Integer a=0;
        Integer f=0;
        Map<String, String> userdata = new HashMap<String, String>();
        userdata.put("b49a5780a99ea81284fc0746a78f84a30e4d5c73",
         "7be057c04632bd379b952d0cb82fac77dac1f867"); //usuario:juan pass:elpepe

		while (true) {
			// espera las peticiones del cliente para comprobar el usuario y contraseña
			try {
				System.err.print("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				// abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				// abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				// se lee del cliente el mensaje, contraseña y usuario
				String mensaje = input.readLine();
                String usuario = input.readLine();
                String contrasenya = input.readLine();
				// a continuación habría que verificar la contrseña del usuario y este en sí. La codificación usada es sha1
                String sha1u = "";
                String sha1p = "";
                try{
                    MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
                    MessageDigest sha2 = MessageDigest.getInstance("SHA-1");
                    sha1.reset();
                    sha2.reset();
                    sha1.update(usuario.getBytes("utf8"));
                    sha2.update(contrasenya.getBytes("utf8"));
                    sha1u= String.format("%040x", new BigInteger(1, sha1.digest()));
                    sha1p= String.format("%040x", new BigInteger(1, sha2.digest()));
                } catch (Exception e){
                    e.printStackTrace();
                }
                //Comprobamos que la base de datos tiene al usuario registrado, luego si la contrasenia es correcta.
                File archivo= new File("./logf.txt");
                BufferedWriter bw;
				Date date =new Date();
                DateFormat h= new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                if (archivo.exists()){
                    bw= new BufferedWriter(new FileWriter(archivo,true));
                }else{
                    bw= new BufferedWriter(new FileWriter(archivo,false));	
                }

                if (userdata.containsKey(sha1u)) {
                    if(userdata.get(sha1u).equals(sha1p)){
                        bw.write("["+h.format(date)+"]"+"Mensaje de "+usuario+ " --- > "+mensaje+" \n");
                        output.println("El mensaje ha sido registrado con exito");
                        
                    }else {
                        bw.write("["+h.format(date)+"]"+"Intento error pass de "+usuario+ " --- > "+mensaje+" \n");
                        output.println("La contrasenya enviada es erronea");
                    }
				} else {
                    output.println("Usuario no registrado");
                }
                bw.close();
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
		BYODServer server = new BYODServer();
		server.runServer();
	}

    
    
}
