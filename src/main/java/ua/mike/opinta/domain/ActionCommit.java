package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;
import java.util.HashMap;
import java.util.Map;

public class ActionCommit extends UserAction {
	private final String VALIDATION_ROLE = "(m1ke)(\\s+)(commit)(\\s+)(-m)((?:[a-z][a-z]+))";
	private final Integer ARGUMENT_POSITION = 6;
	private final Integer[] CHECK_PATTERN_GROUP = new Integer[]{1,3,5,ARGUMENT_POSITION};
	private final String HINT = "m1ke commit <-comment> - commit to mike repository (to the last committed branch)";

	public ActionCommit(Repository repository) {
		super(repository);
	}

	public boolean isValidCommandForThisAction(String command) {
		return super.isValidCommandByPattern(command, getPatternRule(), getPatternVerifyPoint());
	}

	public void processCommand(String command) throws MikeException {
		Map<String, String> mapFileHash = new HashMap<>();
		repository.getChangedFiles().forEach((pathFile) -> {
			try {
				mapFileHash.put(pathFile, repository.getFileHash(pathFile));
			} catch (MikeException e) {
			}
		});
		String transactionId = repository.getTransactionId();
		final boolean[] rollBackTransaction = {false};
		if (repository.startTransaction(transactionId)) {
				mapFileHash.forEach((key, value) -> {
				try {
					repository.addFileCommit(transactionId, key, value);
				} catch (MikeException e) {
					rollBackTransaction[0] = true;
				}
			});
		} else {
			throw new MikeException("Can\'t create file system transaction - root folder is locked");
		}
		if (rollBackTransaction[0] == true) {
			repository.rollBackTransaction(transactionId);
		} else {
			repository.commitTransaction(transactionId);
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
