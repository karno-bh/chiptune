package org.sm.sound.ay;

public class Channel {

    public boolean toneOff;
    public boolean noiseOff;
    public double pulsesPeriod;
    public boolean envelopedAmplitude = true;
    public double fixedAmplitudeLevel;

    private int toneCounter = 0;
    private boolean toneEnabled = true;


    /**
     * Square wave generator.
     * @return an indicator whether square wave is on up or down state
     */
    public boolean updateTone() {
        toneCounter++;
        if (toneCounter >= pulsesPeriod) {
            toneCounter = 0;
            toneEnabled = !toneEnabled;
        }
        return toneEnabled;
    }

    public boolean isToneEnabled() {
        return toneEnabled;
    }
}
