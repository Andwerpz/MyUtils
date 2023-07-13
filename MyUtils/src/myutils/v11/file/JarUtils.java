package myutils.v11.file;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class JarUtils {
	//for now, this is mainly to load files that are packaged as a part of a jar. 
	//instead of loading files, and then interpreting them, we load input streams and then interpret them. 

	public static InputStream loadInputStream(String filepath) {
		return JarUtils.class.getResourceAsStream(filepath);
	}

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

	public static byte[] loadByteBuffer(String filepath) {
		System.out.println("LOADING JAR BYTE BUFFER: " + filepath);
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

}
