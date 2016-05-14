package ua.mike.opinta.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;
import ua.mike.opinta.exceptions.MikeException;

public class FileHelper {
	private static Map<String, String> indexFile = new HashMap<>();

	public static boolean filesAreEqual(File fileOldVersion, File fileCurrentVersion) throws MikeException {
		try (FileInputStream fisOldVersion = new FileInputStream(fileOldVersion);
			 FileInputStream fisCurrentVersion = new FileInputStream(fileCurrentVersion)) {

			byte[] data = new byte[(int) fileOldVersion.length()];
			fisOldVersion.read(data);
			fisOldVersion.close();
			String contentOldVersion = new String(data, "UTF-8");

			data = new byte[(int) fileCurrentVersion.length()];
			fisCurrentVersion.read(data);
			fisCurrentVersion.close();
			String contentCurrentVersion = new String(data, "UTF-8");

			return contentCurrentVersion.equals(contentOldVersion);
		} catch (IOException e) {
			throw new MikeException("Can\'t make comparison for file " + fileCurrentVersion.getName(), e);
		}
	}
	
	public static void createFileByUrl(String url) throws MikeException {
		boolean isFolder = "\\".equals(url.substring(url.length() - 1));
		Path path = Paths.get(url.trim());

		if (!Files.exists(path)) {
            try {
                if (isFolder) {
                	Files.createDirectory(path);
                	System.out.println("Directory " + url + " created successfully.");
                } else {
                	Files.createFile(path);
                	System.out.println("File " + url + " created successfully.");
                }
            } catch (IOException e) {
                throw new MikeException("Error while creating the File" + url, e);
            }
        } else {
        	System.out.println("File " + url + " already exists.");
        }
	}

	public static boolean isExist(String url){
		Path path = Paths.get(url.trim());
		return Files.exists(path);
	}

	public static void createDirectory(String url) throws IOException {
		Path path = Paths.get(url.trim());
		Files.createDirectory(path);
	}

	public static List<String> getFileList(String url) throws MikeException {
		Path path = Paths.get(url.trim());
		List<String> fileList = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)){
			for (Path pathToFile : stream) {
				fileList.add(pathToFile.toString());
			}
		} catch (IOException e) {
			throw new MikeException("Can\'t get list of files", e);
		}
		return fileList;
	}

	public static Map<String, String> getPropertiesFromFile(String url) throws IOException {
		if (indexFile.isEmpty()) {
			FileInputStream indexFileStream =  new FileInputStream(url);
			Properties properties = new Properties();
			properties.load(indexFileStream);
			properties.forEach((property, value) -> indexFile.put((String)property, (String)value));
		}
		return indexFile;
	}

	public static String getFileHash(String pathUrl) throws IOException {
		FileInputStream fis = new FileInputStream(new File(pathUrl));
		String md5 = DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}
}
