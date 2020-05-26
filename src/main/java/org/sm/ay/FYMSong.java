package org.sm.ay;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

public class FYMSong implements Song {

    private static final int INPUT_STREAM_END_OF_INPUT = -1;
    private static final int STRING_END_OF_INPUT = 0;
    private static final int BYTES_IN_INT = 4;
    private static final int BYTE_OFFSET = 8;

    private short[] songBuffer;

    private int offset;
    private int frameCount;
    private int loopFrame;
    private int chipClock;
    private int frameRate;
    private int frame;
    private String track;
    private String author;


    public FYMSong(String songFileLocation) {
        songBuffer = loadBufferFromFile(songFileLocation);

        int[] ptr = new int[1];
        offset = getBigEndianInt(ptr, songBuffer);
        frameCount = getBigEndianInt(ptr, songBuffer);
        loopFrame = getBigEndianInt(ptr, songBuffer);
        chipClock = getBigEndianInt(ptr, songBuffer);
        frameRate = getBigEndianInt(ptr, songBuffer);
        track = getCString(ptr, songBuffer);
        author = getCString(ptr, songBuffer);
    }

    private short[] loadBufferFromFile(String songFileLocation) {
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
            List<Short> buffer = new ArrayList<>();
            int aByte;
            while ((aByte = iis.read()) != INPUT_STREAM_END_OF_INPUT) {
                buffer.add((short)aByte);
            }
            short[] retBuffer = new short[buffer.size()];
            for (int i = 0; i < buffer.size(); i++) {
                retBuffer[i] = buffer.get(i);
            }
            return retBuffer;
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

    public String getAuthor() {
        return author;
    }

    public String getTrack() {
        return track;
    }
}
