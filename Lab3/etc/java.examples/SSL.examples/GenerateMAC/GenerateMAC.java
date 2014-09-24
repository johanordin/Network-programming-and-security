
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Encoder;
/** An example class that generates a message
  * authentication code (MAC)
  */
public class GenerateMAC {
   /** The test method for the class
     */
   public static void main( String[] args ) {
      if ( args.length >= 1 ) try {
      	 String macAlg = "HmacSHA1";
      	 SecureRandom sr = new SecureRandom();
      	 byte[] keyData = new byte[20];
      	 sr.nextBytes( keyData );
         SecretKey sk
            = new SecretKeySpec( keyData, macAlg );
         Mac mac = Mac.getInstance( macAlg );
         mac.init( sk );
      	 FileInputStream fis
      	    = new FileInputStream( args[0] ); 
         FileChannel fc = fis.getChannel();
         ByteBuffer bb
            = ByteBuffer.allocate( (int) fc.size() );
         fc.read( bb );
         byte[] result = mac.doFinal( bb.array() );
         String b64Encoded
            = new BASE64Encoder().encodeBuffer( result );
         System.out.println( "The " + macAlg + " MAC of "
                             + args[0] + " is "
                             + b64Encoded );
         fc.close(); 
         fis.close();
      }
			catch( Exception x ) {
         System.out.println( x );
         x.printStackTrace();
      } else {
         System.out.println( "A file name is required." );
      }
   }
}
