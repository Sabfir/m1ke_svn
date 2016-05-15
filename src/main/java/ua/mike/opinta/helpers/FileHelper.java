package ua.mike.opinta.helpers;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.codec.digest.DigestUtils;
import ua.mike.opinta.exceptions.MikeException;

public class FileHelper {

	public static boolean compareFiles(File fileOldVersion, File fileCurrentVersion) throws MikeException {
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

	public static Path createFileByUrl(String url) throws MikeException {
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
		return path;
	}

	public static boolean isExist(String url){
		Path path = Paths.get(url.trim());
		return Files.exists(path);
	}

	public static void createDirectory(String url) throws IOException {
		Path path = Paths.get(url.trim());
		Files.createDirectory(path);
	}

	public static List<String> getFileList(String url, String filterPattern) throws MikeException {
		Path path = Paths.get(url.trim());
		List<String> fileList = new ArrayList<>();

		DirectoryStream.Filter filter = new DirectoryStream.Filter() {
			@Override
			public boolean accept(Object entry) throws IOException {
				boolean isMatch = true;
				if (!filterPattern.isEmpty()) {
					isMatch = !((Path)entry).toAbsolutePath().toString().endsWith(filterPattern);
				}
				return isMatch;
			}
		};
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, filter)){
			for (Path pathToFile : stream) {
				if (pathToFile.toFile().isDirectory()) {
					fileList.addAll(getFileList(pathToFile.toAbsolutePath().toString(), filterPattern));
				} else {
					fileList.add(pathToFile.toString());
				}
			}
		} catch (IOException e) {
			throw new MikeException("Can\'t get list of files", e);
		}
		return fileList;
	}

	public static Map<String, String> getPropertiesFromFile(String url) throws IOException {
		Map<String, String> listProperties = new HashMap<>();
			FileInputStream indexFileStream =  new FileInputStream(url);
			Properties properties = new Properties();
			properties.load(indexFileStream);
			indexFileStream.close();
			properties.forEach((property, value) -> listProperties.put((String)property, (String)value));
		return listProperties;
	}

	public static void addPropertiesToFile(String url, Map<String, String> listProperties, boolean isAddToFile) throws MikeException {
		Properties properties = new Properties();
		if(isAddToFile) {
			try {
				properties.putAll(getPropertiesFromFile(url));
			} catch (IOException e) {
				throw new MikeException("Can\'t read file " + url, e);
			}
		}
		try (FileOutputStream fileOutputStream = new FileOutputStream(url)) {
			properties.putAll(listProperties);
			properties.store(fileOutputStream, "");
		} catch (IOException e) {
			throw new MikeException("Can\'t write file " + url, e);
		}
	}

	public static String getFileHash(String pathUrl) throws IOException {
		FileInputStream fis = new FileInputStream(new File(pathUrl));
		String md5 = DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}

	public static void removeDirectory(String url) throws IOException {
		Path path = Paths.get(url.trim());
		Files.deleteIfExists(path);
	}


//	public static boolean copyFile(String source, String destination) {
//		try {
//			Files.copy(Paths.get(source), Paths.get(destination));
//			return true;
//		} catch (IOException e) {
//			return false;
//		}
//	}
}
