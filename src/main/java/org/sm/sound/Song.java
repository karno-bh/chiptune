package org.sm.sound;

import org.sm.sound.ay.ChipRegisters;

public interface Song {

    ChipRegisters nextFrame(ChipRegisters possibleBuffer);

    int getChipClock();

    int getFrameRate();

    String getTrack();

    String getAuthor();

    int getFrameCount();

    int getFrame();

    void setFrame(int frameNum);
}
