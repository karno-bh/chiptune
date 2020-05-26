package org.sm.listener;

import org.sm.ay.SongProcessor;
import org.sm.datatypes.DoubleCircularBuffer;

public class ConsoleLogListener implements Runnable{

    private final double timeBetweenUpdates;

    private final int logRate;

    private final SongProcessor songProcessor;

    private final int samplingRate;

    public ConsoleLogListener(int logRate, SongProcessor songProcessor) {
        this.logRate = logRate;
        this.timeBetweenUpdates = 1000d / logRate;
        this.songProcessor = songProcessor;
        this.samplingRate = songProcessor.getSamplingRate();
    }

    private void cycle() {
        DoubleCircularBuffer resultVolumeBuffer = songProcessor.getResultVolumeBuffer();
        if (resultVolumeBuffer != null && resultVolumeBuffer.isDataAvailable()) {
            double[] buff = new double[samplingRate / logRate];
            for (int i = 0; i < samplingRate / logRate; i++) {
                buff[i] = resultVolumeBuffer.readDatum();
            }
            StringBuilder sb = new StringBuilder(buff.length * 2);
            for (int i = 0; i < buff.length; i++) {
                sb.append(buff[i]).append(' ');
            }
            System.out.println("========================: " + buff.length);
            System.out.println(sb.toString());
        }
    }

    private void render() {

    }

    @Override
    public void run() {
        System.out.println("here!");
        long lastUpdateTime = System.currentTimeMillis();
        while(true) {
            long now = System.currentTimeMillis();
            while (now - lastUpdateTime >= timeBetweenUpdates) {
                cycle();
                lastUpdateTime += timeBetweenUpdates;
            }
            render();
        }
    }
}
