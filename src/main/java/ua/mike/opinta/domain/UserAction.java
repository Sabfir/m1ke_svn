package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;

public abstract class UserAction {
	protected Repository repository;
	
	public UserAction(Repository repository) {
		this.repository = repository;
	}
	
	public abstract boolean isValidCommandForThisAction(String command);
	public abstract void processCommand(String command) throws MikeException;
	public abstract String showHint();

	public Repository getRepository() {
		return repository;
	}
	
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
}
