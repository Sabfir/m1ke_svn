package ua.mike.opinta.domain;
import ua.mike.opinta.exceptions.MikeException;

public class ActionCreateBranch extends UserAction {
	private final String VALIDATION_ROLE = "(m1ke)(\\s+)(create-branch)(\\s+)(-(?:[a-z][a-z]+))";
	private final Integer[] CHECK_PATTERN_GROUP = new Integer[]{3,5};
	private final String HINT = "m1ke create-branch <someBranchName>";

	public ActionCreateBranch(Repository repository) {
		super.setRepository(repository);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValidCommandForThisAction(String command) {
		return super.isValidCommandByPattern(command, getPatternRule(), getPatternVerifyPoint());
	}

	@Override
	public void processCommand(String command) throws MikeException {
		// TODO Auto-generated method stub
		System.out.println(repository.RELETIVE_PATH_REFS);
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
