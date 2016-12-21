
public class RailFence 
{
	/**
	 * Encrypts the string using the specified number of rails
	 * @param m the message to encrypt
	 * @param key the number of rails to use
	 * @return the encrypted string. If the key <= 1 or >= m.length(), the original message m is returned
	 */
	public static String encrypt (String m, int key)
	{
		if (key <= 1 || key >= m.length())
			return m;
		String[] rails = new String[key];
		for (int i = 0; i < rails.length; i++)
			rails[i] = "";
		int r = 0, dir = 1;
		for (int i = 0; i < m.length(); i++)
		{
			rails[r] += m.charAt(i);
			if (r == key - 1 && dir == 1 || r == 0 && dir == -1)
				dir *= -1;
			r += dir;
		}
		String c = "";
		for (int i = 0; i < rails.length; i++)
			c += rails[i];
		return c;
	}
	
	/**
	 * Decrypts the string using the specified number of rails
	 * @param c the ciphertext to decrypt
	 * @param key the number of rails to use
	 * @return the decrypted string. If the key <= 1 or >= m.length(), the original message m is returned
	 */
	public static String decrypt (String c, int key)
	{
		if (key <= 1 || key >= c.length())
			return c;
		String[] rails = new String[key];
		for (int i = 0; i < rails.length; i++)
			rails[i] = "";
		int[] lengths = new int[rails.length];
		lengths[0] = (int) Math.ceil((double) c.length() / (2 * (key - 1)));
		int extras = c.length() % (2 * (key - 1));
		for (int r = 1; r < lengths.length - 1; r++)
		{
			lengths[r] = 2 * (c.length() / (2 * (key - 1)));
			if (r < extras)
				lengths[r]++;
			if (2 * (key - 1) - r < extras)
				lengths[r]++;
		}
		int index = 0;
		for (int i = 0; i < lengths.length - 1; index += lengths[i], i++)
			rails[i] = c.substring(index, index + lengths[i]);
		rails[lengths.length - 1] = c.substring(index);
		String m = "";
		int r = 0, dir = 1;
		for (int i = 0; i < c.length(); i++)
		{
			m += rails[r].charAt(0);
			rails[r] = rails[r].substring(1);
			if (r == key - 1 && dir == 1 || r == 0 && dir == -1)
				dir *= -1;
			r += dir;
		}
		return m;
	}
	
	/**
	 * Attempts to crack the cipher. Brute forces all possible keys [1, c.length()-1]
	 * @param c the ciphertext to crack.
	 * @return the most likely decryption
	 */
	public static String crack (String c)
	{
		double maxScore = Cipher.getQuadScore(c);
		String m = c;
		for (int key = 2; key < c.length(); key++)
		{
			String dec = decrypt(c, key);
			double score = Cipher.getQuadScore(dec);
			if (score > maxScore)
			{
				maxScore = score;
				m = dec;
			}
		}
		return m;
	}
	
	public static void demo ()
	{
		System.out.println("----------------Rail Fence---------------------");
		String m = Cipher.getPlaintext();
		int key = (int) (Math.random() * m.replaceAll("[^A-za-z ]", "").length() / 2.0);
		String c = encrypt(m, key);
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
