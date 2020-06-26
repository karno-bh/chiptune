package org.sm.sound.ay;

public class GlobalNoise {

    private int lfsr = 1;
    public double pulsesPeriod;
    public double noiseCounter;
    private boolean noiseEnabled;

    public boolean updateNoise() {
        noiseCounter++;
        if (noiseCounter >= pulsesPeriod) {
            noiseCounter = 0;
            lfsr = nextLFSR(lfsr);
            noiseEnabled = (lfsr & 0x1) == 1;
        }
        return noiseEnabled;
    }

    public boolean isNoiseEnabled() {
        return noiseEnabled;
    }

    private int nextLFSR(int lfsr) {
        int bit = (lfsr) ^ (lfsr >>> 2) ^ (lfsr >>> 3) ^ (lfsr >>> 5);
        return (lfsr >>> 1) | (bit << 15);
    }
}
