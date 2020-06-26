package org.sm.ay;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SongProcessorThread implements Runnable {

    private final PausableSongProcessor songProcessor;

    public SongProcessorThread() {
        songProcessor = new PausableSongProcessor();
    }

    private volatile boolean canRun = true;

    private final Object lock = new Object();

    @Override
    public void run() {
        while(canRun) {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException ie) {
                    throw new RuntimeException("Error in program logic: Song Processor Thread should not be interrupted explicitly", ie);
                }
            }

            songProcessor.process();
        }
    }

    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    public void freeze() {
        songProcessor.setPlayable(false);
    }

    public void unfreeze() {
        synchronized (lock) {
            songProcessor.setPlayable(true);
            lock.notifyAll();
        }
    }

    public PausableSongProcessor getSongProcessor() {
        return songProcessor;
    }

}
