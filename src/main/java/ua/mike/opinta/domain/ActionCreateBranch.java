package ua.mike.opinta.domain;

import java.util.List;

import ua.mike.opinta.exceptions.MikeException;
import ua.mike.opinta.helpers.StringHelper;

public class ActionCreateBranch extends UserAction {

	public ActionCreateBranch(Repository repository) {
		super(repository);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValidCommandForThisAction(String command) {
		List<String> commandWords = StringHelper.getListOfStringBySplitter(command, " ");

		return commandWords.size() == 3 && "CREATE-BRANCH".equals(commandWords.get(1).toUpperCase());
	}

	@Override
	public void processCommand(String command) throws MikeException {
		// TODO Auto-generated method stub
		System.out.println(repository.RELETIVE_PATH_REFS);
	}

	@Override
	public String showHint() {
		return "m1ke create-branch someBranchName";
	}

}
