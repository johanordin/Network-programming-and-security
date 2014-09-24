// package examples.security;
import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.KeyStore;

/** An example class that accesses a key within a keystore
  */

public class TruststoreAccess {
   static final String TRUSTSTORE = "jpattruststore.ks";
   static final String CERTALIAS = "exampleRSAcert";
   static final String STOREPASSWD = "changeit";
   /** The test method for the class
     */
   public static void main( String[] args ) {
      try {
         KeyStore ts = KeyStore.getInstance( "JCEKS" );
         ts.load( new FileInputStream( TRUSTSTORE ),
                  STOREPASSWD.toCharArray() );
         Certificate c = ts.getCertificate( CERTALIAS );
         System.out.println( "The certificate type for "
                             + "alias " + CERTALIAS
                             + " is " + c.getType() );
         System.out.println( "Certificate content for "
                             + "alias " + CERTALIAS
                             + " is " + c.toString() );
      }
      catch( Exception x ) {
         System.out.println( x );
         x.printStackTrace();
      }
   }
}
