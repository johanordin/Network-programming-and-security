import java.math.BigInteger;
import java.util.Random;




public class Lab2 {
	
	public static void main(String [] args){
				
		Random rand = new Random();
		BigInteger p, q, N, v, k, d, z;
		
		System.out.println("Random: " + rand);
		//Select two prime numbers
		p = BigInteger.probablePrime(32, rand);
		q = BigInteger.probablePrime(32, rand);
		
		//compute
		N = p.multiply(q);
		
		v = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		//find k
		int smallerBitLength;
		smallerBitLength = v.bitLength() - 1;
		////System.out.println(smallerBitLength);
		
		System.out.println("Random: " + rand);
		
		z = BigInteger.probablePrime(smallerBitLength, rand);
		//returns true if gcd(x,z)=1
		int i = 0;
		System.out.println("Hello");
		while ( (v.gcd(z)).equals(BigInteger.ONE) == false ){
			i++;
			
			System.out.println("i: " + i);	
			System.out.println("Random: " + rand);	
			z = BigInteger.probablePrime(smallerBitLength, rand);
		}
		System.out.println(v);
		System.out.println(z);
		
		System.out.println("Hello after");
		
		
		
		System.out.println("Random: " + rand);
		
		
		BigInteger e = p.gcd(q);
		
		
		System.out.println(p);
		System.out.println(q);
		System.out.println(e);
		
		
		
		
//		int  n = rand.nextInt(50) + 1;
//		System.out.println(n);
	}

}
