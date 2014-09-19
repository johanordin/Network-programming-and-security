import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;




public class Lab2 {
	
	
	public void run() throws IOException{
		
		Random rand = new Random();
		BigInteger p, q, N, v, k, d;
		
		int bitLength = 64;
		
		// Generate two large prime numbers p and q. Unique for every receiver
		p = BigInteger.probablePrime(bitLength, rand );
		q = BigInteger.probablePrime(bitLength, rand );
		System.out.println("Generate large two prime numbers:");
		System.out.println("p = " + p + " and q = " + q );
		
		// Compute the public number N.
		N = p.multiply(q);		
		System.out.println("----------------------------------------");
		System.out.println("Multiply p and q. Call the new number N.");
		System.out.println("N is a part of the public key.");
		System.out.println("N = " + N);
		
		// Compute the the number v
		v = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		System.out.println("----------------------------------------");
		System.out.println("v = (p-1)*(q-1)");
		System.out.println("v = " + v);
		
		//find k thats a relative prime to v.
		int smallerBitLength;
		smallerBitLength = v.bitLength() - 1;
		k = BigInteger.probablePrime(smallerBitLength, rand);
		
		
		// Find a number thats satisfy the equation gcd(v,k)=1. which is the greatest common denominator.
		// returns true if gcd(v,k)=1
		int i = 0;
		while ( (v.gcd(k)).equals(BigInteger.ONE) == false ){
			i++;
			System.out.println("i: " + i);	
			k = BigInteger.probablePrime(smallerBitLength, rand);
		}
		System.out.println("----------------------------------------");
		System.out.println("Find a number k that is a relatively prime to v");
		System.out.println("That is than by the computation gcd(v,k)=1");
		System.out.println("k = " + k);
		
		//find d
		d = k.modInverse(v);
		System.out.println("----------------------------------------");
		System.out.println("Compute d such that");
		System.out.println("(d * k) % v = 1");
		System.out.println("d = " + d);
		
		System.out.println("----------------------------------------");
		System.out.println("----------------------------------------");
		System.out.println("Public key  (k, N) is: (" + k + "," + N + ")");
		System.out.println("Private key (d, N) is: (" + d + "," + N + ")");
		System.out.println("----------------------------------------");
		
		//to read a string from keyboard
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input; 
		//System.out.println("With a bitLength of: " + bitLength + " bits, you are able to send a message that contains : "  + bitLength/4 + " characthers.");
		System.out.println("Enter your Message you want to decrypt and send: ");
		input = br.readLine();
		System.out.println("Length: " + input.length());
		
		//convert the message to a BigInteger.	
		BigInteger c = new BigInteger(input.getBytes());
		
		System.out.println("----------------------------------------");
		System.out.println("We know use the Public key (k, N) to encrypt the message m:\n...");
		System.out.println(" --> c = m^e mod N ...and sends c ");
		
	    BigInteger encrypted = c.modPow(k, N);
	    
	    System.out.println("----------------------------------------");
	    System.out.println("And the revciver has the Private key (d, N) to decrypt the message by computing:\n...");
	    System.out.println("--> m = c^d mod N ");
	    
	    BigInteger decrypted = encrypted.modPow(d, N);	    

	    // to convert a BigInteger back to a string	
	    String after = new String(decrypted.toByteArray());
	    
	    System.out.println("----------------------------------------");
	    System.out.println("The orginal message before we encrypted it\n");
	    System.out.println(input);
	    
	    System.out.println("----------------------------------------");
	    System.out.println("The message the receiver got after decryption\n");
	    System.out.println(after);
		
		
		
	}
	
	public static void main(String [] args) throws IOException{
		
	    Lab2 lab2 = new Lab2();
	    
	    lab2.run();
	    
	}
	
	

}





//String orginalMsg = "johandaaafadasda" + "johandasda";
//System.out.println("Length: " + orginalMsg.length());    
//BigInteger c = new BigInteger(orginalMsg.getBytes());
//System.out.println(orginalMsg);
//System.out.println(after);

//// to convert an integer b into a BigInteger
//int b = 170; 
//BigInteger bigB = new BigInteger(String.valueOf(b));
//
//	
////	to read a string from keyboard
//String input = (new BufferedReader(new InputStreamReader(System.in))).readLine();
//
////	to convert a string s into a BigInteger
//String s = "abc";
//BigInteger c = new BigInteger(s.getBytes());
//
//
//// to convert a BigInteger back to a string	
//BigInteger a;
//String s = new String(a.toByteArray());
