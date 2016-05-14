package ua.mike.opinta.domain;

import java.lang.reflect.Field;

import ua.mike.opinta.exceptions.MikeException;
import ua.mike.opinta.helpers.FileHelper;

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
//		    String fieldName = field.getName();
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
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
