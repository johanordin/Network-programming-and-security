
//  keytool -genkey -keyalg RSA -alias exampleRSApair -storetype JCEKS -keystore jpatkeystore.ks

import java.io.*;
import java.security.Key;
import java.security.KeyStore;
import sun.misc.BASE64Encoder;
/** An example class that accesses a key within a keystore
 */
public class KeystoreAccess {
	static final String KEYSTORE = "jpatkeystore.ks";
	static final String KEYALIAS = "exampleRSApair";
	static final String STOREPASSWD = "changeit";
	static final String ALIASPASSWD = "changeit";
	/** The test method for the class
	 */
	public static void main( String[] args ) {
		try {
			KeyStore ks = KeyStore.getInstance( "JCEKS" );
			ks.load( new FileInputStream( KEYSTORE ),
				STOREPASSWD.toCharArray() );
			Key k = ks.getKey( KEYALIAS, ALIASPASSWD.toCharArray() );
			System.out.println( "The algorithm for alias " + KEYALIAS + " is " + k.getAlgorithm() );
			System.out.println( "The algorithm for alias " + KEYALIAS + " is " + k.toString() );
		}
		catch( Exception x ) {
			System.out.println( x );
			x.printStackTrace();
		}
	}
}
