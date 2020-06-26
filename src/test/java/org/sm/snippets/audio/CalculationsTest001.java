package org.sm.snippets.audio;

public class CalculationsTest001 {

    public static void main(String[] args) {
        double n = Math.log(0.5d) / Math.log(13d/15d);
        System.out.println(n);
        double a = Math.pow(1d/15d, n);
        System.out.println(a);

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

        System.out.println("levels");
        int i = 0;
        for (double level : amplitudes) {
//            System.out.println("i=" + (i++) + ": " + level);
            System.out.println(level);
        }

        double[] origTable =  {0.0, 0.0,
                0.00465400167849, 0.00772106507973,
                0.0109559777218, 0.0139620050355,
                0.0169985503929, 0.0200198367285,
                0.024368657969, 0.029694056611,
                0.0350652323186, 0.0403906309606,
                0.0485389486534, 0.0583352407111,
                0.0680552376593, 0.0777752346075,
                0.0925154497597, 0.111085679408,
                0.129747463188, 0.148485542077,
                0.17666895552, 0.211551079576,
                0.246387426566, 0.281101701381,
                0.333730067903, 0.400427252613,
                0.467383840696, 0.53443198291,
                0.635172045472, 0.75800717174,
                0.879926756695, 1.0 };

        System.out.println("Orig table");
        for (double level : origTable) {
            System.out.println(level);
        }
    }
}
