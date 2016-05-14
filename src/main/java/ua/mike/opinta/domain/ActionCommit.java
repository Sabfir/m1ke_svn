package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;
import java.util.Map;

public class ActionCommit extends UserAction {
	private final String VALIDATION_ROLE = "(m1ke)(\\s+)(commit)(\\s+)(-(?:[a-z][a-z]+))";
	private final Integer ARGUMENT_POSITION = 5;
	private final Integer[] CHECK_PATTERN_GROUP = new Integer[]{1,3,ARGUMENT_POSITION};
	private final String HINT = "m1ke commit <-comment> - commit to mike repository (to the last committed branch)";

	public ActionCommit(Repository repository) {
		super(repository);
	}

	public boolean isValidCommandForThisAction(String command) {
		return super.isValidCommandByPattern(command, getPatternRule(), getPatternVerifyPoint());
	}

	public void processCommand(String command) throws MikeException {
		Map<String, String> mapFileHash = repository.getMapFilesHash(repository.getChangedFiles());
		mapFileHash.forEach((key, value) -> {
			repository.addFileCommit(key, value);
		});
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
