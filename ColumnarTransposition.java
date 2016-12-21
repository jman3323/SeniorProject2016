import java.util.*;

public class ColumnarTransposition 
{
	public static final int DEFAULT_ITERATIONS = 51;

	public static String encrypt (String m, String key)
	{
		char[][] grid = new char[(int) Math.ceil((double) m.length() / key.length())][key.length()];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++)
				if (i * grid[i].length + j >= m.length())
					grid[i][j] = 'x';
				else
					grid[i][j] = m.charAt(i * grid[i].length + j);
		String k = "";
		for (char c : key.toCharArray())
			if (!k.contains(c + ""))
				k += c;
		char[] arr = k.toCharArray();
		Arrays.sort(arr);
		String c = "";
		for (char ch : arr)
			for (int i = 0; i < grid.length; i++)
				c += grid[i][k.indexOf(ch)];
		return c;
	}

	public static String decrypt (String c, String key)
	{
		char[][] grid = new char[c.length() / key.length()][key.length()];
		for (int j = 0; j < grid[0].length; j++)
			for (int i = 0; i < grid.length; i++)
				grid[i][j] = c.charAt(j * grid.length + i);
		String k = "";
		ArrayList<Character> arr = new ArrayList<>();
		for (char ch : key.toCharArray())
			if (!k.contains(ch + ""))
			{
				k += ch;
				arr.add(ch);
			}
		Collections.sort(arr);
		String m = "";
		for (int i = 0; i < grid.length; i++)
			for (char ch : k.toCharArray())
				m += grid[i][arr.indexOf(ch)];
		return m;
	}
	
	public static String[] crack (String c, int minKeyLength, int maxKeyLength, int iterations)
	{
		String[] decKey = {c, ""};
		double maxScore = Cipher.getQuadScore(c);
		for (int keyLen = minKeyLength; keyLen <= maxKeyLength; keyLen++)
		{
			String[] newDecKey = hillClimb(c, keyLen, iterations);
			double score = Cipher.getQuadScore(newDecKey[0]);
			if (score > maxScore)
			{
				maxScore = score;
				decKey = newDecKey;
			}
		}
		return decKey;
	}

	public static String[] hillClimb (String c, int keyLength)
	{
		return hillClimb(c, keyLength, DEFAULT_ITERATIONS);
	}

	public static String[] hillClimb (String c, int keyLength, int iterations)
	{
		double maxScoreAll = Cipher.getQuadScore(c);
		String m = c;
		String maxKey = "";
		ArrayList<Long> swaps = new ArrayList<>(), usedSwaps = new ArrayList<>();
		long pow = (long) Math.floor(Math.log10(keyLength - 1)) + 1;
		for (int s0 = 0; s0 < keyLength - 1; s0++)
			for (int s1 = s0 + 1; s1 <= keyLength - 1; s1++)
				swaps.add(s0 * (long) Math.pow(10, pow) + s1);

		ArrayList<Integer> rotations = new ArrayList<>(), usedRotations = new ArrayList<>();
		for (int start = 1; start < keyLength; start++)
			rotations.add(start);

		for (int starts = 0; starts < iterations; starts++)
		{
			swaps.addAll(usedSwaps);
			usedSwaps.clear();
			rotations.addAll(usedRotations);
			usedRotations.clear();
			String key = keyGen(keyLength);
			String dec = decrypt(c, key);

			double maxScore = Cipher.getQuadScore(dec);
			while (swaps.size() > 0 || rotations.size() > 0)
			{
				String newKey = "";
				double probSwap = (double) swaps.size() / (swaps.size() + rotations.size());
				if (Math.random() < probSwap)
				{
					Long swapHash = swaps.remove((int) (Math.random() * swaps.size()));
					usedSwaps.add(swapHash);
					int s0 = (int) (swapHash / (long) Math.pow(10, pow)), s1 = (int) (swapHash % (long) Math.pow(10, pow));
					for (int x = 0; x < key.length(); x++)
						if (x == s0)
							newKey += key.charAt(s1);
						else if (x == s1)
							newKey += key.charAt(s0);
						else
							newKey += key.charAt(x);
				}
				else
				{
					int shift = rotations.remove((int) (Math.random() * rotations.size()));
					usedRotations.add(shift);
					newKey = key.substring(shift) + key.substring(0, shift);
				}
				String newDec = decrypt(c, newKey);
				double score = Cipher.getQuadScore(newDec);
				if (score > maxScore)
				{
					maxScore = score;
					dec = newDec;
					key = newKey;
					swaps.addAll(usedSwaps);
					usedSwaps.clear();
					rotations.addAll(usedRotations);
					usedRotations.clear();
				}
			}

			if (maxScore > maxScoreAll)
			{
				maxScoreAll = maxScore;
				m = dec;
				maxKey = key;
			}
		}
		return new String[] {m, maxKey};
	}

	public static String bruteForce (String c, int maxKeyLength)
	{
		return bruteForce(c, 2, maxKeyLength);
	}

	public static String bruteForce (String c, int minKeyLength, int maxKeyLength)
	{
		if (minKeyLength < 2)
			minKeyLength = 2;
		double maxScore = Cipher.getScore(c);
		String m = c;
		for (int keyLength = minKeyLength; keyLength <= maxKeyLength; keyLength++)
			if (c.length() % keyLength == 0)
			{
				String dec = genPerms("", keyLength, c);
				double score = Cipher.getScore(dec);
				if (score > maxScore)
				{
					maxScore = score;
					m = dec;
				}
			}
		return m;
	}

	private static String genPerms (String cur, int keyLength, String c)
	{
		if (cur.length() == keyLength)
			return decrypt(c, cur);
		double maxScore = -Double.MAX_VALUE;
		String m = "";
		HashSet<Character> used = new HashSet<>();
		for (char ch : cur.toCharArray())
			used.add(ch);
		for (int next = 0; next < keyLength; next++)
		{
			if (used.contains(next + 'A'))
				continue;
			String dec = genPerms(cur + (char) (next + 'A'), keyLength, c);
			double score = Cipher.getScore(dec);
			if (score > maxScore)
			{
				maxScore = score;
				m = dec;
			}
		}
		return m;
	}

	public static String keyGen (int keyLength)
	{
		if (keyLength < 2)
			return "0";
		String key = "";
		ArrayList<Integer> toUse = new ArrayList<>();
		for (int i = 0; i < keyLength; i++)
			toUse.add(i);
		while (toUse.size() > 0)
		{
			int rand = (int) (Math.random() * toUse.size());
			key += (char) (toUse.remove(rand) + 'A');
		}
		return key;
	}
	
	public static void demo (int keyLen, int iterations)
	{
		System.out.println("---------------Columnar Transposition------------------------");
		String m = Cipher.getPlaintext();
		String key = keyGen(keyLen);
		String c = encrypt(m, key);
		System.out.println("Ciphertext: " + c + "\n");
		m = decrypt(c, key);
		long time = System.currentTimeMillis();
		String[] decKey = hillClimb(c, keyLen, iterations);
		time = System.currentTimeMillis() - time;
		String dec = decKey[0];
		System.out.println("Decryption: " + dec + "\n");
		System.out.println("Key found:   " + decKey[1]);
		System.out.println("Correct key: " + key);
		//System.out.println("Correct score: " + Cipher.getQuadScore(m));
		//System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");
		//System.out.println("Runtime: " + (time / 1000.0) + " seconds");
		System.out.println("*******************************************");
		System.out.println();
	}
}
