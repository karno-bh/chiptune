package org.sm.ay;

import org.sm.datatypes.DoubleCircularBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class SongProcessor {

    private static final int BUFFER_SIZE = 1024 * 16;
    private static final short MAX_VOLUME = Short.MAX_VALUE;
    private static final int SAMPLING_RATE = 48000;

    private int pulsesPerInterrupt;

    private int interruptCounter = 0;

    private Chip chip;
    private Song song;

    private SourceDataLine sourceDataLine;

    private volatile DoubleCircularBuffer resultVolumeBuffer;

    public SongProcessor(Song song) {
        int interruptFrequency = song.getFrameRate();
        int chipClock = song.getChipClock();
        this.pulsesPerInterrupt = SAMPLING_RATE / interruptFrequency;

        this.chip = new Chip(SAMPLING_RATE, chipClock);
        this.song = song;
        this.sourceDataLine = prepareSound();
    }

    private SourceDataLine prepareSound() {
        AudioFormat format = new AudioFormat(
                SAMPLING_RATE,
                16,
                2,
                true,
                true
        );
        SourceDataLine sourceDataLine;
        try {
            sourceDataLine = AudioSystem.getSourceDataLine(format);
            sourceDataLine.open(format, BUFFER_SIZE);
        } catch (Exception e) {
            throw new RuntimeException("Cannot initialize audio system", e);
        }
        return sourceDataLine;
    }

    public void process() {
        ChipRegisters registers = new ChipRegisters();
        double[] channelAplitudes = new double[chip.getChannelsSize()];
        registers = song.nextFrame(registers);
        chip.updateChipState(registers);
        sourceDataLine.start();

        byte[] buffer = new byte[BUFFER_SIZE];
        resultVolumeBuffer = new DoubleCircularBuffer(BUFFER_SIZE);
        while (true) {
            for (int i = 0; i < BUFFER_SIZE; i++) {
                chip.cycle(channelAplitudes);
                short mixedVolume = mixedVolume(channelAplitudes);
                resultVolumeBuffer.writeDatum(mixedVolume);
                //left channel
                buffer[i]   = (byte) ((mixedVolume >>> 8) & 0xFF);
                buffer[++i] = (byte) (mixedVolume & 0xFF);
                // right channel
                buffer[++i] = (byte) ((mixedVolume >>> 8) & 0xFF);
                buffer[++i] = (byte) (mixedVolume & 0xFF);

                interruptCounter++;
                if (interruptCounter == pulsesPerInterrupt) {
                    interruptCounter = 0;
                    registers = song.nextFrame(registers);
                    chip.updateChipState(registers);
                }
            }
            resultVolumeBuffer.setDataAvailable(true);
            sourceDataLine.write(buffer, 0, buffer.length);
        }
    }

    private short mixedVolume(double[] channelAmplitudes) {
        double mixedPulse = 0;
        for (double channelAmplitude : channelAmplitudes) {
            mixedPulse += channelAmplitude;
        }
        // normalize to [0,1]
        mixedPulse /= channelAmplitudes.length;
        return (short) (mixedPulse * MAX_VOLUME);
    }

    public DoubleCircularBuffer getResultVolumeBuffer() {
        return resultVolumeBuffer;
    }

    public static int getSamplingRate() {
        return SAMPLING_RATE;
    }

    public static short getMaxVolume() {
        return MAX_VOLUME;
    }
}
