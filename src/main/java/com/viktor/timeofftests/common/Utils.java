package com.viktor.timeofftests.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

    public static int[] getRandomIndexes(List list, int count){
        int listSize = list.size();
        Random random = new Random();
        int[] result = new int[count];
        for (int i = 0; i < count; i++){
            result[i] = random.nextInt(listSize);
        }
        return result;
    }

    public static int[] getRandomIndexes(Object[] arr, int count){
        return getRandomIndexes(Arrays.asList(arr), count);
    }
}
