package org.sm.snippets.audio;

public class LangTests001 {

    public static void main(String[] args) {
        new LangTests001().test004();
    }

    void test001() {
        short t = -1000;
        byte b1 = (byte) ((t >>> 8) & 0xFF);
        byte b2 = (byte) (t & 0xFF);
        System.out.println("Byte b1: " + b1 + "Byte b2: " + b2);
        short s = (short)((b1 << 8) | b2);
        System.out.println("Converted back value: " + s);
    }

    void test002() {
        int a = 44100;
        int b = 2_000_000;
        double d = (double) a / b;
        System.out.println("D=" + d);
    }

    void test003() {
        int frameCount = 11007;
        int offset = 86;
        for (int frame = 0; frame < 36; frame++) {
            System.out.println("frame: " + frame);
            for (int r = 0; r < 14; r++) {
                int index = r * frameCount + offset + frame;
                System.out.println("r" + r + ": " + index);
            }
            System.out.println();
        }
    }
    void test004() {
        int frameCount = 6911;
        int offset = 22;
        for (int frame = 0; frame < 36; frame++) {
            System.out.println("frame: " + frame);
            for (int r = 0; r < 14; r++) {
                int index = r * frameCount + offset + frame;
                System.out.println("r" + r + ": " + index);
            }
            System.out.println();
        }
    }
}
