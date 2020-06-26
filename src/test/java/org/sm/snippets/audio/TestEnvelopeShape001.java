package org.sm.snippets.audio;

import org.sm.sound.ay.EnvelopeShape;

public class TestEnvelopeShape001 {

    public static void main(String[] args) {
        new TestEnvelopeShape001().test001();
    }

    void test001() {
        EnvelopeShape es = new EnvelopeShape();
        es.stepsInPeriodShape = 31;
        for (int shape = 0; shape < 16; shape++) {
            int temp = es.setEnvelopeShapeIndex(shape);
            for (int i = 0; i < 128; i++) {
                System.out.print(temp + " ");
                temp = es.update();
            }
            System.out.println();
        }
    }
}
