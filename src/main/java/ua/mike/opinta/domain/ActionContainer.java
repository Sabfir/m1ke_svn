package ua.mike.opinta.domain;

import java.util.ArrayList;
import java.util.List;

import ua.mike.opinta.exceptions.MikeException;

public class ActionContainer {
	private List<UserAction> actions = new ArrayList<UserAction>();
	
	public ActionContainer(List<UserAction> actions) {
		this.actions = actions;
	}
	
	public void processUserCommand(String command) throws MikeException {
		for (UserAction action : actions) {
			if (action.isValidCommandForThisAction(command)) {
				action.processCommand(command);
			}
		}
	}
	
	public List<UserAction> getActions() {
		return actions;
	}
	
	public void setActions(List<UserAction> actions) {
		this.actions = actions;
	}
}
