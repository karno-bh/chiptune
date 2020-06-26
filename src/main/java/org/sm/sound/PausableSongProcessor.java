package org.sm.sound;

import org.sm.datatypes.DoubleCircularBuffer;
import org.sm.sound.FrameNotifier;
import org.sm.sound.Song;
import org.sm.sound.ay.Chip;
import org.sm.sound.ay.ChipRegisters;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class PausableSongProcessor {

    private static final int BUFFER_SIZE = 1024 * 16;
    private static final short MAX_VOLUME = Short.MAX_VALUE;
    private static final int SAMPLING_RATE = 48000;

    private int pulsesPerInterrupt;

    private int interruptCounter = 0;

    private Chip chip;

    private volatile Song song;

    private SourceDataLine sourceDataLine;

    private volatile DoubleCircularBuffer resultVolumeBuffer;

    private volatile boolean playable = true;

    private ChipRegisters registers = new ChipRegisters();

    private double[] channelAmplitudes;

    private byte[] buffer;

    private volatile FrameNotifier frameNotifier;

    public PausableSongProcessor() {
        this.sourceDataLine = prepareSound();
        buffer = new byte[BUFFER_SIZE];
        resultVolumeBuffer = new DoubleCircularBuffer(BUFFER_SIZE);
    }

    public final void setSong(Song song) {
        this.song = song;
        int interruptFrequency = song.getFrameRate();
        int chipClock = song.getChipClock();
        this.pulsesPerInterrupt = SAMPLING_RATE / interruptFrequency;
        this.chip = new Chip(SAMPLING_RATE, chipClock);
        channelAmplitudes = new double[chip.getChannelsSize()];
        registers = song.nextFrame(registers);
        chip.updateChipState(registers);
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
        sourceDataLine.start();
        return sourceDataLine;
    }

    public void process() {
        while (true) {
            if (!playable) {
                resultVolumeBuffer.setDataAvailable(false);
                break;
            }
            for (int i = 0; i < BUFFER_SIZE; i++) {
                chip.cycle(channelAmplitudes);
                short mixedVolume = mixedVolume(channelAmplitudes);
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
                    if (frameNotifier != null) {
                        frameNotifier.onFrameChange(song.getFrame());
                    }
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

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setFrame(int frame) {
        if (song != null) {
            song.setFrame(frame);
        }
    }

    public void setFrameNotifier(FrameNotifier frameNotifier) {
        this.frameNotifier = frameNotifier;
    }
}
