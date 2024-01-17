package myutils.file;

import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class FileUtils {
	// NOTE: all filepaths unless specified to be relative to the current working directory, should be from the root directory. 
	// if it is relative, it's relative to the current working directory, not the '\res' folder as it used to be. 

	public static byte[] convertInputStreamToByteArray(InputStream input) throws IOException {
		return input.readAllBytes();
	}

	public static byte[] convertFileToByteArray(File f) throws IOException {
		byte[] arr = Files.readAllBytes(f.toPath());
		return arr;
	}

	/**
	 * Takes in a filepath relative to the current working directory, and returns the absolute filepath. 
	 * @param relativeFilepath
	 * @return
	 */
	public static String generateAbsoluteFilepath(String relativeFilepath) {
		String resDirectory = SystemUtils.getWorkingDirectory();
		return resDirectory + relativeFilepath;
	}

	public static File loadFile(String filepath) {
		System.out.print("LOADING FILE: " + filepath);
		File file = null;
		file = new File(filepath);
		System.out.println(" SUCCESS");

		return file;
	}

	public static String loadAsString(String filepath) {
		return readFileToString(loadFile(filepath));
	}

	public static File loadFileRelative(String filepath) {
		File file = loadFile(FileUtils.generateAbsoluteFilepath(filepath));
		return file;
	}

	public static String loadAsStringRelative(String filepath) {
		return readFileToString(loadFileRelative(filepath));
	}

	public static String readFileToString(File f) {
		StringBuilder result = new StringBuilder();
		try {
			System.out.println("LOADING FILE: " + f);
			BufferedReader fin = new BufferedReader(new FileReader(f));
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

	public static BufferedImage loadImage(File file) {
		BufferedImage img = null;
		System.out.print("LOADING IMAGE: " + file.getPath());

		try {
			img = ImageIO.read(file);
			System.out.println(" SUCCESS");
		}
		catch (IOException e) {
			System.out.println(" FAILED");
		}

		return img;
	}

	public static BufferedImage loadImage(String filepath) {
		return loadImage(FileUtils.loadFile(filepath));
	}

	public static BufferedImage loadImageRelative(String filepath) {
		BufferedImage image = loadImage(FileUtils.loadFileRelative(filepath));
		return image;
	}

	public static String getFileExtension(String path) {
		int lastPeriod = path.lastIndexOf('.');
		return path.substring(lastPeriod + 1);
	}

	public static String getFileExtension(File f) {
		return getFileExtension(f.getPath());
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
		return getAllFilesFromDirectory(FileUtils.generateAbsoluteFilepath(path));
	}

	public static String[] getAllFilenamesFromDirectoryRelative(String path) {
		return getAllFilenamesFromDirectory(FileUtils.generateAbsoluteFilepath(path));
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
