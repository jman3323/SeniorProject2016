import java.util.*;

public class Substitution 
{
	public static final int DEFAULT_ITERATIONS = 17;

	/**
	 * Encrypts a string by substituting every letter with its corresponding letter in the key. 
	 * The letter 'A' is substituted with the 1st character in the key, 'B' with the 2nd, and so on. 
	 * Non-alphabet characters are ignored. 
	 * Keys are not checked for validity.
	 * @param m the message to encrypt
	 * @param key permutation of the alphabet to be used as the substitution key
	 * @return the encrypted string
	 */
	public static String encrypt (String m, String key)
	{
		String c = "";
		for (int i = 0; i < m.length(); i++)
		{
			char ch = m.charAt(i);
			if (ch >= 'A' && ch <= 'Z')
				c += ("" + key.charAt(ch - 'A')).toUpperCase();
			else
				if (ch >= 'a' && ch <= 'z')
					c += ("" + key.charAt(ch - 'a')).toLowerCase();
				else
					c += ch;
		}
		return c;
	}

	/**
	 * Decrypts the ciphertext with the given key. Non-alphabet characters are ignored.
	 * Keys are not checked for validity.
	 * @param c the ciphertext to decrypt
	 * @param key permutation of the alphabet to be used as the substitution key
	 * @return the decrypted string
	 */
	public static String decrypt (String c, String key)
	{
		String m = "";
		key = key.toLowerCase();
		for (int i = 0; i < c.length(); i++)
		{
			char ch = c.charAt(i);
			if (ch >= 'A' && ch <= 'Z')
				m += (char) (key.indexOf(ch - 'A' + 'a') + 'A');
			else
				if (ch >= 'a' && ch <= 'z')
					m += (char) (key.indexOf(ch) + 'a');
				else
					m += ch;
		}
		return m;
	}

	/**
	 * Attempts to crack the ciphertext using a hill-climbing algorithm. The number of iterations is how many times the algorithm starts over with a new random key. 
	 * This is done because local maximum "hills" can be found that are not truly the highest, so starting over with a random key makes it more likely that a different, 
	 * and possibly higher, maximum will be found. Iterating more times increases the likelihood that the best decryption will be found at the cost of runtime.
	 * @param c the ciphertext to crack
	 * @param iterations how many times the hill-climbing algorithm should run
	 * @return the most likely decryption
	 */
	public static String crack (String c, int iterations)
	{
		double maxScoreAll = Cipher.getQuadScore(c);
		String m = c;
		ArrayList<Integer> swaps = new ArrayList<>(), usedSwaps = new ArrayList<>();
		for (int s0 = 0; s0 < 25; s0++)
			for (int s1 = s0 + 1; s1 <= 25; s1++)
				swaps.add(s0 * 100 + s1);
		
		for (int starts = 0; starts < iterations; starts++)
		{
			//---------------------------------------------
			// Setup: Random key, all possible swaps reset
			//---------------------------------------------
			swaps.addAll(usedSwaps);
			usedSwaps.clear();
			String key = keyGen();
			String dec = Substitution.decrypt(c, key);

			//---------------------------------------------
			// Use quadgrams to try and improve some more
			//---------------------------------------------
			double maxScore = Cipher.getQuadScore(dec);
			while (swaps.size() > 0)
			{
				Integer swapHash = swaps.remove((int) (Math.random() * swaps.size()));
				usedSwaps.add(swapHash);
				int swap0 = swapHash / 100, swap1 = swapHash % 100;
				String newDec = "";
				for (int i = 0; i < dec.length(); i++)
				{
					char ch = dec.charAt(i);
					if (ch - 'A' == swap0)
						newDec += (char) (swap1 + 'A');
					else if (ch - 'a' == swap0)
						newDec += (char) (swap1 + 'a');
					else if (ch - 'A' == swap1)
						newDec += (char) (swap0 + 'A');
					else if (ch - 'a' == swap1)
						newDec += (char) (swap0 + 'a');
					else
						newDec += ch;
				}
				double score = Cipher.getQuadScore(newDec);
				if (score > maxScore)
				{
					maxScore = score;
					dec = newDec;
					swaps.addAll(usedSwaps);
					usedSwaps.clear();
				}
			}

			if (maxScore > maxScoreAll)
			{
				maxScoreAll = maxScore;
				m = dec;
			}
		}
		return m;
	}

	/**
	 * Attempts to crack the ciphertext using a hill-climbing algorithm. Uses the constant DEFAULT_ITERATIONS for number of iterations.
	 * @param c the ciphertext to crack
	 * @return the most likely decryption
	 */
	public static String crack (String c)
	{
		return crack(c, DEFAULT_ITERATIONS);
	}

	public static String keyGen ()
	{
		String alpha = "abcdefghijklmnopqrstuvwxyz";
		String key = "";
		while (alpha.length() > 0)
		{
			int index = (int) (Math.random() * alpha.length());
			key += alpha.charAt(index);
			alpha = alpha.replaceAll("" + (char) alpha.charAt(index), "");
		}
		return key;
	}
	
	public static void demo ()
	{
		System.out.println("----------------Substitution---------------------");
		String m = Cipher.getPlaintext();
		String key = keyGen();
		String c = encrypt(m, key);
		System.out.println("Ciphertext: " + c + "\n");
		String dec = crack(c);
		System.out.println("Decryption: " + dec + "\n");
		//System.out.println("Correct score: " + Cipher.getQuadScore(m));
		//System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");
		System.out.println("*******************************************");
		System.out.println();
	}
}
