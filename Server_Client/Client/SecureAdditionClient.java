// A client-side class that uses a secure TCP/IP socket

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Scanner;

import javax.net.ssl.*;


public class SecureAdditionClient {
	private InetAddress host;
	private int port;
	
	// This is not a reserved port number 
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE    = "Johankeystore.ks";
	static final String TRUSTSTORE  = "Johantruststore.ks";
	static final String STOREPASSWD = "qwerty";
	static final String ALIASPASSWD = "qwerty";
  
	
	// Constructor @param host Internet address of the host where the server is located
	// @param port Port number on the host where the server is listening
	public SecureAdditionClient( InetAddress host, int port ) {
		this.host = host;
		this.port = port;
	}
	
  // The method used to start a client object
	public void run() {
		try {
			
			// First initialize the key and trust material
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ), STOREPASSWD.toCharArray() );
			
			Enumeration en = ks.aliases();
		      while (en.hasMoreElements()){
		          System.out.println("\nks alias: " + en.nextElement() + "\n"); 
		       }
		    
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
			SSLSocketFactory sslFact = sslContext.getSocketFactory();      	
			
			// --------------------------------------------------------//
//			System.out.println("Clienten stöder:");
//			for (int i = 0; i < sslFact.getSupportedCipherSuites().length; i++) {
//				System.out.println("getSupported: " + sslFact.getSupportedCipherSuites()[i]);
//			}
			
			SSLSocket client =  (SSLSocket)sslFact.createSocket(host, port);
			
			// --------------------------------------------------------//
//			System.out.println("Clienten har Valt:");
//		    for(int i = 0; i < client.getEnabledCipherSuites().length; i++){
//		    	System.out.println("getEnabled: " + client.getEnabledCipherSuites()[i]);
//		    }
			client.setEnabledCipherSuites( client.getSupportedCipherSuites() );
			
			
			//String[] suites = client.getSupportedCipherSuites();
			//System.out.println(suites);
			
			client.addHandshakeCompletedListener(new MyHandshakeListener());	
			client.startHandshake();
			//client.
			
			System.out.println("\n>>>> SSL/TLS handshake completed");

			//Create buffers to write to server and receive from server
			
			BufferedReader socketIn = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
			PrintWriter socketOut = new PrintWriter( client.getOutputStream(), true );
			
			
			//while(true){
			// --------------------------------------------------------//
			//show the menu
			System.out.println("\n--- Your now an authorized Client and can: ---");
			System.out.println("--- 1. Download..");
			System.out.println("--- 2. Upload..");
			System.out.println("--- 3. Delete..");
			System.out.println("--- 4. Quit..");
			System.out.println("--- Enter an option: ");
			
			// --------------------------------------------------------//
			//get input from the user
			Scanner in = new Scanner(System.in);
			int choice = in.nextInt();
			System.out.println("Enter a filename: ");
//	        System.out.flush();
//			filename = in.nextLine().trim();
		    Scanner scanner = new Scanner(System.in);
		    String filename = scanner.nextLine();
		    System.out.println("Your filename is " + filename);
			// sending to the server
			socketOut.println(choice);
			socketOut.println(filename);
			
			// --------------------------------------------------------//
			// Depending on the choice --> Do different things
			if (choice == 1){
				System.out.println(">>>> Downloading the file from server..");
				
				FileWriter fileWriterOut = new FileWriter("files/" + filename);
				PrintWriter printWriterOut = new PrintWriter(new BufferedWriter(fileWriterOut), true);
				
				String line = socketIn.readLine();
				
			    while (line != null) {
			    	printWriterOut.println(line);
			        line = socketIn.readLine();
			        System.out.print("...");
			        //Pause for 4 seconds
		            Thread.sleep(400);
			    }
			    System.out.println("\n>>>> Finished downloading.. ");
			    fileWriterOut.close();
			    printWriterOut.close();
				
			} else if (choice == 2) {
				
				try {
						System.out.println(">>>> Uploading the file to the server..");
//					FileReader fileReaderIn = new FileReader("files/" + filename);
//					BufferedReader br = new BufferedReader( fileReaderIn );
						
			            FileInputStream fileInputStream = new FileInputStream("files/" + filename);
			            
			          
			            byte[] buffer = new byte[client.getSendBufferSize()];
			            int read = 0;
			            int readtotal = 0;
			 
			            while ((read = fileInputStream.read(buffer)) != -1) {
			                readtotal = read + readtotal;
			                System.out.println("Writing :" + read + ", Total written:" + readtotal);
			                client.getOutputStream().write(buffer, 0, read);
			            }
			            client.getOutputStream().flush();
			           System.out.println("Client Finished :"+ read + ", Total written:" + readtotal);
			        } catch (Exception e) {
			            e.printStackTrace();
		        }
				
				
				
//				System.out.println(">>>> Uploading the file to the server..");
//				FileReader fileReaderIn = new FileReader("files/" + filename);
//				BufferedReader br = new BufferedReader( fileReaderIn );
//				
//				String line = br.readLine();
//				
//			    while (line != null) {
//			        socketOut.println(line); 
//			        
//			        line = br.readLine();
//			    }
//			    
//		        //Pause for 4 seconds
//	            Thread.sleep(4000);
//			    System.out.println(">>>> Finished upload to the server..");
//			    
//			    socketOut.flush();
//			    fileReaderIn.close();
//			    br.close();
				
			} else if (choice == 3) {
				System.out.println(">>>> Deleting the file from server..");
				
			} else {
				System.out.println(">>>> Quiting..");
				System.exit(0);
			}
			
			
			
			
			
//			String numbers = "1.2 3.4 5.6";
//			System.out.println( ">>>> Sending the numbers " + numbers + " to SecureAdditionServer" );
//			socketOut.println( numbers );
//			System.out.println( socketIn.readLine() );

			socketOut.println ( "" );
			
			//}
			
			
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
	
	
	// The test method for the class @param args Optional port number and host name
	public static void main( String[] args ) {
		try {
			
			InetAddress host = InetAddress.getLocalHost();
			System.out.println(host);
			//System.exit(0);
			
			int port = DEFAULT_PORT ;
			System.out.println(port);
			System.out.println(args.length);
			//System.exit(0);
			
			if ( args.length > 0 ) {
				port = Integer.parseInt( args[0] );
			}
			if ( args.length > 1 ) {
				host = InetAddress.getByName( args[1] );
			}
			
			
			SecureAdditionClient addClient = new SecureAdditionClient( host, port );
			System.out.println(host);
			//System.exit(0);
			
			addClient.run();
		}
		catch ( UnknownHostException uhx ) {
			System.out.println("No server up..");
			System.out.println( uhx );
			uhx.printStackTrace();
		}
	}
}


class MyHandshakeListener implements HandshakeCompletedListener {
	  public void handshakeCompleted(HandshakeCompletedEvent e) {
	    System.out.println("Handshake succesful!");
	    System.out.println("Using cipher suite: " + e.getCipherSuite());
	  }
	}
