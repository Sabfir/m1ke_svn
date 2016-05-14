package ua.mike.opinta;

import java.util.ArrayList;
import java.util.List;

import ua.mike.opinta.domain.ActionCommit;
import ua.mike.opinta.domain.ActionContainer;
import ua.mike.opinta.domain.Repository;
import ua.mike.opinta.domain.UserAction;
import ua.mike.opinta.exceptions.MikeException;
import ua.mike.opinta.view.WorkFlow;

public class App 
{
	public static void main( String[] args )
	{
		try {
			// mike init
			// how to find current dir like git?
			// try this:
			// String currentLocation = System.getProperty("user.dir");
			// or
			// File cwd = new File(".");
			
			String currentLocation = System.getProperty("user.dir");
			Repository repository = new Repository(currentLocation);
			
			List<UserAction> actions = new ArrayList<UserAction>();
			// TODO add all actions (classes that extend UserAction abstract class) through reflection
			actions.add(new ActionCommit(repository));
			
			ActionContainer actionContainer = new ActionContainer(actions);
			
			WorkFlow workFlow = new WorkFlow(actionContainer);
			
    		workFlow.runMikeRun(repository);
		} catch (MikeException e) {
			System.out.println("An error occurred while working with the mike svv!" + e.getMessage());
			e.printStackTrace();
		}
	}
}
