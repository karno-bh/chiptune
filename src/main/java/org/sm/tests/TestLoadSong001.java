package org.sm.tests;

import org.sm.ay.ChipRegisters;
import org.sm.ay.FYMSong;
import org.sm.ay.Song;

public class TestLoadSong001 {

    public static void main(String[] args) {
        TestLoadSong001 testLoadSong001 = new TestLoadSong001();
        testLoadSong001.test001();
    }

    void test001() {
        Song song = new FYMSong("c:\\Users\\sergeymo\\Downloads\\aftogen4.fym");
        ChipRegisters chipRegisters = new ChipRegisters();
        for (int i = 0; i < 3; i++) {
            chipRegisters = song.nextFrame(chipRegisters);
            System.out.println(chipRegisters);
        }
    }
}
