package org.sm.snippets.audio;

import org.sm.sound.FYMSong;
import org.sm.sound.PausableSongProcessor;
import org.sm.sound.Song;

public class TestSongPlay001 {

    public static void main(String[] args) {
        TestSongPlay001 testSongPlay001 = new TestSongPlay001();
        testSongPlay001.test001();
    }

    void test001() {
        Song song = new FYMSong("c:\\Users\\sergeymo\\Downloads\\muzfubit.fym");
        PausableSongProcessor songProcessor = new PausableSongProcessor();
        songProcessor.setSong(song);
        songProcessor.process();
    }
}
