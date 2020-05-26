package org.sm.datatypes;

public class DoubleCircularBuffer {

    private final double[] buffer;

    private int readPrt = 0;

    private int writePtr = 0;

    private volatile boolean dataAvailable = false;

    public DoubleCircularBuffer(int buffSize) {
        buffer = new double[buffSize];
    }

    public boolean isDataAvailable() {
        return dataAvailable;
    }

    public void setDataAvailable(boolean dataAvailable) {
        this.dataAvailable = dataAvailable;
    }

    public void writeDatum(double datum) {
        buffer[writePtr++] = datum;
        writePtr %= buffer.length;
    }

    public double readDatum() {
        double retVal = buffer[readPrt++];
        readPrt %= buffer.length;
        return retVal;
    }
}
