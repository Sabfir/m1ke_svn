package ua.mike.opinta.domain;
import ua.mike.opinta.exceptions.MikeException;

import java.util.regex.Matcher;

public class ActionGetBranch extends UserAction {
	private final String VALIDATION_ROLE = "(m1ke)(\\s+)(get-branch)(\\s+)((?:[a-z][a-z]+))";
	private final Integer ARGUMENT_POSITION = 5;
	private final Integer[] CHECK_PATTERN_GROUP = new Integer[]{1,3,ARGUMENT_POSITION};
	private final String HINT = "m1ke get-branch <someBranchName> - it switches current branch to required one";

	public ActionGetBranch(Repository repository) {
		super(repository);
	}

	@Override
	public boolean isValidCommandForThisAction(String command) {
		return super.isValidCommandByPattern(command, getPatternRule(), getPatternVerifyPoint());
	}

	@Override
	public Repository getRepository() {
		return super.getRepository();
	}

	@Override
	public void processCommand(String command) throws MikeException {
		Matcher matcher = super.resolvePattern(command, getPatternRule());
		matcher.find();
		String branch = matcher.group(ARGUMENT_POSITION);
		if (!getRepository().isBranchExist(branch)) {
			throw new MikeException("Branch " + branch + " doesn't  exist!");
		} else {
			if (getRepository().getChangedFiles().size() != 0) {
				throw new MikeException("Current branch " + getRepository().getCurentBranchName() + " has uncommitted changes.");
			} else {
				if(!getRepository().setCurentBranchName(branch)) {
					throw new MikeException("Can\'t switch to " + branch + " branch - inner fault");
				}
			}
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
