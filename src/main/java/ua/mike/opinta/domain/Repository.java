package ua.mike.opinta.domain;

import org.apache.commons.transaction.file.FileResourceManager;
import org.apache.commons.transaction.file.ResourceManagerException;
import org.apache.commons.transaction.file.ResourceManagerSystemException;
import org.apache.commons.transaction.util.Log4jLogger;
import org.apache.log4j.Logger;
import ua.mike.opinta.exceptions.MikeException;
import ua.mike.opinta.helpers.FileHelper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Repository {
	private String path;
	public final String RELETIVE_PATH_NAME = ".mike";
	public final String RELETIVE_PATH_HEAD = ".mike\\HEAD";
	public final String RELETIVE_PATH_REFS = ".mike\\refs\\";
	public final String RELETIVE_PATH_INDEX = ".mike\\index";
	public final String RELETIVE_PATH_TEMP = ".mike\\temp\\";
	private final String HEAD_CONTENT_KEY = "ref";
	private final String HEAD_CONTENT_VALUE = "master";
	private final String INITIALIZATION_INFO = "m1ke found there was no branch here \'master\' branch was chosen";
	private static Map<String, String> indexFile = new HashMap<>();
	FileResourceManager frm;

	public Repository(String path) throws MikeException {
		setPath(path);
	}

	public Repository() {}

	public boolean isInitialized() {
		return FileHelper.isExist(getPath() + RELETIVE_PATH_NAME);
	}

	public void initialize() throws MikeException {
		FileHelper.createFileByUrl(RELETIVE_PATH_NAME);
		FileHelper.createFileByUrl(RELETIVE_PATH_REFS);
		FileHelper.createFileByUrl(RELETIVE_PATH_INDEX);
		String pathHeadFile = FileHelper.createFileByUrl(getPath() + RELETIVE_PATH_HEAD).toString();
		Map<String, String> headFileContent = new HashMap<>();
		headFileContent.put(HEAD_CONTENT_KEY, HEAD_CONTENT_VALUE);
		FileHelper.addPropertiesToFile(pathHeadFile, headFileContent, false);
		System.out.println(INITIALIZATION_INFO);
	}
	
	public String getCurentBranchName() throws MikeException {
		Map<String, String> propertiesFromFile = null;
		try {
			propertiesFromFile = FileHelper.getPropertiesFromFile(getPath() + RELETIVE_PATH_HEAD);
		} catch (IOException e) {
			throw new MikeException("Can\'t get current branch", e);
		}

		return propertiesFromFile.get(HEAD_CONTENT_KEY);
	}

	public boolean setCurentBranchName(String branchName) {
		boolean status = true;
		Map<String, String> headFileContent = new HashMap<>();
		headFileContent.put(HEAD_CONTENT_KEY, branchName);
		try {
			FileHelper.addPropertiesToFile(getPath() + RELETIVE_PATH_HEAD, headFileContent, false);
		} catch (MikeException e) {
			status = false;
		}
		return status;
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
	public boolean removeBranch(String branch) {
		boolean status;
		try {
			FileHelper.removeDirectory(getPath() + RELETIVE_PATH_REFS + branch);
			status = true;
		} catch (IOException e) {
			status = false;
		}
		return status;
	}
	public String getPath() {
		return path;
	}

	public void setPath(String path) throws MikeException {
		this.path = path;
		frm = new FileResourceManager(getPath(), RELETIVE_PATH_TEMP, false, new Log4jLogger(Logger.getRootLogger()));
		try {
			frm.start();
		} catch (ResourceManagerSystemException e) {
			throw new MikeException("Can\'t create file system transaction - root folder is locked", e);
		}
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
				if (!FileHelper.compareFiles(new File(fileUrl), new File(getPath() + RELETIVE_PATH_REFS + getCurentBranchName() + "\\" + fileHash))) {
					fileList.add(fileUrl);
				}
			} catch (MikeException e) {
				//exclude file....
			}
		}
		return fileList;
	}

	public String getFileHash(String pathUrl) throws MikeException {
		String fileHash;
		try {
			if (indexFile.isEmpty()) {
				indexFile = FileHelper.getPropertiesFromFile(getPath() + RELETIVE_PATH_INDEX);
			}
			fileHash = indexFile.get(pathUrl);
			if (fileHash == null) {
				fileHash = FileHelper.getFileHash(pathUrl);
			}
		} catch (IOException e) {
			throw new MikeException("Can't compute hash for " + pathUrl, e);
		}
		return fileHash;
	}

	public void addFileCommit(String transactionId, String pathToFile, String hash) throws MikeException {
		boolean overwrite = true;
		try {
			frm.copyResource(transactionId, pathToFile, getPath() + RELETIVE_PATH_REFS + getCurentBranchName() + "\\" + hash + "-" + String.valueOf(new GregorianCalendar().getTimeInMillis()), overwrite);
		} catch (ResourceManagerException e) {
			throw new MikeException("Transaction error of copy operation for file " + pathToFile, e);
		}
	}

	public String getTransactionId() {
		try {
			return frm.generatedUniqueTxId();
		} catch (ResourceManagerSystemException e) {
			return "";
		}
	}

	public boolean startTransaction(String transactionId) {
		boolean status = false;
		if (!transactionId.isEmpty()) {
			try {
				frm.startTransaction(transactionId);
				status = true;
			} catch (ResourceManagerException e) {
				status = false;
			}
		}
		return status;
	}

	public boolean commitTransaction(String transactionId) {
		try {
			frm.commitTransaction(transactionId);
			return true;
		} catch (ResourceManagerException e) {
			return false;
		}
	}

	public boolean rollBackTransaction(String transactionId) {
		try {
			frm.rollbackTransaction(transactionId);
			return true;
		} catch (ResourceManagerException e) {
			frm.reset();
			return false;
		}
	}

}
