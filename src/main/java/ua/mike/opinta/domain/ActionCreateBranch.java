package ua.mike.opinta.domain;
import ua.mike.opinta.exceptions.MikeException;

import java.util.regex.Matcher;

public class ActionCreateBranch extends UserAction {
	private final String VALIDATION_ROLE = "(m1ke)(\\s+)(create-branch)(\\s+)(-(?:[a-z][a-z]+))";
	private final Integer ARGUMENT_POSITION = 5;
	private final Integer[] CHECK_PATTERN_GROUP = new Integer[]{1,3,ARGUMENT_POSITION};
	private final String HINT = "m1ke create-branch <someBranchName>";

	public ActionCreateBranch(Repository repository) {
		super(repository);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValidCommandForThisAction(String command) {
		return super.isValidCommandByPattern(command, getPatternRule(), getPatternVerifyPoint());
	}

	@Override
	public void processCommand(String command) throws MikeException {
		Matcher matcher = super.resolvePattern(command, getPatternRule());
		matcher.find();
		String branch = matcher.group(ARGUMENT_POSITION);
		if (repository.isBranchExist(branch)) {
			throw new MikeException("Branch " + branch + " is already exist!");
		}
		if (repository.createBranch(branch)) {
			throw new MikeException("Branch " + branch + " doesn\'t create");
		}
	}

	@Override
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
