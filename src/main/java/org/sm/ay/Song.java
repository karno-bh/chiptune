package org.sm.ay;

public interface Song {

    ChipRegisters nextFrame(ChipRegisters possibleBuffer);

    int getChipClock();

    int getFrameRate();

    String getTrack();

    String getAuthor();

    public int getFrameCount();
}
