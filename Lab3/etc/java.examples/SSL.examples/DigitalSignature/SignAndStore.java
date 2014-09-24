// package examples.security;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.*;
/** An example class that accesses a key within a keystore and uses
  * the key to create a digital signature
  */
public class SignAndStore {
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
         Signature sig = Signature.getInstance( "SHA1withRSA" );
         sig.initSign( (PrivateKey) k );
         FileInputStream fis = new FileInputStream( "hamlet1.txt" ); 
         FileChannel fc = fis.getChannel();
         ByteBuffer bb = ByteBuffer.allocate( (int) fc.size() );
         fc.read( bb );
         sig.update( bb.array() );
         byte[] result = sig.sign();
         FileOutputStream fos = new FileOutputStream( "hamlet1.txt.sig" );
         fos.write( result );
         fc.close(); 
         fis.close();
         fos.close();
         System.out.println( "Signature generated successfully and saved" );
      }
      catch( Exception x ) {
         System.out.println( x );
         x.printStackTrace();
      }
   }
}
