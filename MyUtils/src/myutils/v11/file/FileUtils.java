package myutils.v11.file;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import myutils.v10.file.SystemUtils;

public class FileUtils {

	// NOTE: all filepaths unless specified to be relative to the current working directory, should be from the root directory. 

	public static String loadAsStringRelative(String file) {
		StringBuilder result = new StringBuilder();
		InputStream is;
		try {
			System.out.println("LOADING FILE: " + file);
			is = FileUtils.class.getResourceAsStream(file);
			BufferedReader fin = new BufferedReader(new InputStreamReader(is));
			String buffer = "";
			while ((buffer = fin.readLine()) != null) {
				result.append(buffer + '\n');
			}
			fin.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();
	}

	// loads img with filepath starting from root; C:
	// assumes file is in /res folder
	public static BufferedImage loadImageRelative(String filepath) {
		String resDirectory = SystemUtils.getWorkingDirectory() + "\\res\\";
		BufferedImage img = null;

		System.out.print("LOADING IMAGE: " + resDirectory + filepath);

		try {
			img = ImageIO.read(new File(resDirectory + filepath));
			System.out.println(" SUCCESS");
		}
		catch (IOException e) {
			System.out.println(" FAILED");
		}

		return img;
	}

	public static File loadFileRelative(String filepath) {
		String resDirectory = SystemUtils.getWorkingDirectory() + "\\res\\";

		System.out.print("LOADING FILE: " + resDirectory + filepath);
		File file = null;
		file = new File(resDirectory + filepath);
		System.out.println(" SUCCESS");

		return file;
	}

	public static String getFileExtension(String path) {
		int lastPeriod = path.lastIndexOf('.');
		return path.substring(lastPeriod + 1);
	}

	public static String removeFileExtension(String path) {
		int lastPeriod = path.lastIndexOf('.');
		return path.substring(0, lastPeriod);
	}

	public static File[] getAllFilesFromDirectory(String path) {
		File folder = new File(path);
		return folder.listFiles();
	}

	public static String[] getAllFilenamesFromDirectory(String path) {
		File[] files = getAllFilesFromDirectory(path);
		String[] names = new String[files.length];
		for (int i = 0; i < names.length; i++) {
			File f = files[i];
			names[i] = f.getName();
		}
		return names;
	}

	public static File[] getAllFilesFromDirectoryRelative(String path) {
		return getAllFilesFromDirectory(SystemUtils.getWorkingDirectory() + "\\res\\" + path);
	}

	public static String[] getAllFilenamesFromDirectoryRelative(String path) {
		return getAllFilenamesFromDirectory(SystemUtils.getWorkingDirectory() + "\\res\\" + path);
	}

	public static File[] openFileExplorer() {
		FileDialog fd = new FileDialog(new JFrame());
		fd.setVisible(true);
		File[] f = fd.getFiles();
		fd.dispose();
		fd.getOwner().dispose(); //have to do because some mem leak issues :shrug:
		return f;
	}

}
