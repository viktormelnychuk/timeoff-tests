package com.viktor.timeofftests.common;

import java.util.*;

public class Utils {

    public static int[] getRandomIndexes(List list, int count){
        Random random = new Random();
        Set<Integer> generated = new LinkedHashSet<>();
        while (generated.size() < count){
            Integer next = random.nextInt(list.size());
            generated.add(next);
        }
        return convertArrays(generated.toArray(new Integer[generated.size()]));
    }

    public static int[] getRandomIndexes(Object[] arr, int count){
        return getRandomIndexes(Arrays.asList(arr), count);
    }

    private static int[] convertArrays(Integer[] arr){
        int[] result = new int[arr.length];
        for(int i = 0; i < arr.length; i++){
            result[i] = arr[i];
        }
        return result;
    }
}
