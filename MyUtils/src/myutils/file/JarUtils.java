package myutils.file;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public class JarUtils {
	//for now, this is mainly to load files that are packaged as a part of a jar. 
	//instead of loading files, and then interpreting them, we load input streams and then interpret them. 

	/**
	 * Loads file from resource package as input stream. 
	 * @param filepath
	 * @return
	 */
	public static InputStream loadInputStream(String filepath) {
		return JarUtils.class.getResourceAsStream(filepath);
	}

	/**
	 * Loads image from resource package
	 * @param filepath
	 * @return
	 */
	public static BufferedImage loadImage(String filepath) {
		System.out.print("LOADING JAR IMAGE: " + filepath);
		try {
			InputStream is = JarUtils.loadInputStream(filepath);
			BufferedImage image = ImageIO.read(JarUtils.loadInputStream(filepath));
			is.close();
			System.out.println(" SUCCESS");
			return image;
		}
		catch (IOException e) {
			System.err.println(" FAILED");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads byte buffer from a resource package
	 * @param filepath
	 * @return
	 */
	public static byte[] loadByteBuffer(String filepath) {
		System.out.print("LOADING JAR BYTE BUFFER: " + filepath);
		try {
			InputStream is = JarUtils.loadInputStream(filepath);
			byte[] buffer;
			buffer = is.readAllBytes();
			is.close();
			System.out.println(" SUCCESS");
			return buffer;
		}
		catch (IOException e) {
			System.err.println(" FAILED");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads a font object from a resource package
	 * 
	 * Currently only works for .ttf or TrueType fonts. 
	 * @param filepath
	 * @return
	 */
	public static Font loadFont(String filepath) {
		System.out.print("LOADING JAR FONT: " + filepath);
		try {
			InputStream is = JarUtils.loadInputStream(filepath);
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			is.close();
			System.out.println(" SUCCESS");
			return font;
		}
		catch (IOException | FontFormatException e) {
			System.err.println(" FAILED");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Loads string from resource package
	 * @param filepath
	 * @return
	 */
	public static String loadString(String filepath) {
		System.out.print("LOADING JAR STRING: " + filepath);
		try {
			StringBuilder result = new StringBuilder();
			InputStream is = JarUtils.loadInputStream(filepath);
			BufferedReader fin = new BufferedReader(new InputStreamReader(is));
			String buffer = "";
			while ((buffer = fin.readLine()) != null) {
				result.append(buffer + '\n');
			}
			fin.close();
			System.out.println(" SUCCESS");
			return result.toString();
		}
		catch (IOException e) {
			System.err.println(" FAILED");
			e.printStackTrace();
		}
		return null;
	}

}
