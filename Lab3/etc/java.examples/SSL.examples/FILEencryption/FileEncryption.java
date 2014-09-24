
// query: java FileEncryption.class /e hamlet.txt hamlet.enc
// query: java FileEncryption.class /d hamlet.enc hamlet.txt 
// uses jpatkeystore.ks for public key encryption key


// package examples.security;
import java.io.*;
import javax.crypto.*;
import java.security.Key;
import java.security.KeyStore;
/** Class definition that handles the encryption and
  * decryption of files
  */
public class FileEncryption {
   static final String KEYSTORE = "jpatkeystore.ks";
   static final String KEYALIAS = "exampleDESkey";
   static final String STOREPASSWD = "changeit";
   static final String ALIASPASSWD = "changeit";
   /** Get the key for encrypting/decrypting the file
     * from the keystore; if it doesn't exist then
     * generate a key and save it for next time
     */ 
   private static Key getKey() {
      Key k = null;
      try {
         KeyStore ks = KeyStore.getInstance("JCEKS");
         ks.load( new FileInputStream( KEYSTORE ),
                  STOREPASSWD.toCharArray() );
         if ( ks.isKeyEntry( KEYALIAS ) ) {
            k = ks.getKey( KEYALIAS,
                           ALIASPASSWD.toCharArray() );
         } else {
            KeyGenerator kg
               = KeyGenerator.getInstance( "DES" );
            k = kg.generateKey();
            ks.setKeyEntry( KEYALIAS, k,
                            ALIASPASSWD.toCharArray(),
                            null );
            ks.store( new FileOutputStream( KEYSTORE ),
                      STOREPASSWD.toCharArray() );
         }
      } catch ( Exception x ) {
         System.out.println( x );
         x.printStackTrace();
      }
      return k;
   }
   /** Get the cipher needed for either encryption or
     * decryption
     * @param opMode specify either encrypt or decrypt mode
     */
   private static Cipher getCipher( int opMode ) {
      Cipher c = null;
      try {
         Key k = getKey();
         c = Cipher.getInstance( k.getAlgorithm() );
         c.init( opMode, k );
      } catch ( Exception x ) {
         System.out.println( x );
         x.printStackTrace();
      }
      return c;
   }
   /** Encrypt a file
     * @param clearFile the file to be encrypted
     * @param encrFile the encrypted output file
     */
   private static void encryptFile( String clearFile,
                                      String encrFile ) {
      try {
         FileInputStream fis
            = new FileInputStream( clearFile );
         FileOutputStream fos
            = new FileOutputStream( encrFile );
         Cipher c = getCipher( Cipher.ENCRYPT_MODE );
         CipherOutputStream cphout
            = new CipherOutputStream( fos, c );
         byte[] byteBuff = new byte[ 8196 ];
         int bytesRead = fis.read( byteBuff );
         while ( bytesRead != -1 ) {
            cphout.write( byteBuff, 0, bytesRead );
            bytesRead = fis.read( byteBuff );
         }
         cphout.close();
         fis.close();
         fos.close();
      } 
      catch( IOException iox ) {
         System.out.println( iox );
      }
   }
   /** Decrypt a file
     * @param encrFile the file to be decrypted
     * @param clearFile the decrypted output file
     */
   private static void decryptFile( String encrFile,
                                      String clearFile ) {
      try {
         FileInputStream fis
            = new FileInputStream( encrFile );
         Cipher c = getCipher( Cipher.DECRYPT_MODE );
         CipherInputStream cphin
            = new CipherInputStream( fis, c );
         FileOutputStream fos
            = new FileOutputStream( clearFile );
         byte[] byteBuff = new byte[ 8196 ];
         int bytesRead = cphin.read( byteBuff );
         while ( bytesRead != -1 ) {
            fos.write( byteBuff, 0, bytesRead );
            bytesRead = cphin.read( byteBuff );
         }
         fis.close();
         fos.close();
         cphin.close();
      } 
      catch( IOException iox ) {
         System.out.println( iox );
      }
   }
   /** Method used to display a message explaining how the
     * utility is to be used
     */
   private static void displayUsageMessage() {
      System.out.println( "Usage: " 
                          + FileEncryption.class.getName()
                          + " { /e | /d }"
                          + " source_file target_file" );
   }
   /** Method used to encrypt or decrypt a file based on
     * the option specified
     * @param args[0] /e or /d option
     * @param args[1] source filename
     * @param args[2] target filename
     */
   public static void main( String[] args ) {
      if ( args.length < 2 ) {
         displayUsageMessage();
      } else {
      	 if ( args[0].equalsIgnoreCase( "/e" ) ) {
      	 	// encrypt the source file to the target file
      	 	encryptFile( args[1], args[2] );
      	    System.out.println( args[1]
      	       + " successfully encrypted as " + args[2] );
      	 } else if ( args[0].equalsIgnoreCase( "/d" ) ) {
      	 	// decrypt the source file to the target file
      	 	decryptFile( args[1], args[2] );
      	    System.out.println( args[1]
      	       + " successfully decrypted as " + args[2] );
      	 } else {
      	 	// unrecognized option
            displayUsageMessage();
      	 }
      }
   }
}
