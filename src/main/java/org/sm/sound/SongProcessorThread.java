package org.sm.sound;

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
            // the thread will be locked once song processor will release.
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException ie) {
                    throw new RuntimeException("Error in program logic: Song Processor Thread should not be interrupted explicitly", ie);
                }
            }

            // While song processor is playable it will not proceed further in this thread loop.
            // Song processor in that time decodes the data and feeds it to audio card.
            // Once it is not playable it the thread will be locked to not consume CPU without a need
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
