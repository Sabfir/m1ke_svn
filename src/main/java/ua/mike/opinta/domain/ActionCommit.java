package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;

public class ActionCommit extends UserAction {

	public ActionCommit(Repository repository) {
		super(repository);
	}

	public boolean isValidCommandForThisAction(String command) {
		// TODO
		
		return false;
	}

	public void processCommand(String command) throws MikeException {
		// TODO
	}

	public String showHint() {
		return "mike commit - commit to mike repository (to the last committed branch)";
	}

	@Override
	public String getPatternRule() {
		return null;
	}

	@Override
	public Integer[] getPatternVerifyPoint() {
		return new Integer[0];
	}

}
