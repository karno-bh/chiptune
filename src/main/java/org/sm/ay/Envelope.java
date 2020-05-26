package org.sm.ay;

public class Envelope {

    private static final double[] amplitudes = calcNormalizedAmplitudes();

    private int stepsInEnvelopePeriod = 31;
    private EnvelopeShape envelopeShape = new EnvelopeShape();
    private double amplitude;
    public double envelopePulsesPeriod;
    public double envelopeStepPulsesPeriod;
    public double envelopeStepCounter;

    public Envelope() {
        envelopeShape.stepsInPeriodShape = stepsInEnvelopePeriod;
        int index = envelopeShape.setEnvelopeShapeIndex(0);
        amplitude = amplitudes[index];
    }

    public void setEnvelopePulsesPeriod(double envelopePulsesPeriod) {
        this.envelopePulsesPeriod = envelopePulsesPeriod;
        this.envelopeStepPulsesPeriod  = envelopePulsesPeriod / (stepsInEnvelopePeriod + 1);
    }

    public void setEnvelopeShape(int envelopeShapeIndex) {
        envelopeShape.setEnvelopeShapeIndex(envelopeShapeIndex);
        envelopeStepCounter = 0;
    }

    public void update() {
        envelopeStepCounter++;
        if (envelopeStepCounter >= envelopeStepPulsesPeriod) {
            envelopeStepCounter = 0;
            int index = envelopeShape.update();
            amplitude = amplitudes[index];
        }
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double getStaticAmplitude(int amplitudeIndex) {
        return amplitudes[amplitudeIndex * 2 + 1];
    }

    /**
     * Generate approximate values of volumes (amplitudes)
     * @return
     */
    private static double[] calcNormalizedAmplitudes() {
        double n = Math.log(0.5d) / Math.log(13d/15d);
        double a = Math.pow(1d/15d, n);
        double[] amplitudes = new double[32];
        int ampIndex = 1;
        for (double step = 0; step < 16; step++) {
            double level = a * Math.pow(step, n);
            amplitudes[ampIndex] = level;
            if (ampIndex > 1) {
                double prevLevel = amplitudes[ampIndex - 2];
                double avgLevel = (level + prevLevel) / 2d;
                amplitudes[ampIndex - 1] = avgLevel;
            }
            ampIndex += 2;
        }
        return amplitudes;
    }
}
