import java.math.BigInteger;
import java.util.Random;




public class Lab2 {
	
	public static void main(String [] args){
				
		Random rand = new Random();
		BigInteger p, q, N, v, k, d;
		
		//System.out.println("Random: " + rand.nextInt());
		////Generate large two prime numbers
		p = BigInteger.probablePrime(32, rand );
		q = BigInteger.probablePrime(32, rand );
		System.out.println("Generate large two prime numbers:");
		System.out.println("p = " + p + " and q = " + q );
		
		//compute
		N = p.multiply(q);		
		System.out.println("----------------------------------------");
		System.out.println("Multiply p and q. Call the new number N.");
		System.out.println("N is a part of the public key.");
		System.out.println("N = " + N);
		
		
		v = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		System.out.println("----------------------------------------");
		System.out.println("v = (p-1)*(q-1)");
		System.out.println("v = " + v);
		
		//find k
		int smallerBitLength;
		smallerBitLength = v.bitLength() - 1;
		k = BigInteger.probablePrime(smallerBitLength, rand);
		
		//returns true if gcd(v,k)=1
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
		
		
	    String orginalMsg = "johandaaafadasda" + "johandasda";
	    System.out.println("Length: " + orginalMsg.length());
	    
	    BigInteger c = new BigInteger(orginalMsg.getBytes());
	    
	    BigInteger encrypted = c.modPow(k, N);
	    
	    BigInteger decrypted = encrypted.modPow(d, N);	    
	    
	    String after = new String(decrypted.toByteArray());
	    
	    System.out.println(orginalMsg);
	    System.out.println(after);
	    
		
//		// to convert an integer b into a BigInteger
//		int b = 170; 
//		BigInteger bigB = new BigInteger(String.valueOf(b));
//
//			
////	 	to read a string from keyboard
//	    String input = (new BufferedReader(new InputStreamReader(System.in))).readLine();
//
////	 	to convert a string s into a BigInteger
//	    String s = "abc";
//	    BigInteger c = new BigInteger(s.getBytes());
//
//
//	// to convert a BigInteger back to a string	
//	    BigInteger a;
//	    String s = new String(a.toByteArray());
	}

}
