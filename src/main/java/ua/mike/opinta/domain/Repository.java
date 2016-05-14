package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;
import ua.mike.opinta.helpers.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Repository {
	private String path;
	protected static final String RELETIVE_PATH_NAME = ".mike";
	protected static final String RELETIVE_PATH_HEAD = ".mike\\HEAD";
	protected static final String RELETIVE_PATH_REFS = ".mike\\refs\\";
	protected static final String RELETIVE_PATH_INDEX = ".mike\\index";
	
	public Repository(String path) {
		this.path = path;
	}
	
	public boolean isInitialized() {
		// TODO check whether folder ".mike" exists
		return true;
	}
	
	public void initialize() throws MikeException {
		FileHelper.createFileByUrl(RELETIVE_PATH_NAME);
		FileHelper.createFileByUrl(RELETIVE_PATH_HEAD);
		FileHelper.createFileByUrl(RELETIVE_PATH_REFS);
		FileHelper.createFileByUrl(RELETIVE_PATH_INDEX);
		
		
//		Class<? extends Repository> clazz = this.getClass(); 
//		Field[] fields = clazz.getDeclaredFields(); 
//		
//		for (Field field : fields) { 
//		    String fieldName = field.getvalueName();
//		    if (fieldName.toUpperCase().contains("RELETIVE_PATH_")) {
//		    	// TODO create file or folder. Folder ends with \\
//		    	try {
//					FileHelper.createFileByUrl((String) field.get(this));
//				} catch (Exception e) {
//					throw new MikeException("", e);
//				}
//		    }
//		} 
	}
	
	public String getCurentBranchName() {
		// TODO read first not empty line from file RELETIVE_PATH_HEAD. E.g. "ref: refs/dev" means that the branch name is "dev"
		return "";
	}

	public boolean isBranchExist(String branch){
		return FileHelper.isExist(getPath() + RELETIVE_PATH_REFS + branch);
	}

	public boolean createBranch(String branch) {
		boolean status;
		try {
			FileHelper.createDirectory(getPath() + RELETIVE_PATH_REFS + branch);
			status = true;
		} catch (IOException e) {
			status = false;
		}
		return status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getChangedFiles() {
		List<String> fileList;
		try {
			fileList = FileHelper.getFileList(getPath());
		} catch (MikeException e) {
			fileList = new ArrayList<String>();
		}
		for (String fileUrl : fileList) {
			try {
				String fileHash = getFileHash(fileUrl);
				if (!FileHelper.filesAreEqual(new File(fileUrl), new File(getPath() + RELETIVE_PATH_REFS + "!!!!!!!" + fileHash))) {
					fileList.add(fileUrl);
				}
			} catch (MikeException e) {
				//exclude file....
			}
		}
		return fileList;
	}

	private String getFileHash(String pathUrl) throws MikeException {
		String fileHash;
		try {
			Map<String, String> propertiesFromFile = FileHelper.getPropertiesFromFile(getPath() + RELETIVE_PATH_INDEX);
			fileHash = propertiesFromFile.get(pathUrl);
			if (fileHash == null) {
				fileHash = FileHelper.getFileHash(pathUrl);
			}
		} catch (IOException e) {
			throw new MikeException("Can't compute hash for " + pathUrl, e);
		}
		return fileHash;
	}

	public Map<String, String> getMapFilesHash(List<String> changedFiles) {
		return null;
	}

	public void addFileCommit(String key, String value) {

	}
}
