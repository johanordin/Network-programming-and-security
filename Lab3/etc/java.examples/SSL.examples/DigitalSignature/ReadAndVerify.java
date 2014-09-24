
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.*;
import java.security.cert.Certificate;
/** An example class that accesses a key within a keystore in order
  * to verify a digital signature
  */
public class ReadAndVerify {
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
         Certificate cert = ks.getCertificate( KEYALIAS );
         Signature sig = Signature.getInstance( "SHA1withRSA" );
         sig.initVerify( cert );
      	 FileInputStream fis = new FileInputStream( "hamlet1.txt" ); 
         FileChannel fc = fis.getChannel();
         ByteBuffer bb = ByteBuffer.allocate( (int) fc.size() );
         fc.read( bb );
         sig.update( bb.array() );
         FileInputStream fisSig = new FileInputStream( "hamlet1.txt.sig" );
         FileChannel fcSig = fisSig.getChannel();
         ByteBuffer bbSig = ByteBuffer.allocate( (int) fcSig.size() );
         fcSig.read( bbSig );
         boolean success = sig.verify( bbSig.array() );
         if ( success ) {
            System.out.println( "Signature verified" );
         } else {
            System.out.println( "Signature rejected" );
         }
         fc.close(); 
         fis.close();
         fcSig.close();
         fisSig.close();
      }
      catch( Exception x ) {
         System.out.println( x );
         x.printStackTrace();
      }
   }
}
