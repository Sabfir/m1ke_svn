package ua.mike.opinta.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.mike.opinta.exceptions.MikeException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActionCreateBranchTest {
    @Mock Repository repository;
    @InjectMocks ActionCreateBranch actionCreateBranch = new ActionCreateBranch(repository);

    @Before public void setUp(){}

    @Test public void testIsValidCommandForThisAction() throws Exception {
        Assert.assertEquals("Can\'t validate action create-branch", true,
                actionCreateBranch.isValidCommandForThisAction("m1ke create-branch -someBranchName"));
    }

    @Test(expected = MikeException.class) public void testProcessCommandBranchExist() throws Exception {
        when(repository.isBranchExist(anyString())).thenReturn(true);
        actionCreateBranch.processCommand("m1ke create-branch -someBranchName");
        verify(repository, times(0)).createBranch(anyString());
    }

    @Test() public void testProcessCommandBranchNotExist() throws Exception {
        when(repository.isBranchExist(anyString())).thenReturn(false);
        actionCreateBranch.processCommand("m1ke create-branch -someBranchName");
        verify(repository, times(1)).createBranch(anyString());
    }
}