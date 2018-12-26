package com.viktor.timeofftests.matcher;
import lombok.Data;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class CollectionMatchers{
    public static Matcher<List> hasAllItemsExcludingProperties(List expected, String... prosToIgnore){
        return new HasAllIgnoringPros(expected, prosToIgnore);
    }
    public static Matcher<List> containsSubList(List expected){
        return new ContainsSubList(expected);
    }
}

class ContainsSubList extends TypeSafeDiagnosingMatcher<List>{
    private List expected;
    private List actual;

    ContainsSubList(List expected){
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(List actual, Description description) {
        if(actual.size() < expected.size()){
            description.appendText(String.format("Expected list to contain less than %d items but was %d",expected.size(), actual.size()));
            return false;
        }
        for(Object o : expected){
            if(!actual.contains(o)){
                description.appendText(String.format("Item %s was not found in list", o.toString()));
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {

    }
}

class HasAllIgnoringPros extends TypeSafeDiagnosingMatcher<List>{
    private List expected;
    private List<String> prosToIgnore;
    private List actual;

    HasAllIgnoringPros(List expected, String[] propsToIgnore){
        this.expected = expected;
        this.prosToIgnore = Arrays.asList(propsToIgnore);
    }
    @Override
    protected boolean matchesSafely(List actual, Description description) {
        if(actual.size() != expected.size()){
            description.appendText(String.format("Different list sizes. Was: <%d> expected:<%d>", actual.size(), expected.size()));
            return false;
        }
        if(actual.size() == 0){
            description.appendText("Cannot compare zero sized lists");
            return false;
        }
        if(expected.get(0).getClass() != actual.get(0).getClass()){
            description.appendText("Cannot compare lists with different classes");
            return false;
        }
        this.actual = actual;
        List<Field> fields = Arrays.asList(expected.get(0).getClass().getDeclaredFields());
        fields = fields.stream().filter(
                (it)->{
                    return !prosToIgnore.contains(it.getName());
                }
        ).collect(Collectors.toList());
        List<Error> errors = new ArrayList<>();
        for (int i = 0; i < expected.size(); i++){
            for(int j = 0; j < fields.size(); j++){
                Field field = fields.get(j);
                field.setAccessible(true);
                Object expectedObj = expected.get(i);
                Object actualObj = actual.get(i);
                try {
                    if(!Objects.equals(field.get(actualObj), field.get(expectedObj))){
                        Error error = new Error();
                        error.setActual(field.get(actualObj));
                        error.setExpected(field.get(expectedObj));
                        error.setField(field.getName());
                        error.setIndex(i);
                        errors.add(error);
                    }
                } catch (IllegalAccessException e) {
                    /*ignore because exception will not be thrown*/
                }
            }
        }
        if(!errors.isEmpty()) {
            errors.forEach(
                    (error)->{
                        description.appendText(error.toString());
                        description.appendText("\n");
                    }
            );
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void describeTo(Description description) {
        String actualList = String.valueOf(actual);
        String expectedList = String.valueOf(expected);
        String result = String.format(" <%s> to match <%s> ignoring properties: <%s>",
                actualList, expectedList, prosToIgnore);
        description.appendText(result);
    }
}

@Data
class Error {
    Object expected;
    Object actual;
    String field;
    int index;

    @Override
    public String toString(){
        return String.format("<%s> to be <%s> but was <%s> in <%d> element",
                field, String.valueOf(expected), String.valueOf(actual), ++index );
    }
}