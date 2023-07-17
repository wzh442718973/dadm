package com.glaring.colourful.bully.ad;

public final class HEX {
    /***
     '0' : 48		0011 0000
     '9' : 57       0011 1001
     'A' : 65		0100 0001
     'F' : 70       0100 0110
     'a' : 97       0110 0001
     'f' : 102      0110 0110

     'a' - 'A' = 32
     */

    private static final byte[] sHEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final byte[] rHEX = {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, 10, 11, 12, 13, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};


    public static byte[] encode(byte[] src, int begin, int end) {
        if (src == null || begin >= end) {
            return null;
        }
        byte[] out = new byte[(end - begin) * 2];

        for (int k = 0, i = begin; i < end; ++i, k += 2) {
            final int v = (0xff & src[i]);
            out[k] = sHEX[(v >> 4) & 0x0f];
            out[k + 1] = sHEX[v & 0x0f];
        }
        return out;
    }

    public static byte[] decode(byte[] hexs, int begin, int end) {
        if (hexs == null || begin >= end) {
            return null;
        }
        int count = end - begin;
        byte[] out = new byte[(count >> 1) + (count & 0x01)];
        int h, l;
        int k = 0;
        if ((count & 0x01) != 0) {
            h = '0';
            l = 0xff & hexs[begin++];
        } else {
            h = 0xff & hexs[begin++];
            l = 0xff & hexs[begin++];
        }
        do {
            h = (h & 0x80) + rHEX[h & 0x7F];
            l = (l & 0x80) + rHEX[l & 0x7F];
            if (h < 0 || l < 0) {
                throw new NumberFormatException("hex in invalid");
            }

            out[k++] = (byte) ((h << 4) | l);
            if (begin < end) {
                h = 0xff & hexs[begin++];
                l = 0xff & hexs[begin++];
            } else {
                break;
            }
        } while (true);
        return out;
    }

    public static byte[] encode(String src) {
        byte[] data = src.getBytes();
        return encode(data, 0, data.length);
    }

    public static byte[] decode(String hexs) {
        hexs = hexs.trim().toLowerCase();
        if (hexs.startsWith("0x")) {
            hexs = hexs.substring(2).trim();
        }
        byte[] data = hexs.getBytes();
        return decode(data, 0, data.length);
    }

    public static String toHex(byte v) {
        final byte[] out = new byte[2];

        out[0] = sHEX[(v & 0xf0) >> 4];
        out[1] = sHEX[(v & 0x0f)];

        return new String(out);
    }

    public static String toHex(short v) {
        final byte[] out = new byte[4];
        for (int k = 3; k >= 0; --k) {
            out[k] = sHEX[(v & 0x0f)];
            v >>= 4;
        }
        return new String(out);
    }

    public static String toHex(int v) {
        final byte[] out = new byte[8];
        for (int k = 7; k >= 0; --k) {
            out[k] = sHEX[(v & 0x0f)];
            v >>= 4;
        }
        return new String(out);
    }

    public static String toHex(long v) {
        final byte[] out = new byte[16];
        for (int k = 15; k >= 0; --k) {
            out[k] = sHEX[(int) (v & 0x0f)];
            v >>= 4;
        }
        return new String(out);
    }

    public static String toHex(float v) {
        return toHex(Float.valueOf(v).intValue());
    }

    public static String toHex(double v) {
        return toHex(Double.valueOf(v).longValue());
    }

    public static String toHex(byte[] bs) {
        return new String(encode(bs, 0, bs.length));
    }

    public static String toHex(String hexs) {
        hexs = hexs.trim().toLowerCase();
        if (hexs.startsWith("0x")) {
            hexs = hexs.substring(2).trim();
        }
        return toHex(hexs.getBytes());
    }


    public static int toInt(byte[] hexs) {
        byte[] data = decode(hexs, 0, hexs.length);
        int k = Math.max(0, data.length - 4);

        int val = (data[k++] & 0xff);
        for (; k < data.length; ++k) {
            val = (val << 8) | (data[k] & 0xff);
        }
        return val;
    }

    public static long toLong(byte[] hexs) {
        byte[] data = decode(hexs, 0, hexs.length);
        int k = Math.max(0, data.length - 8);

        long val = (data[k++] & 0xff);
        for (; k < data.length; ++k) {
            val = (val << 8) | (data[k] & 0xff);
        }
        return val;
    }

    public static float toFloat(byte[] hexs) {
        int val = toInt(hexs);
        return Integer.valueOf(val).floatValue();
    }

    public static double toDouble(byte[] hexs) {
        long val = toLong(hexs);
        return Long.valueOf(val).doubleValue();
    }

    public static byte[] toBytes(String src) {
        byte[] data = src.getBytes();
        return decode(data, 0, data.length);
    }

//
//    public static boolean IsNum(String num) {
//        for (int i = 0; i < num.length(); ++i) {
//            if (_HexToInt_(num.charAt(i)) >= 10) {
//                return false;
//            }
//        }
//        return true;
//    }

    public static boolean isHex(String hexs) {
        hexs = hexs.trim().toLowerCase();
        if (hexs.startsWith("0x")) {
            hexs = hexs.substring(2).trim();
        }
        for (int i = 0; i < hexs.length(); ++i) {
            int v = hexs.charAt(i);
            v = (v & 0x80) + rHEX[v & 0x7F];
            if (v < 0) {
                return false;
            }
        }
        return true;
    }

}
