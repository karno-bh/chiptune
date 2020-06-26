package org.sm.snippets.audio;

public class TestExtrapolateBuffer001 {

    public static void main(String[] args) {
        TestExtrapolateBuffer001 testExtrapolateBuffer001 = new TestExtrapolateBuffer001();
        testExtrapolateBuffer001.test002();
    }

    void test001() {
        double[] buff = new double[100];
        for(int i = 0; i < buff.length; i++) {
            // System.out.println(i);
            buff[i] = i;
        }

        int elementsInExtrapolation = 7;
        int extraElement = buff.length % elementsInExtrapolation == 0 ? 0 : 1;
        System.out.println(extraElement);
        double[] extrapolatedBuffer = new double[buff.length / elementsInExtrapolation + extraElement];
        for(int i = 0, j = elementsInExtrapolation, k = 0; i < buff.length; j += elementsInExtrapolation) {
            double sum = 0;
            for(;i<j && i < buff.length; i++){
                sum += buff[i];
                sum = Math.max(sum, buff[i]);
            }
            extrapolatedBuffer[k++] = sum / elementsInExtrapolation;
        }
        StringBuilder sb = new StringBuilder();
        for (double d : extrapolatedBuffer) {
            sb.append(d).append(' ');
        }
        System.out.println(sb.toString());
    }

    void test002() {
        double[] buff = new double[10];
        for(int i = 0; i < buff.length; i++) {
            // System.out.println(i);
            buff[i] = i;
        }

        double[] extrapolatedBuffer = new double[7];
        int extraElement = buff.length % extrapolatedBuffer.length == 0 ? 0 : 1;
        int elementsInExtrapolation = buff.length / extrapolatedBuffer.length + extraElement;
        for (int i = 0, j = elementsInExtrapolation, k = 0; i < buff.length; j += elementsInExtrapolation) {
            double sum = 0;
            for (;i < j && i < buff.length; i++) {
                sum += buff[i];
            }
            if (k > 800) {
                System.out.println("k> 800");
            }
            extrapolatedBuffer[k++] = sum / elementsInExtrapolation;
        }
        StringBuilder sb = new StringBuilder();
        for (double d : extrapolatedBuffer) {
            sb.append(d).append(' ');
        }
        System.out.println(sb);

        /*int elementsInExtrapolation = 7;
        int extraElement = buff.length % elementsInExtrapolation == 0 ? 0 : 1;
        System.out.println(extraElement);
        double[] extrapolatedBuffer = new double[buff.length / elementsInExtrapolation + extraElement];
        for(int i = 0, j = elementsInExtrapolation, k = 0; i < buff.length; j += elementsInExtrapolation) {
            double sum = 0;
            for(;i<j && i < buff.length; i++){
                sum += buff[i];
                sum = Math.max(sum, buff[i]);
            }
            extrapolatedBuffer[k++] = sum / elementsInExtrapolation;
        }
        StringBuilder sb = new StringBuilder();
        for (double d : extrapolatedBuffer) {
            sb.append(d).append(' ');
        }
        System.out.println(sb.toString());*/
    }
}
