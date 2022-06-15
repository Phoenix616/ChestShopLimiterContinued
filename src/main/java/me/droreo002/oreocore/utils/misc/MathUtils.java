package me.droreo002.oreocore.utils.misc;

import org.apache.commons.lang.math.NumberUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {

    public static boolean chanceOf(double value) {
        return chanceOf(value, 100.0);
    }

    public static boolean chanceOf(double value, final double check) {
        return Math.random() * check <= value;
    }

    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt((max - min) + 1) + min;
    }

    public static <T> T randomOnList(List<T> list) {
        return list.get(random(0, list.size()));
    }

    public static boolean isNumber(String text) {
        return NumberUtils.isNumber(text);
    }

    public static double getPercentage(double first, double second) {
        return (first / second) * 100;
    }
}
