package com.cd.utils;

public class SecurityUtil {

    /**
     * 手机号加密函数
     * @param phoneNum
     * @return
     */
    public static String phoneNumEncrypt(String phoneNum) {
        // 数字0-9及+，-按从小到大顺序变成RIZBHGECFO
        phoneNum = phoneNum.replace("-", "A");
        phoneNum = phoneNum.replace("0", "R");
        phoneNum = phoneNum.replace("1", "I");
        phoneNum = phoneNum.replace("2", "Z");
        phoneNum = phoneNum.replace("3", "B");
        phoneNum = phoneNum.replace("4", "H");
        phoneNum = phoneNum.replace("5", "G");
        phoneNum = phoneNum.replace("6", "E");
        phoneNum = phoneNum.replace("7", "C");
        phoneNum = phoneNum.replace("8", "F");
        phoneNum = phoneNum.replace("9", "O");
        phoneNum = phoneNum.replace(" ", "");
        phoneNum = phoneNum.replace("+", "Y");
        return phoneNum;
    }

    /**
     * 手机号解密函数
     * @param phoneNum
     * @return
     */
    public static String phoneNumDecrypt(String phoneNum) {
        // 数字0-9及+，-按从小到大顺序变成RIZBHGECFO
        phoneNum = phoneNum.replace("A", "-");
        phoneNum = phoneNum.replace("R", "0");
        phoneNum = phoneNum.replace("I", "1");
        phoneNum = phoneNum.replace("Z", "2");
        phoneNum = phoneNum.replace("B", "3");
        phoneNum = phoneNum.replace("H", "4");
        phoneNum = phoneNum.replace("G", "5");
        phoneNum = phoneNum.replace("E", "6");
        phoneNum = phoneNum.replace("C", "7");
        phoneNum = phoneNum.replace("F", "8");
        phoneNum = phoneNum.replace("O", "9");
        phoneNum = phoneNum.replace("Y", "+");
        return phoneNum;
    }
}
