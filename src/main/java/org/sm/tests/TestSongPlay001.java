package org.sm.tests;

import org.sm.ay.FYMSong;
import org.sm.ay.Song;
import org.sm.ay.SongProcessor;

public class TestSongPlay001 {

    public static void main(String[] args) {
        TestSongPlay001 testSongPlay001 = new TestSongPlay001();
        testSongPlay001.test001();
    }

    void test001() {
        Song song = new FYMSong("c:\\Users\\sergeymo\\Downloads\\muzfubit.fym");
        SongProcessor songProcessor = new SongProcessor(song);
        songProcessor.process();
    }
}
