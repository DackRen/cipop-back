package com.cipop.base.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Demo class
 *
 * @author renjunzhou
 * @date 2017/7/20
 */
@Component
public class CommonUtils {
    public static boolean isEmpty(Collection collection){
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Iterable iterable){
        return iterable == null || iterable.spliterator().getExactSizeIfKnown() == 0;
    }

    public static boolean isEmpty(String string){
        return string == null || string.length() == 0;
    }

    public static boolean isEmpty(Object[] array){
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Collection collection){
        return collection != null && !collection.isEmpty();
    }

    public static String nullIfEmpty(String s){
        return Strings.emptyToNull(s);
    }

    /**
     * is a integer but be stored as a double type?
     * @param obj
     * @return
     */
    public static boolean isIntegerForDouble(double obj) {
        // range
        double eps = 1e-10;
        return obj-Math.floor(obj) < eps;
    }

    public static String firstNotNull(String... objects){
        for(String object: objects){
            if(object != null) {return object;}
        }
        return null;
    }

    public static <T> Set<T> findDuplicates(List<T> list) {
        Set<T> uniques = new HashSet<>();
        return list.stream()
            .filter(e -> !uniques.add(e))
            .collect(Collectors.toSet());
    }

    public static Boolean between(String value, String from, String to){
        return StringUtils.compare(value, from) >= 0 && StringUtils.compare(value, to) <= 0;
    }

    public static String cut(String content, int max){
        if(content != null){
            return content.substring(0, Math.min(content.length(), max));
        }
        return "";
    }

    public static <T> T deepClone(T t) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(t);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        return (T)ois.readObject();
    }

    public static <T> T deepCloneJackson(T t, Class<? extends T> tClass) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(t);
        return objectMapper.readValue(json, tClass);
    }

    public static boolean equals(String s1, String s2){
        return StringUtils.equals(s1, s2);
    }
}
