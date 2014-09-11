import java.math.BigInteger;
import java.util.Random;




public class Lab2 {
	
	public static void main(String [] args){
				
		Random rand = new Random();
		BigInteger p, q, N, v, k, d;
		
		System.out.println("Random: " + rand);
		//Select two prime numbers
		p = BigInteger.probablePrime(128, rand);
		q = BigInteger.probablePrime(128, rand);
		
		//compute
		N = p.multiply(q);
		v = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		//find k
		int smallerBitLength;
		smallerBitLength = v.bitLength() - 1;
		////System.out.println(smallerBitLength);
		
		System.out.println("Random: " + rand);
		
		k = BigInteger.probablePrime(smallerBitLength, rand);
		//returns true if gcd(x,k)=1
		int i = 0;
		System.out.println("Hello");
		while ( (v.gcd(k)).equals(BigInteger.ONE) == false ){
			i++;
			System.out.println("i: " + i);	
			System.out.println("Random: " + rand);	
			k = BigInteger.probablePrime(smallerBitLength, rand);
		}
		System.out.println(v);
		System.out.println(k);
		System.out.println("Hello after");
		System.out.println("Random: " + rand);
		
		
		//find d
		
		d = k.modInverse(v);
		System.out.println("d: " + d);
		
	
		System.out.println(p);
		System.out.println(q);
		System.out.println("Public key is : (" + k + "," + N + ")");
		System.out.println("Private key is: (" + d + "," + N + ")");
		
		
	    String orginalMsg = "johandaaafadasda" + "johandaaafadasda";
	    
	    System.out.println("Length: " + orginalMsg.length());
	    
	    BigInteger c = new BigInteger(orginalMsg.getBytes());
	    
	    BigInteger encrypted = c.modPow(k, N);
	    
	    BigInteger decrypted = encrypted.modPow(d, N);	    
	    
	    String after = new String(decrypted.toByteArray());
	    
	    System.out.println(orginalMsg);
	    System.out.println(after);
	    
		
	    
	    
	    
//		int  n = rand.nextInt(50) + 1;
//		System.out.println(n);
		
		
		
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
