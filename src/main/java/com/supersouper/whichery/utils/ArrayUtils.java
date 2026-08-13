package com.supersouper.whichery.utils;

public class ArrayUtils {

    public static boolean[] byteArrayToBooleanArray(byte[] byteArray) {
        boolean[] boolArray = new boolean[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            boolArray[i] = byteArray[i] != 0;
        }
        return boolArray;
    }

    public static byte[] booleanArrayToByteArray(boolean[] boolArray) {
        byte[] byteArray = new byte[boolArray.length];
        for (int i = 0; i < boolArray.length; i++) {
            byteArray[i] = (byte) (boolArray[i] ? 1 : 0);
        }
        return byteArray;
    }

    public static int[] byteArrayToIntArray(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            intArray[i] = byteArray[i];
        }
        return intArray;
    }

    public static byte[] intArrayToByteArray(int[] intArray) {
        byte[] byteArray = new byte[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            byteArray[i] = (byte) intArray[i];
        }
        return byteArray;
    }
}
