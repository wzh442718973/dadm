package com.glaring.colourful.bully.base;


public class Tests {
    public static void main(String[] args) {
        byte[] hexs = HEX.encode("我是谁");
        System.out.println(new String(hexs));
        System.out.println(new String(HEX.encode("我是谁2")));
        byte[] aa = HEX.decode("0123456789ABCDEF");

        System.out.println(new String(HEX.encode(aa, 0, aa.length)));

        System.out.println(new String(HEX.decode(hexs, 0, hexs.length)));

        System.out.println("1234567890=0x" + HEX.toHex((short) 1234567890));

        System.out.println(""+HEX.toInt(HEX.toHex((int)1234567).getBytes()));

        System.out.println("" + HEX.isHex("0x123c4435Aabe"));
    }
}
