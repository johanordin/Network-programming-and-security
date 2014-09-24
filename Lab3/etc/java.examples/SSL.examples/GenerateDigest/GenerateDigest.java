
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;
/** An example class that generates a message digest
  * for a file specified as an input parameter
  */
public class GenerateDigest {
   /** The test method for the class
     */
   public static void main( String[] args ) {
      if ( args.length >= 1 ) try {
      	 MessageDigest md
      	    = MessageDigest.getInstance( "SHA-1" );
      	 FileInputStream fis
      	    = new FileInputStream( args[0] ); 
         FileChannel fc = fis.getChannel();
         ByteBuffer bb
            = ByteBuffer.allocate( (int) fc.size() );
         fc.read( bb );
         byte[] result = md.digest( bb.array() );
				
				String b64Encoded
            = new BASE64Encoder().encodeBuffer( result );
         System.out.println( "The SHA-1 digest of "
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

