import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.sound.sampled.*;

public class Driver 
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("Loading n grams...");
		Cipher.loadNGrams();
		System.out.println("Done.\n");
		
		Caesar.demo();
		RailFence.demo();
		Substitution.demo();
		ColumnarTransposition.demo(17, 51);
		Vigenere.demo();
		Autokey.demo();
		Beaufort.demo();
		Porta.demo();
		
		
		/*String m = "Hello. Today I am going to talk about ciphers. Ciphers are cool. They do stuff to other stuff to make it different stuff. Very cool. I am trying to write a very long message so that I have a nice good large sample with which to work with. Good data is helpful to have.";
		String c = "", key = "", dec = "";
		String[] decKey;
		int keyLen;*/
		
		/*System.out.println("----------------Caesar---------------------");
		String c = Caesar.encrypt(m, 17);
		System.out.println(c);
		System.out.println(Caesar.decrypt(c, 17));
		System.out.println(Caesar.crack(c));
		System.out.println("\n");*/
		
		/*System.out.println("----------------Substitution---------------------");
		m = Cipher.getPlaintext();
		key = Substitution.keyGen();
		c = Substitution.encrypt(m, key);
		System.out.println(c);
		System.out.println(Substitution.decrypt(c, key));
		dec = Substitution.crack(c);
		System.out.println(dec);
		System.out.println("Correct score: " + Cipher.getQuadScore(m));
		System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");
		System.out.println("\n");*/
		
		/*System.out.println("----------------Rail Fence---------------------");
		c = RailFence.encrypt(m, 17);
		System.out.println(c);
		System.out.println(RailFence.decrypt(c, 17));
		System.out.println(RailFence.crack(c));
		System.out.println("\n");*/
		
		/*System.out.println("---------------Columnar Transposition------------------------");
		m = Cipher.getPlaintext();
		keyLen = 25;
		key = ColumnarTransposition.keyGen(keyLen);
		c = ColumnarTransposition.encrypt(m, key);
		System.out.println(c);
		m = ColumnarTransposition.decrypt(c, key);
		System.out.println(m);
		long time = System.currentTimeMillis();
		decKey = ColumnarTransposition.hillClimb(c, keyLen);
		time = System.currentTimeMillis() - time;
		dec = decKey[0];
		System.out.println(dec);
		System.out.println("Key found:   " + decKey[1]);
		System.out.println("Correct key: " + key);
		System.out.println("Correct score: " + Cipher.getQuadScore(m));
		System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");
		System.out.println("Runtime: " + (time / 1000.0) + " seconds");*/
		
		/*c = ".aou.aTueodsyat  oa eittapvHaikphodouer.lyi at  phrost m t rhfr it movm vg tohtfedolipc ttkeforrysaadmwoGi xeyn heoo f e .itlg nlliko oT t Ceef mfuc  eeteosh .au.l gaerl ofinV neoeIiaec dh ogaci y saffotwrsh oa w  lxo tos  tht  r  agshegw idlalI brs.st tteIg n  cr hw eh";
		key = "BEGJIACFHD";
		System.out.println(ColumnarTransposition.decrypt(c, key));
		System.out.println(key);*/
		
		/*System.out.println("---------------Vigenere------------------------");
		m = Cipher.getPlaintext();
		keyLen = 17;
		key = Vigenere.keyGen(keyLen);
		m = "This is a very secret message. I hope no one finds out about it."; key = "coolkeydude";
		c = Vigenere.encrypt(m, key);
		System.out.println(c);
		System.out.println(Vigenere.decrypt(c, key));
		//decKey = Vigenere.comboCrack(c, 3, key.length());
		decKey = Vigenere.crack(c, key.length(), key.length());
		System.out.println(decKey[0] + "\nKey found:   " + decKey[1]);
		decKey = Vigenere.crackGuessKey(c, decKey[1]);
		dec = decKey[0];
		System.out.println(dec);
		System.out.println("Key found:   " + decKey[1]);
		System.out.println("Correct key: " + key);
		System.out.println("Correct score: " + Cipher.getQuadScore(m));
		System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");*/
		
		/*System.out.println("---------------Autokey------------------------");
		m = Cipher.getPlaintext();
		keyLen = 17;
		key = Autokey.keyGen(keyLen);
		c = Autokey.encrypt(m, key);
		System.out.println(c);
		System.out.println(Autokey.decrypt(c,  key));
		decKey = Autokey.comboCrack(c, 3, key.length());
		dec = decKey[0];
		System.out.println(dec);
		System.out.println("Key found:   " + decKey[1]);
		System.out.println("Correct key: " + key);
		System.out.println("Correct score: " + Cipher.getQuadScore(m));
		System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");*/
		
		/*System.out.println("---------------Beaufort------------------------");
		m = Cipher.getPlaintext();
		keyLen = 17;
		key = Autokey.keyGen(keyLen);
		c = Beaufort.encrypt(m, key);
		System.out.println(c);
		System.out.println(Beaufort.decrypt(c,  key));
		decKey = Beaufort.comboCrack(c, 3, key.length());
		dec = decKey[0];
		System.out.println(dec);
		System.out.println("Key found:   " + decKey[1]);
		System.out.println("Correct key: " + key);
		System.out.println("Correct score: " + Cipher.getQuadScore(m));
		System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");*/
		
		/*System.out.println("---------------Porta------------------------");
		m = Cipher.getPlaintext();
		keyLen = 17;
		key = Autokey.keyGen(keyLen);
		c = Porta.encrypt(m, key);
		System.out.println(c);
		System.out.println(Porta.decrypt(c, key));
		decKey = Porta.comboCrack(c, 3, key.length());
		dec = decKey[0];
		System.out.println(dec);
		System.out.println("Key found:   " + decKey[1]);
		System.out.println("Correct key: " + key);
		System.out.println("Correct score: " + Cipher.getQuadScore(m));
		System.out.println("Produced score: " + Cipher.getQuadScore(dec));
		if (dec.equals(m))
			System.out.println("Decryption successful.");
		else
			System.out.println("Decryption failed.");*/
		
		
		
		
		
		
		
		//**************************************************
		// Setup stuff
		//**************************************************
		/*String algo = "rgb";
		BufferedImage img = ImageIO.read(new File("test.png"));
		BufferedImage imgOut = Stego.encryptLSB(img, "Hello there this is a test...", algo);
		ImageIO.write(imgOut, "png", new File("testOut.png"));
		BufferedImage enc = ImageIO.read(new File("10111001.png"));
		String dec = Stego.decryptLSB(enc, algo);
		System.out.println(dec);*/

		/*AudioInputStream in = AudioSystem.getAudioInputStream(new File("mixed.wav"));
		AudioInputStream out = Stego.encryptLSB(in, "Hello there this is a super secret message");
		AudioSystem.write(out, AudioSystem.getAudioFileFormat(new File("mixed.wav")).getType(), new File("testOut.wav"));
		AudioInputStream enc = AudioSystem.getAudioInputStream(new File("testOut.wav"));
		System.out.println(Stego.decryptLSB(enc).substring(0, 51));*/

		/*long start = System.currentTimeMillis();
		BufferedImage bane = ImageIO.read(new File("10111001.png"));
		System.out.println(Stego.decryptLSB(bane, "bgr").get(0));
		System.out.println(System.currentTimeMillis() - start);*/

		/*BufferedImage mp = ImageIO.read(new File("montyPython.png"));
		mp = Stego.encryptLSB(mp, "nudge nudge wink wink say no more say no more", "rgb");
		ImageIO.write(mp, "png", new File("testOut.png"));*/

		/* Read in bigrams
		Scanner scan = new Scanner(new File("english_bigrams_1.txt"));
		double[][] map = new double[26][26];
		while (scan.hasNextLine())
		{
			String[] in = scan.nextLine().split(" ");
			map[in[0].charAt(0) - 'A'][in[0].charAt(1) - 'A'] = Integer.parseInt(in[1]);
		}
		double total = 0;
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				total += map[i][j];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				map[i][j] = Math.log(map[i][j] / total);
		System.out.println(Arrays.deepToString(map).replace('[', '{').replace(']', '}').replaceAll("}, ", "},\n"));*/

		/*Read in trigrams
		Scanner scan = new Scanner(new File("english_trigrams.txt"));
		double[][][] map = new double[26][26][26];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
					map[i][j][k] = 0;
		while (scan.hasNextLine())
		{
			String[] in = scan.nextLine().split(" ");
			map[in[0].charAt(0) - 'A'][in[0].charAt(1) - 'A'][in[0].charAt(2) - 'A'] = Integer.parseInt(in[1]);
		}
		double total = 0;
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
					total += map[i][j][k];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
					if (map[i][j][k] == 0)
						map[i][j][k] = -Double.MAX_VALUE;
					else
						map[i][j][k] = Math.log(map[i][j][k] / total);
		FileWriter w = new FileWriter(new File("trigrams.txt"));
		for (double[][] a : map)
			for (double[] a1 : a)
				w.write(Arrays.toString(a1).replaceAll("[\\[\\]]", "") + System.lineSeparator());
		w.close();*/

		/*Read in quadgrams
		Scanner scan = new Scanner(new File("english_quadgrams.txt"));
		double[][][][] map = new double[26][26][26][26];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
					for (int l = 0; l < map[i][j][k].length; l++)
						map[i][j][k][l] = 0;
		while (scan.hasNextLine())
		{
			String[] in = scan.nextLine().split(" ");
			map[in[0].charAt(0) - 'A'][in[0].charAt(1) - 'A'][in[0].charAt(2) - 'A'][in[0].charAt(3) - 'A'] = Integer.parseInt(in[1]);
		}
		double total = 0;
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
					for (int l = 0; l < map[i][j][k].length; l++)
						total += map[i][j][k][l];
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
					for (int l = 0; l < map[i][j][k].length; l++)
						if (map[i][j][k][l] == 0)
							map[i][j][k][l] = -Double.MAX_VALUE;
						else
							map[i][j][k][l] = Math.log(map[i][j][k][l] / total);
		FileWriter w = new FileWriter(new File("quadgrams.txt"));
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[i].length; j++)
				for (int k = 0; k < map[i][j].length; k++)
				{
					String t = "";
					for (int l = 0; l < map[i][j][k].length - 1; l++)
						t += map[i][j][k][l] + ", ";
					t += map[i][j][k][25] + System.lineSeparator();
					w.write(t);
				}
		w.close();*/
		
		/*String stuffs = "E 529117365 T 390965105 A 374061888 O 326627740 I 320410057 N 313720540 S 294300210 R 277000841 H 216768975 L 183996130 D 169330528 C 138416451 U 117295780 M 110504544 F 95422055 G 91258980 P 90376747 W 79843664 Y 75294515 B 70195826 V 46337161 K 35373464 J 9613410 X 8369915 Z 4975847 Q 4550166";
		String[] stuff = stuffs.split(" ");
		long[] mono = new long[26];
		double total = 0;
		for (int i = 0; i < stuff.length; i += 2)
		{
			mono[stuff[i].charAt(0) - 'A'] = Long.parseLong(stuff[i + 1]);
			total += Long.parseLong(stuff[i + 1]);
		}
		System.out.print("{");
		for (long l : mono)
			System.out.print(Math.log(l / total) + ", ");
		System.out.println();*/
	}
}
