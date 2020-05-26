package org.sm.ay;

public class ChipRegisters {

    // tone registers per channel

    // Channel A
    public int r0; // fine
    public int r1; // coarse
    // Channel B
    public int r2; // fine
    public int r3; // coarse

    // Channel C
    public int r4; // fine
    public int r5; // coarse

    // global noise register
    public int r6;

    // mixer control register
    public int r7;

    // Amplitude Control per Channel Registers
    public int r8; // Channel A
    public int r9; // Channel B
    public int rA; // Channel C

    // Envelope Generator
    public int rB; // fine
    public int rC; // coarse
    public int rD; // shape

}
