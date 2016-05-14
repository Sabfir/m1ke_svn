package ua.mike.opinta.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ua.mike.opinta.exceptions.MikeException;

public class FileHelper {
	public static boolean filesAreEqual(File file1, File file2) {
		// TODO compare file
		
		return true;
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

}
