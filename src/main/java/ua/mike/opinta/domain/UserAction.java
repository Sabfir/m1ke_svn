package ua.mike.opinta.domain;

import ua.mike.opinta.exceptions.MikeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UserAction {
    protected Repository repository;

    public UserAction() {
    }

    public abstract void processCommand(String command) throws MikeException;

    public abstract String showHint();

    public abstract String getPatternRule();

    public abstract Integer[] getPatternVerifyPoint();

    public abstract boolean isValidCommandForThisAction(String command);

    protected boolean isValidCommandByPattern(String command, String patternString, Integer[] checkPatternGroup) {
        List<Integer> listResult = new ArrayList<Integer>();
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            for (int captureCounter = 1; captureCounter <= matcher.groupCount(); captureCounter++) {
                if (matcher.group(captureCounter) != null && Arrays.asList(checkPatternGroup).contains(captureCounter)) {
                    listResult.add(captureCounter);
                }
            }

        }
        return listResult.size() == checkPatternGroup.length;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}
