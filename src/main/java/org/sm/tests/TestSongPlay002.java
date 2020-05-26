package org.sm.tests;

import org.sm.ay.FYMSong;
import org.sm.ay.Song;
import org.sm.ay.SongProcessor;
import org.sm.listener.ConsoleLogListener;

public class TestSongPlay002 {

    public static void main(String[] args) {
        TestSongPlay002 testSongPlay001 = new TestSongPlay002();
        testSongPlay001.test001();
    }

    void test001() {
        Song song = new FYMSong("data/ft_story.fym");
        System.out.printf("Playing:\n\tAuthor: %s\n\tTrack:  %s\n", song.getAuthor(), song.getTrack());
        System.out.println("\tDuration: " + (double) song.getFrameCount() / song.getFrameRate() + " seconds");
        SongProcessor songProcessor = new SongProcessor(song);
        ConsoleLogListener logListener = new ConsoleLogListener(20, songProcessor);
        Thread log = new Thread(logListener);
        log.start();
        songProcessor.process();
    }
}
