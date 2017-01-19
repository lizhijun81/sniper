package com.github.xiaoma.sniper.core.utils;

/**
 * Created by machunxiao on 2017/1/18.
 */
public enum ReflectionUtils {
    ;
    private static final String PARAM_CLASS_SPLIT = ",";
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

    public static Class<?>[] forNames(String classList) throws ClassNotFoundException {
        if (classList == null || classList.trim().length() == 0) {
            return EMPTY_CLASS_ARRAY;
        }
        String[] classStrings = classList.split(PARAM_CLASS_SPLIT);

        Class<?>[] classTypes = new Class[classStrings.length];
        for (int i = 0; i < classStrings.length; i++) {
            String className = classStrings[i].trim();
            classTypes[i] = Class.forName(className);
        }
        return classTypes;
    }
}
