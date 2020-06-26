package org.sm.snippets.audio;

import org.sm.datatypes.DoubleCircularBuffer;
import org.sm.graphics.animation.AnimatedBoard;
import org.sm.math.RangeMapper;
import org.sm.sound.FYMSong;
import org.sm.sound.PausableSongProcessor;
import org.sm.sound.Song;

import java.awt.*;

public class TestGUISongPlay001 extends AnimatedBoard {

    private final PausableSongProcessor songProcessor;

    private final int songDataLength;

    private final RangeMapper heightMapper;

    private final double[] extrapolatedBuffer;

    private final int elementsInExtrapolation;

    public TestGUISongPlay001(int boardWidth, int boardHeight, PausableSongProcessor songProcessor) {
        super(boardWidth, boardHeight);
        this.songProcessor = songProcessor;
        this.songDataLength = songProcessor.getSamplingRate() / GAME_HERZ;
        this.extrapolatedBuffer = new double[boardWidth];
        int extraElement = songDataLength % boardWidth == 0 ? 0 : 1;
        this.elementsInExtrapolation = songDataLength / boardWidth + extraElement;
        double middleHeight = boardHeight / 2;
        this.heightMapper = new RangeMapper(0, songProcessor.getMaxVolume(),
                middleHeight - (3d/4d * middleHeight),  middleHeight + (3d/4d * middleHeight));
    }

    @Override
    protected void render() {
        Graphics g = null;
        try {
            g = buffer.getGraphics();
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.BLACK);
            g.fillRect(0,0, width, height);
            g.setColor(Color.WHITE);
            for (int i = 1; i < extrapolatedBuffer.length /*width of canvas*/; i++) {
                int xFrom = i - 1;
                //System.out.print(extrapolatedBuffer[i - 1]+ " ");
                int yFrom = (int)this.heightMapper.map(extrapolatedBuffer[i - 1]);
/*                if (yFrom > 2000) {
                    System.out.println("ssss");
                }*/
                // System.out.println(yFrom);
                int xTo = i;
                int yTo = (int)this.heightMapper.map(extrapolatedBuffer[i]);
                g.drawLine(xFrom, yFrom, xTo, yTo);
            }
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }



    @Override
    protected void cycle() {
        DoubleCircularBuffer resultVolumeBuffer = songProcessor.getResultVolumeBuffer();
        if (resultVolumeBuffer != null && resultVolumeBuffer.isDataAvailable()) {
            for (int i = 0, j = elementsInExtrapolation, k = 0; i < songDataLength; j += elementsInExtrapolation) {
                double sum = 0;
                for (;i < j && i < songDataLength; i++) {
                    double dutum = resultVolumeBuffer.readDatum();
                    /*if (i > 1600) {
                        System.out.println("sss");
                    }*/
                    //System.out.println("datum: " + dutum);
                    /*if (dutum > Short.MAX_VALUE) {
                        System.out.println("sajdajssdkj");
                    }*/
                    sum += dutum;
                }
//                System.out.println(sum / elementsInExtrapolation);
                /*if (sum / elementsInExtrapolation > Short.MAX_VALUE) {
                    System.out.println("sadasda");
                }*/
                if (k > 800) {
                    System.out.println("k>800");
                }
                extrapolatedBuffer[k++] = sum / elementsInExtrapolation;
            }
        }
    }

    @Override
    protected void onInit() {

    }

    public static void main(String[] args) {
        Song song = new FYMSong("data/ft_story.fym");
        System.out.printf("Playing:\n\tAuthor: %s\n\tTrack:  %s\n", song.getAuthor(), song.getTrack());
        System.out.println("\tDuration: " + (double) song.getFrameCount() / song.getFrameRate() + " seconds");
        PausableSongProcessor songProcessor = new PausableSongProcessor();
        songProcessor.setSong(song);
        TestGUISongPlay001 testGUISongPlay001 = new TestGUISongPlay001(1024, 768, songProcessor);
        testGUISongPlay001.start();
        songProcessor.process();
    }
}
