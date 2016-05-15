package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;
import java.util.HashMap;
import java.util.Map;

public class ActionCommit extends UserAction {
	private final String VALIDATION_ROLE = "(m1ke)(\\s+)(commit)(\\s+)(-m)(\\s+)((?:[a-z][a-z]+))";
	private final Integer ARGUMENT_POSITION = 7;
	private final Integer[] CHECK_PATTERN_GROUP = new Integer[]{1,3,5,ARGUMENT_POSITION};
	private final String HINT = "m1ke commit -m <comment> - commit to mike repository (to the last committed branch)";

	public ActionCommit(Repository repository) {
		super(repository);
	}

	public boolean isValidCommandForThisAction(String command) {
		return super.isValidCommandByPattern(command, getPatternRule(), getPatternVerifyPoint());
	}

	@Override
	public Repository getRepository() {
		return super.getRepository();
	}

	public void processCommand(String command) throws MikeException {
		Map<String, String> mapFileHash = new HashMap<>();
		getRepository().getChangedFiles().forEach((pathFile) -> {
			try {
				mapFileHash.put(pathFile, getRepository().getFileHash(pathFile));
			} catch (MikeException e) {
			}
		});
		String transactionId = getRepository().getTransactionId();
		final boolean[] rollBackTransaction = {false};
		if (getRepository().startTransaction(transactionId)) {
				mapFileHash.forEach((key, value) -> {
				try {
					getRepository().addFileCommit(transactionId, key, value);
				} catch (MikeException e) {
					rollBackTransaction[0] = true;
				}
			});
		} else {
			throw new MikeException("Can\'t create file system transaction - root folder is locked");
		}
		if (rollBackTransaction[0] == true) {
			getRepository().rollBackTransaction(transactionId);
		} else {
			getRepository().commitTransaction(transactionId);
		}
	}

	public String showHint() {
		return HINT;
	}

	@Override
	public String getPatternRule() {
		return VALIDATION_ROLE;
	}

	@Override
	public Integer[] getPatternVerifyPoint() {
		return CHECK_PATTERN_GROUP;
	}

}
