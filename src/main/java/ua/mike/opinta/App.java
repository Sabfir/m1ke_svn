package ua.mike.opinta;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

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
			System.out.println("System.getProperty(user.dir):" + System.getProperty("user.dir"));
			
			String currentLocation = System.getProperty("user.dir");
			Repository repository = new Repository(currentLocation);
			
			List<UserAction> actions = new ArrayList<UserAction>();
			
			Reflections reflections = new Reflections(UserAction.class);
			Set<Class<? extends UserAction>> actionClasses = reflections.getSubTypesOf(UserAction.class);
			
			for (Class<? extends UserAction> actionClass : actionClasses) {
				Constructor<?> constructor = actionClass.getConstructor(UserAction.class);
				actions.add((UserAction) constructor.newInstance(new Object[] {repository}));
			}
			
			ActionContainer actionContainer = new ActionContainer(actions);
			
			WorkFlow workFlow = new WorkFlow(actionContainer);
			
    		workFlow.runMikeRun(repository);
		} catch (MikeException e) {
			System.out.println("An error occurred while working with the mike svv!" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}
}
