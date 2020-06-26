package org.sm.snippets.audio;

import org.sm.sound.FYMSong;
import org.sm.sound.PausableSongProcessor;
import org.sm.sound.Song;

public class TestSongPlay003 {

    public static void main(String[] args) {
        TestSongPlay003 testSongPlay001 = new TestSongPlay003();
        testSongPlay001.test001();
    }

    void test001() {
        Song song = new FYMSong("data/a-nana.fym");
        System.out.printf("Playing:\n\tAuthor: %s\n\tTrack:  %s\n", song.getAuthor(), song.getTrack());
        System.out.println("\tDuration: " + (double) song.getFrameCount() / song.getFrameRate() + " seconds");
        PausableSongProcessor songProcessor = new PausableSongProcessor();
        songProcessor.setSong(song);
        songProcessor.process();
    }
}
