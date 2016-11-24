package lu.innocence.ignis;

/**
 * Copyright by Fabien Steines
 * Innocence Studios 2016
 */
public class ZeroStringGenerator {

    public static String addZeros(int inputValue, final int MAX) {
        int zeroCount = String.valueOf(MAX).length() - String.valueOf(inputValue).length();
        String zeros = "";
        for (int i = 0; i < zeroCount; i++) {
            zeros += "0";
        }

        return String.format("%s%d", zeros, inputValue);
    }

}
