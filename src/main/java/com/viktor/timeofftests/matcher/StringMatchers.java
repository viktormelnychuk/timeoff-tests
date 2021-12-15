package com.viktor.timeofftests.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import java.util.List;

public class StringMatchers {
    public static Matcher<String> stringContainsAllSubstringsInAnyOrder(List<String> expected){
        return new StringContainsListOfStringInAnyOrder(expected);
    }
}

class StringContainsListOfStringInAnyOrder extends TypeSafeDiagnosingMatcher<String>{
    private List<String> expected;
    StringContainsListOfStringInAnyOrder(List<String> expected){
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(String actual, Description description) {
        boolean mathces = true;
        for (String s : expected) {
            if (!actual.contains(s)) {
                description.appendText(String.format("<%s>, ", s));
                mathces = false;
            }
        }
        if(!mathces){
            description.appendText(String.format("was not found in <%s>", actual));
        }
        return mathces;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("contians all in any order:<%s>", expected));
    }
}