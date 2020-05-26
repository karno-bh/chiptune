package org.sm.tests;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SimpleBeep001 implements Runnable {

    @Override
    public void run() {
        AudioFormat format = new AudioFormat(
                44100,
                16,
                2,
                true,
                true
        );
        int buffSize = 1024 * 16;
        SourceDataLine sourceDataLine = null;
        try {

            sourceDataLine = AudioSystem.getSourceDataLine(format);
            sourceDataLine.open(format, buffSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        byte buffers[][] = new byte[2][buffSize];
        int herz = 15;
        int periodInPulses = 44100 / herz;
        short volume = 1000;
        int halfPeriod = periodInPulses;
        int renderedPulses = 0;

        sourceDataLine.start();
        int bufferIndex = 0;
        while(true) {
            byte[] buffer = buffers[bufferIndex];
            for(int i = 0;/*renderedPulses < halfPeriod && */ i < buffer.length; i++) {
                // buffer[i] = volume >>> 8;
                buffer[i] = (byte) ((volume >>> 8) & 0xFF);
                buffer[++i] = (byte) (volume & 0xFF);
                renderedPulses++;

                if (renderedPulses == halfPeriod /*channels*/) {
                    System.out.println(renderedPulses);
                    renderedPulses = 0;
                    volume = (short)-volume;
                    // System.out.println(volume);
                }
            }
            int written = sourceDataLine.write(buffer, 0, buffer.length);
            // System.out.printf("Written: %d\n", written);
            // bufferIndex  = bufferIndex == 0 ? 1 : 0;
        }
    }

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new SimpleBeep001());
        t.start();
    }
}
