package org.sm.tests;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SimpleBeep002 implements Runnable {

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
        int herz = 4000;
        int periodInPulses = 44100 / herz;
        short volume = 1000;
        int halfPeriod = periodInPulses;
        int noiseCounter = 0;

        sourceDataLine.start();
        int bufferIndex = 0;
        while(true) {
            byte[] buffer = buffers[bufferIndex];
            for(int i = 0;i < buffer.length; i++) {
                // buffer[i] = volume >>> 8;
                // System.out.println("Noise counter: " + noiseCounter);
                if ((noise & 1) == 1) {
                    // System.out.println("noise: " + noise);
                    buffer[i] = (byte) ((volume >>> 8) & 0xFF);
                    buffer[++i] = (byte) (volume & 0xFF);
                }
                if (++noiseCounter >=  halfPeriod * 2) {
                    noiseCounter = 0;
//                     System.out.println("Noise: " + noise);
                    // System.out.println("Noise: " + noise);
                    int bit0x3 = (noise ^ (noise >>> 3)) & 1;
                    noise = (noise >>> 1) | (bit0x3 << 16);
                    volume = (short)-volume;
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

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new SimpleBeep002());
        t.start();
    }
}
