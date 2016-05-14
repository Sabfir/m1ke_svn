package ua.mike.opinta.view;

import java.util.Scanner;

import ua.mike.opinta.domain.ActionContainer;
import ua.mike.opinta.domain.Repository;
import ua.mike.opinta.exceptions.MikeException;

public class WorkFlow {
	private ActionContainer actionContainer;
	
	public WorkFlow(ActionContainer actionContainer) {
		this.actionContainer = actionContainer;
	}

	public void runMikeRun(Repository repository) throws MikeException {
		try {
			validateStateBeforeCommands(repository);
		} catch (Exception e) {
			throw new MikeException("Can't initialize mike!", e);
		}
		
		Scanner scannerIn = new Scanner(System.in);
		
		while (true) {
			System.out.println("Type comand ('Exit' to exit, 'Help' to help)");
			String command = scannerIn.nextLine();
			
			if ("EXIT".equals(command.toUpperCase())) {
				break;
			}
			
			actionContainer.processUserCommand(command);
		}
		scannerIn.close();
		
		System.out.println("Mike is closed");
	}

	private void validateStateBeforeCommands(Repository repository) throws MikeException {
		if (!repository.isInitialized()) {
			repository.initialize();
		}
	}
}
