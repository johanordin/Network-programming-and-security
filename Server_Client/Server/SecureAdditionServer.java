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
			//Keytool ktool = KeyTool.getCacertsKeyStore()
			
//			System.out.println(KeyStore.getDefaultType());
			
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
			
//			File keystoreFile = new File("keystores/nyStore.ks");
////			
//			keystoreFile.getParentFile().mkdirs();
//			KeyTool kt = new KeyTool(keystoreFile, KEYSTORE_PASSWORD);
			
////		// Generate a self signed RSA key with a life of 10 years..
//			kt.genKeyPair("selfsigned", "RSA", 2048, 3600);
//			kt.saveKeyStore();
			
//			KeyTool kt = new KeyTool();
//
//			kt.run(args, System.out);
			
			
			// --------------------------------------------------------//
			
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			
			
//			System.out.println("Servern stöder:");
//			for (int i = 0; i < sslServerFactory.getSupportedCipherSuites().length; i++) {
//				System.out.println("getSupported: " + sslServerFactory.getSupportedCipherSuites()[i]);
//			}
			    
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
			
			
//			System.out.println("Servern har Valt:");
//		    for(int i = 0; i < sss.getEnabledCipherSuites().length; i++){
//		    	System.out.println("getEnabled: " + sss.getEnabledCipherSuites()[i]);
//		    }
				
			sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			//Needed to auth client?
			//sss.setNeedClientAuth(true);
			
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
				
				FileReader in = new FileReader("files/" + filename);
				BufferedReader br = new BufferedReader( in );
				
				File file = new File("files/" + filename);
				System.out.println(file.exists());

				if ( file.exists() ){
					
					String line = br.readLine();
				    while ( line != null ) {
				        out.println(line);
				        line = br.readLine();
				        System.out.println("...");	
				    }
				    System.out.println("[S] - Server done sending..");		
				} else {
					System.out.println("[S] - Couldn't not find the file.");	
				}
				
			    in.close();
			    br.close();
				
			} else if (choice == 2) {
				System.out.println("[S] - Uploading the file to the server..");
				
//				FileWriter fileWriterOut = new FileWriter("files/" + filename);
//				PrintWriter printWriterOut = new PrintWriter(new BufferedWriter(fileWriterOut), true);
//				
				try {
	                InputStream inputStream = incoming.getInputStream();
	                FileOutputStream fileOutputStream = new FileOutputStream(new File("files/" + filename));
	                
	                byte[] buffer = new byte[incoming.getReceiveBufferSize()];
	                int read = 0;
	                int readtotal = 0;
	                
	                while ((read = inputStream.read(buffer)) != -1) {
	                    readtotal = readtotal + read;
	                    System.out.println("Writing :" + read + ", Total written:" + readtotal);
	                    fileOutputStream.write(buffer, 0, read);
	                }
	                
	                System.out.println("Server Finished :"+ read + ", Total written:" + readtotal);
	            } catch (IOException ex) {
	            }
				
				
				
				
				
				//////////-----------------------------------

//				String line = null;
//			    int i = 0;
//				while ( (line = inClient.readLine()) != null ) {
//			    	
//					
//					printWriterOut.println(line);
//			    	System.out.println(line);
//			    	System.out.println(i);
//			    	i++;
//			    }
				
//				String line = inClient.readLine();
//			    
//				while ( line != null ) {
//			    	printWriterOut.println(line);
//			    	//System.out.println(line);
//			        line = inClient.readLine();
//			    }
//				
//				
			    
				System.out.println("[S] - Uploading finished");
//			    fileWriterOut.close();
//			    printWriterOut.close();
			    
			    
			    /////////////////////////////////////////////


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
//				incoming.close();
				System.exit(0);
			}
			
			
			
			//}
			
//			// --------------------------------------------------------//
			//Super important --> Closing the connection.
			//incoming.close();
			
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



