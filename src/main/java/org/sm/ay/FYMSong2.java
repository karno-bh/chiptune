package org.sm.ay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

public class FYMSong2 implements Song {

    private static final int INPUT_STREAM_END_OF_INPUT = -1;
    private static final int STRING_END_OF_INPUT = 0;
    private static final int BYTES_IN_INT = 4;
    private static final int BYTE_OFFSET = 8;

    private short[] songBuffer;

    private int offset = -1;
    private int frameCount;
    private int loopFrame;
    private int chipClock;
    private int frameRate;
    private int frame;
    private String track;
    private String author;


    public FYMSong2(String songFileLocation) {
        this(songFileLocation, false);
    }

    public FYMSong2(String songFileLocation, boolean onlyHeader) {
        songBuffer = loadBufferFromFile(songFileLocation, onlyHeader);
    }

    private short[] loadBufferFromFile(String songFileLocation, boolean onlyHeader) {
        File songFile = new File(songFileLocation);
        if (!songFile.isFile()) {
            throw new RuntimeException("Location: " + songFileLocation + " is not a file");
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(songFile);
        } catch (IOException ioe) {
            throw new RuntimeException("Cannot open file", ioe);
        }
        BufferedInputStream bis = new BufferedInputStream(fis);
        try (InflaterInputStream iis = new InflaterInputStream(bis)) {
            int[] ptr = new int[1];
            ArrayList<Short> buffer = new ArrayList<>();
            int aByte;
            for (int byteCounter = 0; (aByte = iis.read()) != INPUT_STREAM_END_OF_INPUT; byteCounter++) {
                buffer.add((short)aByte);
                if (byteCounter == BYTES_IN_INT) {
                    short[] buff = dumpToArray(buffer);
                    offset = getBigEndianInt(ptr, buff);
                } else if (byteCounter == offset) {
                    short[] buff = dumpToArray(buffer);
                    frameCount = getBigEndianInt(ptr, buff);
                    loopFrame = getBigEndianInt(ptr, buff);
                    chipClock = getBigEndianInt(ptr, buff);
                    frameRate = getBigEndianInt(ptr, buff);
                    track = getCString(ptr, buff);
                    author = getCString(ptr, buff);
                    if (onlyHeader) break;
                }
            }
            return dumpToArray(buffer);
        } catch (IOException ioe) {
            throw new RuntimeException("Cannot process: " + songFileLocation, ioe);
        }
    }

    private int getBigEndianInt(int[] ptr, short[] dump) {
        int val = 0;
        for (int i = 0; i < BYTES_IN_INT; i++) {
            val += dump[ptr[0]++] << (BYTE_OFFSET * i);
        }
        return val;
    }

    private String getCString(int[] ptr, short[] dump) {
        StringBuilder sb = new StringBuilder();
        char ch;
        while ((ch = (char)dump[ptr[0]++]) != STRING_END_OF_INPUT) {
            sb.append(ch);
        }
        return sb.toString();
    }

    @Override
    public ChipRegisters nextFrame(ChipRegisters possibleBuffer) {
        ChipRegisters next = possibleBuffer == null ? new ChipRegisters() : possibleBuffer;
        int regNum = 0;
        next.r0 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r1 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r2 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r3 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r4 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r5 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r6 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r7 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r8 = songBuffer[regNum++ * frameCount + frame + offset];
        next.r9 = songBuffer[regNum++ * frameCount + frame + offset];
        next.rA = songBuffer[regNum++ * frameCount + frame + offset];
        next.rB = songBuffer[regNum++ * frameCount + frame + offset];
        next.rC = songBuffer[regNum++ * frameCount + frame + offset];
        next.rD = songBuffer[regNum   * frameCount + frame + offset];

        frame++;
        if (frame == frameCount) {
            frame = loopFrame;
            System.out.println("looping!");
//            throw new RuntimeException();
        }
        return next;
    }

    private short[] dumpToArray(ArrayList<Short> arrayList) {
        short[] retVal = new short[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            retVal[i] = arrayList.get(i);
        }
        return retVal;
    }

    public int getChipClock() {
        return chipClock;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        if (frame < 0 || frame >= frameCount) {
            throw new IllegalStateException("Frame Should be in the within a range: [0, " + frameCount + ")");
        }
        this.frame = frame;
    }

    public String getAuthor() {
        return author;
    }

    public String getTrack() {
        return track;
    }
}
