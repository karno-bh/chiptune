package org.sm.sound.ay;

public class EnvelopeShape {

    public int stepsInPeriodShape;
    public int counter;

    private EnvelopePattern envelopePattern;
    private int patternIndex = 0;

    public int setEnvelopeShapeIndex(int envelopeShapeIndex) {
        this.envelopePattern = patterns[envelopeShapeIndex];
        this.counter = envelopePattern.periodShapes[0].from == ShapeDirection.UP ? stepsInPeriodShape : 0;
        this.patternIndex = 0;
        return counter;
    }

    public int update() {
        int diff = envelopePattern.periodShapes[patternIndex].to - envelopePattern.periodShapes[patternIndex].from;
        counter += diff;
        if (counter < 0 || counter > stepsInPeriodShape) {
            patternIndex++;
            if (patternIndex == envelopePattern.periodShapes.length) {
                patternIndex = envelopePattern.repeatFrom;
            }
            counter = envelopePattern.periodShapes[patternIndex].from == ShapeDirection.UP ? stepsInPeriodShape : 0;
        }
        return counter;
    }

    interface ShapeDirection {
        int UP = 1;
        int DOWN = 0;
    }

    static class EnvelopePeriodShape {
        int from;
        int to;

        public EnvelopePeriodShape(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    static class EnvelopePattern {
        EnvelopePeriodShape[] periodShapes;
        int repeatFrom;

        public EnvelopePattern(EnvelopePeriodShape[] periodShapes, int repeatFrom) {
            this.periodShapes = periodShapes;
            this.repeatFrom = repeatFrom;
        }
    }

    static EnvelopePattern[] patterns = genPatterns();

    static EnvelopePattern[] genPatterns() {
        EnvelopePattern[] patterns = new EnvelopePattern[16];
        EnvelopePattern cont0att0 = new EnvelopePattern(
            new EnvelopePeriodShape[] {
                new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.DOWN),
                new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.DOWN)
            },
            1
        );

        patterns[0b0000] = cont0att0;
        patterns[0b0001] = cont0att0;
        patterns[0b0010] = cont0att0;
        patterns[0b0011] = cont0att0;

        EnvelopePattern cont0att1 = new EnvelopePattern(
            new EnvelopePeriodShape[] {
                new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.UP),
                new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.DOWN)
            },
            1
        );

        patterns[0b0100] = cont0att1;
        patterns[0b0101] = cont0att1;
        patterns[0b0110] = cont0att1;
        patterns[0b0111] = cont0att1;

        patterns[0b1000] = new EnvelopePattern(
            new EnvelopePeriodShape[] {
                    new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.DOWN),
            },
            0
        );
        patterns[0b1001] = cont0att0;
        patterns[0b1010] = new EnvelopePattern(
            new EnvelopePeriodShape[]{
                new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.DOWN),
                new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.UP),
            },
            0
        );
        patterns[0b1011] = new EnvelopePattern(
            new EnvelopePeriodShape[]{
                new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.DOWN),
                new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.UP),
            },
            1
        );

        patterns[0b1100] = new EnvelopePattern(
            new EnvelopePeriodShape[]{
                    new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.UP),
            },
            0
        );
        patterns[0b1101] = new EnvelopePattern(
            new EnvelopePeriodShape[]{
                new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.UP),
                new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.UP)
            },
            1
        );
        patterns[0b1110] = new EnvelopePattern(
            new EnvelopePeriodShape[]{
                    new EnvelopePeriodShape(ShapeDirection.DOWN, ShapeDirection.UP),
                    new EnvelopePeriodShape(ShapeDirection.UP, ShapeDirection.DOWN)
            },
            0
        );
        patterns[0b1111] = cont0att1;


        return patterns;
    }
}
