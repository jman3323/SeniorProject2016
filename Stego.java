import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import javax.sound.sampled.*;

public class Stego 
{
	public static final String PRINTABLE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&\\\'()*+,-./:;<=>?@[\\]^_`{|}~ \t\n\r";
	
	/**
	 * Encrypts an image using the Least Significant Bits. 
	 * In the case that the image is too small to fit the entire message, the message is truncated to fit
	 * @param img host image in which to hide the message
	 * @param message message to hide in the host image
	 * @param algo byte order in which to encode. Valid algorithms include at most one of each symbol r, g, and b (ex. rgb, bgr, r, g...). Invalid algorithms will cause a null return
	 * @return a new BufferedImage instance of the encrypted image
	 */
	public static BufferedImage encryptLSB (BufferedImage img, String message, String algo)
	{
		if (!algo.matches("^([rgb])(?:(?!\\1)([rgb])|$)(?:(?!\\1)(?!\\2)[rgb]|$)$"))
			return null;
		BufferedImage enc = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		for (int x = 0; x < img.getWidth(); x++)
			for (int y = 0; y < img.getHeight(); y++)
				enc.setRGB(x, y, img.getRGB(x, y));
		byte[] msgBin = new byte[message.length() * 8];
		for (int i = 0; i < message.length(); i++)
			for (int j = 0, c = message.charAt(i); j < 8; j++, c >>= 1)
				msgBin[i * 8 + 7 - j] = (byte) (c % 2);
		for (int bit = 0, row = 0; row < enc.getHeight() && bit < msgBin.length; row++)
			for (int col = 0; col < enc.getWidth() && bit < msgBin.length; col++)
			{
				int rgb = enc.getRGB(col, row);
				int red = (rgb >> 16) & 0xff, green = (rgb >> 8) & 0xff, blue = rgb & 0xff;
				int[] colorsOrig = {red, green, blue};
				String index = "rgb";
				int[] colors = new int[algo.length()];
				for (int i = 0; i < colors.length; i++)
					colors[i] = colorsOrig[index.indexOf(algo.charAt(i))];
				colors[0] = (colors[0] >> 1 << 1) + msgBin[bit++];
				if (bit < msgBin.length && 1 < colors.length)
					colors[1] = (colors[1] >> 1 << 1) + msgBin[bit++];
				if (bit < msgBin.length && 2 < colors.length)
					colors[2] = (colors[2] >> 1 << 1) + msgBin[bit++];
				int newR = (algo.indexOf("r") != -1 ? colors[algo.indexOf("r")] : red);
				int newG = (algo.indexOf("g") != -1 ? colors[algo.indexOf("g")] : green);
				int newB = (algo.indexOf("b") != -1 ? colors[algo.indexOf("b")] : blue);
				int newRGB = (newR << 16) + (newG << 8) + newB;
				enc.setRGB(col, row, newRGB);
			}
		return enc;
	}

	/**
	 * Decrypts the image using the Least Significant Bits. 
	 * An ArrayList<String> is returned with all consecutive sequences of printable characters
	 * @param img
	 * @param algo byte order in which to decode. Valid algorithms include at most one of each symbol r, g, and b (ex. rgb, bgr, r, g...). Invalid algorithms will cause a null return
	 * @return a list of possible plaintexts
	 */
	public static ArrayList<String> decryptLSB(BufferedImage img, String algo)
	{
		if (!algo.matches("^([rgb])(?:(?!\\1)([rgb])|$)(?:(?!\\1)(?!\\2)[rgb]|$)$"))
			return null;
		ArrayList<String> result = new ArrayList<>();
		String dec = "";
		int c = 0, count = 0;
		for (int row = 0; row < img.getHeight(); row++)
			for (int col = 0; col < img.getWidth(); col++)
			{
				int rgb = img.getRGB(col, row);
				int red = (rgb >> 16) & 0xff, green = (rgb >> 8) & 0xff, blue = rgb & 0xff;
				int[] colorsOrig = {red, green, blue};
				String index = "rgb";
				int[] colors = new int[algo.length()];
				for (int i = 0; i < colors.length; i++)
					colors[i] = colorsOrig[index.indexOf(algo.charAt(i))];
				for (int i = 0; i < colors.length; i++)
				{
					c += colors[i] % 2 * Math.pow(2, 7 - count);
					count++;
					if (count == 8)
					{
						if (PRINTABLE.contains("" + (char) c))
							dec += (char) c;
						else
							if (!dec.equals(""))
							{
								result.add(dec);
								dec = "";
							}
						count = 0;
						c = 0;
					}
				}
			}
		if (!dec.equals(""))
			result.add(dec);
		return result;
	}

	/**
	 * Encrypts a sound file using the Least Significant Bits. 
	 * In the case that the image is too small to fit the entire message, the message is truncated to fit
	 * @param sound
	 * @param message
	 * @return a new AudioInputStream that represents the modified audio
	 */
	public static AudioInputStream encryptLSB (AudioInputStream sound, String message)
	{
		byte[] msgBin = new byte[message.length() * 8];
		for (int i = 0; i < message.length(); i++)
			for (int j = 0, c = message.charAt(i); j < 8; j++, c >>= 1)
				msgBin[i * 8 + 7 - j] = (byte) (c % 2);
		try
		{
			byte[] sampled = new byte[sound.available()];
			sound.read(sampled);
			for (int i = 0; i < sampled.length && i < msgBin.length; i++)
				sampled[i] = (byte) ((sampled[i] >> 1 << 1) + msgBin[i]);
			return new AudioInputStream(new ByteArrayInputStream(sampled), sound.getFormat(), sound.getFrameLength());
		}
		catch (Exception e) {return null;}
	}

	/**
	 * Decrypts a sound file using the Least Significant Bits. 
	 * An ArrayList<String> is returned with all consecutive sequences of printable characters
	 * @param sound
	 * @return a list of possible plaintexts
	 */
	public static ArrayList<String> decryptLSB (AudioInputStream sound)
	{
		try
		{
			ArrayList<String> result = new ArrayList<>();
			String dec = "";
			byte[] buffer = new byte[8];
			while (sound.available() > 0)
			{
				sound.read(buffer);
				int c = 0;
				for (int i = 0; i < buffer.length; i++)
					c = (c << 1) | (buffer[i] & 0x1);
				if (PRINTABLE.contains("" + (char) c))
					dec += (char) c;
				else
					if (!dec.equals(""))
					{
						result.add(dec);
						dec = "";
					}
			}
			if (!dec.equals(""))
				result.add(dec);
			return result;
		}
		catch (Exception e) {return null;}
	}
}
