package com.arlabs.myfm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author AR-LABS
 */
public class NumberExtractor {
    private static final String REGEX = "\\d+";
    private static final Pattern PATTERN = Pattern.compile(REGEX);
    private static Matcher matcher;
    
    public static List<Integer> extractNumbers(String inputText) {
        List<Integer> nums = new ArrayList<>();
        
        matcher = PATTERN.matcher(inputText);
        
        while(matcher.find()) {
            nums.add(Integer.valueOf(matcher.group()));
        }
        
        return nums;
    }
}
