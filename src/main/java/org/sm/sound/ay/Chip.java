package org.sm.sound.ay;

public class Chip {

    private double pulseFactor;

    private Channel[] channels;
    private GlobalNoise globalNoise;
    private Envelope envelope;

    public Chip(int samplingRate, int chipClock) {
        this.channels = new Channel[3];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new Channel();
        }
        this.globalNoise = new GlobalNoise();
        this.envelope = new Envelope();

        this.pulseFactor = (double) samplingRate / chipClock;
    }

    public void updateChipState(ChipRegisters registers) {
        Channel channel01 = channels[0];
        Channel channel02 = channels[1];
        Channel channel03 = channels[2];

        channel01.pulsesPeriod = samplingTonePeriod(registers.r1, registers.r0, pulseFactor);
        channel02.pulsesPeriod = samplingTonePeriod(registers.r3, registers.r2, pulseFactor);
        channel03.pulsesPeriod = samplingTonePeriod(registers.r5, registers.r4, pulseFactor);

        globalNoise.pulsesPeriod = pulseFactor * 16 * registers.r6;
        // get a better "boom-boom-boom"... seems like frequency in register is too low...
        globalNoise.pulsesPeriod *= 2;

        channel01.toneOff  = ((registers.r7      ) & 0x1) == 1;
        channel02.toneOff  = ((registers.r7 >>> 1) & 0x1) == 1;
        channel03.toneOff  = ((registers.r7 >>> 2) & 0x1) == 1;

        channel01.noiseOff = ((registers.r7 >>> 3) & 0x1) == 1;
        channel02.noiseOff = ((registers.r7 >>> 4) & 0x1) == 1;
        channel03.noiseOff = ((registers.r7 >>> 5) & 0x1) == 1;

        channel01.fixedAmplitudeLevel = registers.r8 & 0b1111;
        channel01.envelopedAmplitude = ((registers.r8 >>> 4) & 0x01) == 1;

        channel02.fixedAmplitudeLevel = registers.r9 & 0b1111;
        channel02.envelopedAmplitude = ((registers.r9 >>> 4) & 0x01) == 1;

        channel03.fixedAmplitudeLevel = registers.rA & 0b1111;
        channel03.envelopedAmplitude = ((registers.rA >>> 4) & 0x01) == 1;

        int chipEnvelopePulsesPeriod = 256 * registers.rC + registers.rB;
        double envelopePulsesPeriod = pulseFactor * 256 * chipEnvelopePulsesPeriod;
        envelope.setEnvelopePulsesPeriod(envelopePulsesPeriod);

        if (registers.rD != 0xFF) {
            envelope.setEnvelopeShape(registers.rD);
        }
    }

    public void cycle(double[] mixPerChannel) {
        globalNoise.updateNoise();
        envelope.update();
        for (int i = 0; i < channels.length; i++) {
            Channel channel = channels[i];
            channel.updateTone();
            boolean channelEnabled =
                    (channel.isToneEnabled()  || channel.toneOff ) &&
                    (globalNoise.isNoiseEnabled() || channel.noiseOff);
            double channelOut = 0d;
            if (channelEnabled) {
                if (channel.envelopedAmplitude) {
                    channelOut = envelope.getAmplitude();
                } else {
                    channelOut = (1d / 16d) * channel.fixedAmplitudeLevel;
                }
            }
            mixPerChannel[i] = channelOut;
        }
    }

    public int getChannelsSize() {
        return channels.length;
    }

    private double samplingTonePeriod(int coarseRegister, int fineRegister, double pulseFactor) {
        int chipTonePeriod = 256 * coarseRegister + fineRegister;
        return pulseFactor * 16 * chipTonePeriod / 2;
    }

}
