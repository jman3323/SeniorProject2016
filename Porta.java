
public class Porta 
{
	private static String[] tableau = 
		{
		"nopqrstuvwxyzabcdefghijklm",
		"opqrstuvwxyznmabcdefghijkl",
		"pqrstuvwxyznolmabcdefghijk",
		"qrstuvwxyznopklmabcdefghij",
		"rstuvwxyznopqjklmabcdefghi",
		"stuvwxyznopqrijklmabcdefgh",
		"tuvwxyznopqrshijklmabcdefg",
		"uvwxyznopqrstghijklmabcdef",
		"vwxyznopqrstufghijklmabcde",
		"wxyznopqrstuvefghijklmabcd",
		"xyznopqrstuvwdefghijklmabc",
		"yznopqrstuvwxcdefghijklmab",
		"znopqrstuvwxybcdefghijklma"
		};
	
	public static String encrypt (String m, String key)
	{
		key = key.replaceAll("[^A-za-z]", "").toLowerCase();
		String c = "";
		int offset = 0;
		for (int i = 0; i < m.length(); i++)
		{
			char ch = m.charAt(i);
			if (ch >= 'A' && ch <= 'Z')
				c += (char) (tableau[(key.charAt(offset++ % key.length()) - 'a') / 2].charAt(ch - 'A') - 'a' + 'A');
			else if (ch >= 'a' && ch <= 'z')
				c += tableau[(key.charAt(offset++ % key.length()) - 'a') / 2].charAt(ch - 'a');
			else
				c += ch;
		}
		return c;
	}
	
	public static String decrypt (String c, String key)
	{
		return encrypt(c, key);
	}
	
	public static String[] comboCrack (String c, int minKeyLength, int maxKeyLength)
	{
		String[] decKey = crack(c, minKeyLength, maxKeyLength);
		return crackGuessKey(c, decKey[1]);
	}
	
	public static String[] crack (String c, int minKeyLength, int maxKeyLength)
	{
		double maxScoreAll = Cipher.getQuadScore(c);
		String m = c;
		String maxKey = "";
		String cStripped = c.replaceAll("[^A-za-z]", "").toLowerCase();
		if (minKeyLength < 1)
			minKeyLength = 1;
		if (maxKeyLength >= cStripped.length())
			maxKeyLength = cStripped.length() - 1;
		for (int keyLen = minKeyLength; keyLen <= maxKeyLength; keyLen++)
		{
			double[] pairScores = new double[keyLen];
			String[] pairChars = new String[keyLen];
			for (int pair = 0; pair < pairScores.length; pair++)
			{
				pairScores[pair] = -Double.MAX_VALUE;
				pairChars[pair] = "";
				for (char ch0 = 'a'; ch0 <= 'z'; ch0 += 2)
					for (char ch1 = 'a'; ch1 <= 'z'; ch1 += 2)
					{
						double score = 0, total = 0;
						for (int i = pair; i + 2 <= cStripped.length(); i += keyLen, total++)
							score += Cipher.getBiScore(decrypt(cStripped.substring(i, i + 2), ch0 + "" + ch1));
						score = score / total;
						if (score > pairScores[pair])
						{
							pairScores[pair] = score;
							pairChars[pair] = ch0 + "" + ch1;
						}
					}
			}
			String key = "";
			for (int i = 0; i < keyLen; i++)
				if (pairScores[i] > pairScores[(i - 1 + keyLen) % keyLen])
					key += pairChars[i].charAt(0);
				else
					key += pairChars[(i - 1 + keyLen) % keyLen].charAt(1);
			String dec = decrypt(c, key);
			double score = Cipher.getQuadScore(dec);
			if (score > maxScoreAll)
			{
				maxScoreAll = score;
				m = dec;
				maxKey = key;
			}
		}
		return new String[] {m, maxKey};
	}
	
	public static String[] crack2 (String c, int minKeyLength, int maxKeyLength)
	{
		String cStripped = c.replaceAll("[^A-za-z]", "").toLowerCase();
		double maxScoreAll = Cipher.getQuadScore(c);
		String maxKey = "";
		if (minKeyLength < 1)
			minKeyLength = 1;
		if (maxKeyLength > cStripped.length())
			maxKeyLength = cStripped.length();
		for (int keyLen = minKeyLength; keyLen <= maxKeyLength; keyLen++)
		{
			String key = keyGen(keyLen);
			int lastChanged = -1;
			double maxScore = -Double.MAX_VALUE;
			for (int i = 0; i != lastChanged; i = (i + 1) % keyLen)
			{
				double maxScoreChar = -Double.MAX_VALUE;
				String maxKeyChar = "";
				for (char ch = 'a'; ch <= 'z'; ch += 2)
				{
					String newKey = key.substring(0, i) + ch + key.substring(i + 1);
					String newDec = decrypt(c, newKey);
					double score = Cipher.getQuadScore(newDec);
					if (score > maxScoreChar)
					{
						maxScoreChar = score;
						maxKeyChar = newKey;
					}
				}
				if (maxScoreChar > maxScore)
				{
					maxScore = maxScoreChar;
					key = maxKeyChar;
					lastChanged = i;
				}
			}
			if (maxScore > maxScoreAll)
			{
				maxScoreAll = maxScore;
				maxKey = key;
			}
		}
		return new String[] {decrypt(c, maxKey), maxKey};
	}
	
	public static String[] crackGuessKey (String c, String startKey)
	{
		double maxScore = Cipher.getQuadScore(c);
		String key = startKey;
		int lastChanged = -1;
		for (int i = 0; i != lastChanged; i = (i + 1) % startKey.length())
		{
			double maxScoreChar = -Double.MAX_VALUE;
			String maxKeyChar = "";
			for (char ch = 'a'; ch <= 'z'; ch += 2)
			{
				String newKey = key.substring(0, i) + ch + key.substring(i + 1);
				String newDec = decrypt(c, newKey);
				double score = Cipher.getQuadScore(newDec);
				if (score > maxScoreChar)
				{
					maxScoreChar = score;
					maxKeyChar = newKey;
				}
			}
			if (maxScoreChar > maxScore)
			{
				maxScore = maxScoreChar;
				key = maxKeyChar;
				lastChanged = i;
			}
		}
		return new String[] {decrypt(c, key), key};
	}
	
	public static String keyGen (int keyLength)
	{
		String key = "";
		while (key.length() < keyLength)
			key += (char) (Math.random() * 26 + 'a');
		return key;
	}
	
	public static void demo ()
	{
		System.out.println("---------------Porta------------------------");
		String m = Cipher.getPlaintext();
		int keyLen = (int) (Math.random() * m.replaceAll("[^A-Za-z]", "").length() / 7.0) + 1;
		String key = keyGen(keyLen);
		String c = encrypt(m, key);
		System.out.println("Ciphertext: " + c + "\n");
		String[] decKey = comboCrack(c, key.length() / 2, key.length());
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
		System.out.println("*******************************************");
		System.out.println();
	}
}
