// An example class that uses the secure server socket class

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

import sun.security.tools.KeyTool;

import java.security.*;
import java.util.StringTokenizer;


public class SecureAdditionServer {
	private int port;

	// This is not a reserved port number 
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE    = "Serverkeystore.ks";
	static final String TRUSTSTORE  = "Servertruststore.ks";
	static final String STOREPASSWD = "123456";
	static final String ALIASPASSWD = "123456";
	
	//This is for the keyTool class
	static final String KEYSTORE_PASSWORD = "123456";
	
	
	/** Constructor
	 * @param port The port where the server
	 *    will listen for requests
	 */
	SecureAdditionServer( int port ) {
		this.port = port;
	}
	
	/** The method that does the work for the class */
	public void run() {
		try {
			
			// First initialize the key and trust material		
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), STOREPASSWD.toCharArray() );
			
			KeyStore ts = KeyStore.getInstance( "JCEKS" );
			ts.load( new FileInputStream( TRUSTSTORE ), STOREPASSWD.toCharArray() );
			
			// KeyManagers decide which key material to use
			KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
			kmf.init( ks, ALIASPASSWD.toCharArray() );
			
			// TrustManagers decide whether to allow connections
			TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
			tmf.init( ts );

			// --------------------------------------------------------//
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
			sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			//Needed to auth client?
			sss.setNeedClientAuth(true);
			
			System.out.println("\n>>>> SecureAdditionServer: active ");
			SSLSocket incoming = (SSLSocket)sss.accept();
			
			// --------------------------------------------------------//
			// declare the buffers
			BufferedReader inClient = new BufferedReader( new InputStreamReader( incoming.getInputStream() ) );
			
			PrintWriter out = new PrintWriter( incoming.getOutputStream(), true );			
			
			System.out.println("[S] - Reads option from client..");
			int choice = Integer.parseInt(inClient.readLine());
			String filename = inClient.readLine();
			
			//while(true) {
			// --------------------------------------------------------//
			// Depending on the choice --> Do different things
			if (choice == 1){
				
				System.out.println("[S] - Starting to send the file..");		
				try {		
		            FileInputStream fileInputStream = new FileInputStream("files/" + filename);
		          
		            byte[] buffer = new byte[incoming.getSendBufferSize()];
		            int read = 0;
		            int readtotal = 0;
		 
		            while ((read = fileInputStream.read(buffer)) != -1) {
		                readtotal = read + readtotal;
		                incoming.getOutputStream().write(buffer, 0, read);
		            }
		            incoming.getOutputStream().flush();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
				System.out.println("[S] - Finished to send the file..");	
				
			} else if (choice == 2) {
				System.out.println("[S] - Uploading the file to the server..");
	
				try {
	                InputStream inputStream = incoming.getInputStream();
	                FileOutputStream fileOutputStream = new FileOutputStream(new File("files/" + filename));
	                
	                byte[] buffer = new byte[incoming.getReceiveBufferSize()];
	                int read = 0;
	                int readtotal = 0;
	                
	                while ((read = inputStream.read(buffer)) != -1) {
	                    readtotal = readtotal + read;
	                    fileOutputStream.write(buffer, 0, read);
	                }
	                
	            } catch (IOException ex) {
	            }
				
				System.out.println("[S] - Uploading finished");


			} else if (choice == 3) {
				
				System.out.println("[S] : Deleting the file from server..");
				File file = new File("files/" + filename);
				if(!file.delete()){
					System.out.println("[S] : Deleting failed.");
				}
				
			} else {
				
				System.out.println("Closing the connection..");
				// --------------------------------------------------------//
				//Super important --> Closing the connection.
				incoming.close();
				System.exit(0);
			}	
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
	
	/** The test method for the class
	 * @param args[0] Optional port number in place of
	 *        the default
	 */
	public static void main( String[] args ) {
		int port = DEFAULT_PORT;
		if (args.length > 0 ) {
			port = Integer.parseInt( args[0] );
		}
		SecureAdditionServer addServe = new SecureAdditionServer( port );
		addServe.run();
	}
}



