import java.math.BigInteger;
import java.util.Random;


/**
 * @author Johan
 *
 */
public class RSA_Algoritm {
	
	private Random rand = new Random();

	private BigInteger p, q, N, v, k, d;
	
	private int smallerBitLength;
	
	
	public RSA_Algoritm (int bitLength) {
		
		
		p = BigInteger.probablePrime(bitLength, rand );
		q = BigInteger.probablePrime(bitLength, rand );
		
		N = p.multiply(q);
		
		v = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		smallerBitLength = v.bitLength() - 1;
		k = BigInteger.probablePrime(smallerBitLength, rand);
		
		while ( (v.gcd(k)).equals(BigInteger.ONE) == false ){
			k = BigInteger.probablePrime(smallerBitLength, rand);
		}
		
		d = k.modInverse(v);
		
	}
	
	
	private void compute(){
		
	}
	
	
	
	
}

