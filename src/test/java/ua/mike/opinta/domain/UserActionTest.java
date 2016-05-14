package ua.mike.opinta.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserActionTest {
	private UserAction userAction;
	private ActionCreateBranch actionCreateBranch;

	@Before
	public void setUp() {
		actionCreateBranch = new ActionCreateBranch(new Repository(""));
		userAction = Mockito.spy(UserAction.class);
		// TODO before each test
	}

	@Test
	public void isValidCommandByPatternTest(){
		Assert.assertEquals("Can\'t validate action create-branch", true,
				userAction.isValidCommandByPattern("m1ke create-branch -someBranchName",
				actionCreateBranch.getPatternRule(), actionCreateBranch.getPatternVerifyPoint()));
	}

	@Test
	public void isValidCommandByPatternFalseTest(){
		Assert.assertEquals("Don\'t verify correct command create--branch", false,
				userAction.isValidCommandByPattern("m1ke create--branch -someBranchName",
						actionCreateBranch.getPatternRule(), actionCreateBranch.getPatternVerifyPoint()));
	}

	@Test
	public void isValidCommandByPatternWithoutParameterTest(){
		Assert.assertEquals("Don\'t verify correct command create-branch without parameter", false,
				userAction.isValidCommandByPattern("m1ke create-branch",
						actionCreateBranch.getPatternRule(), actionCreateBranch.getPatternVerifyPoint()));
	}
}
