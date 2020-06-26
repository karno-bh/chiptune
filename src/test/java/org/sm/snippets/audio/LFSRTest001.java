package org.sm.snippets.audio;

public class LFSRTest001 {

    public static void main(String[] args) {

        int reg = 0xACE1;
        for (int i = 0; i < 1000; i++) {
            System.out.printf("0x%08X\n", reg);
            reg = lfsr(reg);
            int bit = reg & 1;
            System.out.println("bit: " + bit);
        }
    }

    static int lfsr(int lfsr) {
        int bit = (lfsr) ^ (lfsr >>> 2) ^ (lfsr >>> 3) ^ (lfsr >>> 5);
        return lfsr = (lfsr >>> 1) | (bit << 15);
    }
}
