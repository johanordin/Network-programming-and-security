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
//	static final int DEFAULT_PORT = 8189;
//	static final String KEYSTORE = "jpatkeystore.ks";
//	static final String TRUSTSTORE = "jpattruststore.ks";
//	static final String STOREPASSWD = "changeit";
//	static final String ALIASPASSWD = "changeit";
	
	// This is not a reserved port number 
	static final int DEFAULT_PORT = 8189;
	static final String KEYSTORE    = "KeyClientJohan.ks";
	static final String TRUSTSTORE  = "TrustClientJohan.ks";
	static final String STOREPASSWD = "123456";
	static final String ALIASPASSWD = "123456";
	
	
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
			
			System.out.println(KeyStore.getDefaultType());
			
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
			
//			File keystoreFile = new File();
//			
//			keystoreFile.getParentFile().mkdirs();
//			KeyTool kt = new KeyTool(keystoreFile, KEYSTORE_PASSWORD);
//			// Generate a self signed RSA key with a life of 10 years..
//			kt.genKeyPair("selfsigned", "RSA", 2048, 3600);
//			kt.saveKeyStore();
			
			// --------------------------------------------------------//
			
			SSLContext sslContext = SSLContext.getInstance( "TLS" );
			sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );
			
			SSLServerSocketFactory sslServerFactory = sslContext.getServerSocketFactory();
			SSLServerSocket sss = (SSLServerSocket) sslServerFactory.createServerSocket( port );
			
			sss.setEnabledCipherSuites( sss.getSupportedCipherSuites() );
			//Needed to auth client?
			//sss.setNeedClientAuth(true);
			
			System.out.println("\n>>>> SecureAdditionServer: active ");
			SSLSocket incoming = (SSLSocket)sss.accept();
			
			// --------------------------------------------------------//

			BufferedReader in = new BufferedReader( new InputStreamReader( incoming.getInputStream() ) );
			PrintWriter out = new PrintWriter( incoming.getOutputStream(), true );			
			
			
			
			String str;

			while ( !(str = in.readLine()).equals("") ) {
				double result = 0;
				StringTokenizer st = new StringTokenizer( str );
				
				try {
					while( st.hasMoreTokens() ) {
						Double d = new Double( st.nextToken() );
						result += d.doubleValue();
					}
					out.println( "The result is " + result );
				}
				catch( NumberFormatException nfe ) {
					out.println( "Sorry, your list contains an invalid number" );
				}
			}
			
			
			
			
			
			incoming.close();
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



