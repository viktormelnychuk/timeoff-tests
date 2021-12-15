package com.viktor.timeofftests.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MapMatchers {
    public static Matcher<Map> mapContainsAllElements(Map expected){
        return new MapContainsAllElements(expected);
    }
}
class MapContainsAllElements extends TypeSafeDiagnosingMatcher<Map>{
    private Map actual;
    private Map expected;

    MapContainsAllElements(Map expected){
        this.expected = expected;
    }
    @Override
    protected boolean matchesSafely(Map actual, Description description) {
        boolean result = true;
        this.actual = actual;
        if(actual.size() != expected.size()){
            description.appendText(String.format("expected map size <%d> but was <%d>", expected.size(), actual.size()));
            return false;
        }
        if(expected.size() == 0){
            description.appendText("Collections are empty");
            return false;
        }
        Set keySet = expected.keySet();
        for (Object o : keySet) {
            if(!Objects.equals(actual.get(o), expected.get(o))){
                String errorMessage = String.format(" expected value for key <%s> was <%s> but got <%s>",
                        String.valueOf(o),
                        String.valueOf(expected.get(o)),
                        String.valueOf(actual.get(o)));
                description.appendText(errorMessage);
                description.appendText("\n");
                result = false;
            }
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {
        String msg = "%s to contains all entries from %s";
        description.appendText(String.format(msg, String.valueOf(expected), String.valueOf(actual)));
    }
}
