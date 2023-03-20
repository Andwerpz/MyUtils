package myutils.v10.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;

import myutils.v10.file.FileUtils;

public class FontUtils {

	public static Font loadFont(String path) {
		try {
			FileInputStream is = new FileInputStream(FileUtils.loadFile(path));
			Font font = Font.createFont(Font.TRUETYPE_FONT, is);
			return font;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (FontFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Font deriveSize(int size, Font font) {
		return font.deriveFont((float) size);
	}

}
