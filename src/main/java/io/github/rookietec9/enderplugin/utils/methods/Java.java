package io.github.rookietec9.enderplugin.utils.methods;

/**
 * @author Jeremi
 * @version 22.4.3
 */
public class Java {

    public static final char BLACK_BOX = '\u25A0';

    public static int getLeven(String firstString, String secondString) {
        int getLevenDistance = 0;
        char[] firstArray = firstString.toCharArray();
        char[] secondArray = secondString.toCharArray();
        if (firstArray == secondArray) {
            return 0;
        }
        if (firstArray.length == secondArray.length) for (int i = 0; i < firstArray.length; i++) if (firstArray[i] != secondArray[i]) getLevenDistance++;

        if (firstArray.length != secondArray.length) {
            getLevenDistance = secondArray.length - firstArray.length;
            if (firstArray.length < secondArray.length) {
                secondArray = String.valueOf(secondArray).substring(0, firstArray.length).toCharArray();
                if (firstArray.length == secondArray.length) {
                    for (int i = 0; i < firstArray.length; i++) {
                        if (firstArray[i] != secondArray[i]) {
                            getLevenDistance++;
                        }
                    }
                }
            } else {
                firstArray = String.valueOf(firstArray).substring(0, secondArray.length).toCharArray();
                if (firstArray.length == secondArray.length) {
                    for (int i = 0; i < firstArray.length; i++) if (firstArray[i] != secondArray[i]) getLevenDistance++;
                }
            }
        }
        return getLevenDistance;
    }

    public static boolean isInRange(int compare, int min, int max) {
        return (compare <= Math.max(max, min)) && (Math.min(max, min) <= compare);
    }

    public static boolean isInRange(float compare, float min, float max) {
        return (compare <= max) && (min <= compare);
    }

    public static String capFirst(String s) {
        if (s.length() < 1) return "";

        String[] words = s.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            builder.append(word.toUpperCase().charAt(0)).append(word.substring(1).toLowerCase());
            if (i + 1 < words.length) builder.append(" ");
        }
        return builder.toString();
    }

    public static String upSlash(String s) {
        String[] ret = s.replace("_", " ").split(" ");
        StringBuilder str = new StringBuilder();
        for (String s1 : ret) str.append(capFirst(s1)).append(" ");
        return str.toString().substring(0, str.toString().length() - " ".length());
    }

    public static String convertToRoman(int num) {
        StringBuilder result = new StringBuilder();
        while (num > 0) {
            if (num >= 1000) {
                result.append("M");
                num -= 1000;
            } else if (num >= 900) {
                result.append("CM");
                num -= 900;
            } else if (num >= 500) {
                result.append("D");
                num -= 500;
            } else if (num >= 400) {
                result.append("CD");
                num -= 400;
            } else if (num >= 100) {
                result.append("C");
                num -= 100;
            } else if (num >= 90) {
                result.append("XC");
                num -= 90;
            } else if (num >= 50) {
                result.append("L");
                num -= 50;
            } else if (num >= 40) {
                result.append("XL");
                num -= 40;
            } else if (num >= 10) {
                result.append("X");
                num -= 10;
            } else if (num >= 9) {
                result.append("IX");
                num -= 9;
            } else if (num >= 5) {
                result.append("V");
                num -= 5;
            } else if (num >= 4) {
                result.append("IV");
                num -= 4;
            } else {
                result.append("I");
                num--;
            }
        }
        return result.toString();
    }

    public static int getRandom(double min, double max) {
        return (int) ((int) (Math.random() * (max + 1.0D - min)) + min);
    }

    public static boolean argWorks(String arg, String... possibleArgs) {
        for (String s : possibleArgs) if (arg.equalsIgnoreCase(s)) return true;
        return false;
    }
}