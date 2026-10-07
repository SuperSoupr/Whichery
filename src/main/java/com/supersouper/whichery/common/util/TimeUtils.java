package com.supersouper.whichery.common.util;

public class TimeUtils {

    public static int minutesToTicks(double minutes) {
        return (int) Math.ceil(minutes * 60 * 20);
    }

}
