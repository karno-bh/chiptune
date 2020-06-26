package org.sm.snippets.audio;

import org.sm.sound.ay.ChipRegisters;
import org.sm.sound.FYMSong;
import org.sm.sound.Song;

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
