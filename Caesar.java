
public class Caesar 
{
	/**
	 * Rotates the plaintext by the key k. Non-alphabet characters are ignored.
	 * @param m the plaintext message
	 * @param k the key with which to rotate by
	 * @return an encrypted string
	 */
	public static String encrypt (String m, int k)
	{
		String c = "";
		k %= 26;
		for (int i = 0; i < m.length(); i++)
		{
			char ch = m.charAt(i);
			if (ch >= 'A' && ch <= 'Z')
				c += (char) ((ch - 'A' + k) % 26 + 'A');
			else
				if (ch >= 'a' && ch <= 'z')
					c += (char) ((ch - 'a' + k) % 26 + 'a');
				else
					c += ch;
		}
		return c;
	}

	/**
	 * Decrypts the ciphertext as if it was encrypted with k. In other words, it rotates the ciphertext by -k (negative k). Non-alphabet characters are ignored.
	 * @param c the ciphertext
	 * @param k the key with which to decrypt
	 * @return a decrypted string
	 */
	public static String decrypt (String c, int k)
	{
		String m = "";
		k %= 26;
		for (int i = 0; i < c.length(); i++)
		{
			char ch = c.charAt(i);
			if (ch >= 'A' && ch <= 'Z')
				m += (char) ((ch - 'A' - k + 26) % 26 + 'A');
			else
				if (ch >= 'a' && ch <= 'z')
					m += (char) ((ch - 'a' - k + 26) % 26 + 'a');
				else
					m += ch;
		}
		return m;
	}

	/**
	 * Attempts to crack the ciphertext. Brute forces all keys and chooses the decryption with the highest fitness score.
	 * @param c ciphertext to crack
	 * @return the plaintext most likely to be English
	 */
	public static String crack (String c)
	{
		double maxScore = Cipher.getScore(c);
		int maxKey = 0;
		String m = c;
		for (int k = 1; k <= 25; k++)
		{
			String dec = decrypt(c, k);
			double score = Cipher.getScore(dec);
			if (score > maxScore)
			{
				maxScore = score;
				maxKey = k;
				m = dec;
			}
		}
		return m;
	}
	
	public static int keyGen()
	{
		return (int) (Math.random() * 26);
	}
	
	public static void demo ()
	{
		System.out.println("----------------Caesar---------------------");
		String m = Cipher.getPlaintext();
		int key = keyGen();
		String c = Caesar.encrypt(m, key);
		System.out.println("Ciphertext: " + c + "\n");
		String dec = crack(c);
		System.out.println("Decryption: " + dec + "\n");
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed");
		System.out.println("*******************************************");
		System.out.println();
	}
}
