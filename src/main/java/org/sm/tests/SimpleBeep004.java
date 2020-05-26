package org.sm.tests;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SimpleBeep004 implements Runnable {

    @Override
    public void run() {
        AudioFormat format = new AudioFormat(
                44100,
                16,
                2,
                true,
                true
        );
        SourceDataLine sourceDataLine = null;
        try {

            sourceDataLine = AudioSystem.getSourceDataLine(format);
            sourceDataLine.open(format, 4096 * 4);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int noise = 1;
        byte buffers[][] = new byte[2][4096 * 4];
        int herz = 3000;
        int periodInPulses = 44100 / herz;
        short volume = 2000;
        int halfPeriod = periodInPulses;
        int noiseCounter = 0;

        sourceDataLine.start();
        int bufferIndex = 0;
        while(true) {
            byte[] buffer = buffers[bufferIndex];
            for(int i = 0;i < buffer.length; i++) {
                // buffer[i] = volume >>> 8;
                // System.out.println("Noise counter: " + noiseCounter);
                short localVolume = (short)-volume;
                if ((noise & 1) == 1) {
                    localVolume = volume;
                    // System.out.println("noise: " + noise);
                }
                buffer[i] = (byte) ((localVolume >>> 8) & 0xFF);
                buffer[++i] = (byte) (localVolume & 0xFF);

                if (++noiseCounter >=  halfPeriod * 2) {
                    noiseCounter = 0;
//                     System.out.println("Noise: " + noise);
                    // System.out.println("Noise: " + noise);
                    noise = lfsr(noise);
                    // volume = (short)-volume;
////                    System.out.println("noise & 1: " + (noise & 1));
//                    noise = (int) (Math.random() * 50000);
//                    System.out.print(noise & 1);
                }
            }
            int written = sourceDataLine.write(buffer, 0, buffer.length);
            //System.out.println("written");
            // System.out.printf("Written: %d\n", written);
            // bufferIndex  = bufferIndex == 0 ? 1 : 0;
        }
    }

    int lfsr(int lfsr) {
        int bit = (lfsr) ^ (lfsr >>> 2) ^ (lfsr >>> 3) ^ (lfsr >>> 5);
        return (lfsr >>> 1) | (bit << 15);
    }

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new SimpleBeep004());
        t.start();
    }
}
