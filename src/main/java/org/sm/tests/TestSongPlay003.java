package org.sm.tests;

import org.sm.ay.FYMSong;
import org.sm.ay.Song;
import org.sm.ay.SongProcessor;

public class TestSongPlay003 {

    public static void main(String[] args) {
        TestSongPlay003 testSongPlay001 = new TestSongPlay003();
        testSongPlay001.test001();
    }

    void test001() {
        Song song = new FYMSong("data/a-nana.fym");
        System.out.printf("Playing:\n\tAuthor: %s\n\tTrack:  %s\n", song.getAuthor(), song.getTrack());
        System.out.println("\tDuration: " + (double) song.getFrameCount() / song.getFrameRate() + " seconds");
        SongProcessor songProcessor = new SongProcessor(song);
        songProcessor.process();
    }
}
