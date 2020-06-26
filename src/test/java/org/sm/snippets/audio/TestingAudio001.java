package org.sm.snippets.audio;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

public class TestingAudio001 {

    public static void main(String[] args) {
        TestingAudio001 test = new TestingAudio001();
        test.test001();
        test.test002();
    }

    public void test001() {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : infos) {
            System.out.printf("Name: %s, description %s \n", info.getName(), info.getDescription());
        }
    }

    public void test002() {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        Mixer.Info main = infos[0];
        Mixer mixer = AudioSystem.getMixer(main);
        Line.Info[] sourceLineInfos = mixer.getSourceLineInfo();
        System.out.println("Source Line info");
        for (Line.Info sourceLineInfo : sourceLineInfos) {
            System.out.println(sourceLineInfo);
        }

        Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
        System.out.println("Target Line Infos");
        for (Line.Info targetLineInfo : targetLineInfos) {
            System.out.println(targetLineInfo);
        }

    }
}
