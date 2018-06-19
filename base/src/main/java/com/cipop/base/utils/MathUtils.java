package com.cipop.base.utils;

import java.util.Arrays;

/**
 * Java simple variable type can calculate float value properly，this util provide math method in business level
 *
 * @author Yang Naihua
 * @date 2017/6/19
 */

public final class MathUtils {
    private static final int DEFAULT_SCALE = 3;
    private MathUtils(){}

    public static double max(Double v1, Double v2){
        if (v1 == null) {
            v1 = 0d;
        }
        if (v2 == null) {
            v2 = 0d;
        }
        return Math.max(v1, v2);
    }

    public static double add(Double v1,Double v2){
        if (v1 == null) {
            v1 = 0d;
        }
        if (v2 == null) {
            v2 = 0d;
        }
        return round(v1 + v2);
    }

    /**
     * @param v1 Be subtracted
     * @param v2 Subtraction
     * @return double result
     */
    public static double sub(Double v1, Double v2){
        if(v1 == null){
            v1 = 0d;
        }
        if(v2 == null){
            v2 = 0d;
        }
        return round(v1 - v2);
    }

    public static double mul(Double v1, Double v2){
        if(v1 == null){
            v1 = 0d;
        }
        if(v2 == null){
            v2 = 0d;
        }
        return round(v1 * v2);
    }

    public static double mul(Double v1, Integer v2){
        if(v1 == null){
            v1 = 0d;
        }
        if(v2 == null){
            v2 = 0;
        }
        return round(v1 * v2);
    }

    /**
     * @param v1 Divide
     * @param v2 divisor
     * @return double result
     */
    public static double div(Double v1, Double v2){
        if(v1 == null){
            v1 = 0d;
        }
        if(v2 == null){
            v2 = 0d;
        }
        return round(v1 / v2);
    }

    public static Double round(Double v){
        if(v == null){
            return 0d;
        }
        return Double.parseDouble(String.format("%.3f", v));
    }

    /**
     * Sum double array
     * @param values double array
     * @return sum value
     */
    public static Double sum(Double ... values){
        return round(Arrays.stream(values).reduce(MathUtils::add).get());
    }
}
