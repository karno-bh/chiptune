package org.sm.tests;

import org.sm.ay.FYMSong;
import org.sm.ay.Song;
import org.sm.ay.SongProcessor;
import org.sm.datatypes.DoubleCircularBuffer;
import org.sm.graphics.animation.AnimatedBoard;
import org.sm.math.RangeMapper;

import java.awt.*;

public class TestGUISongPlay003 extends AnimatedBoard {

    private final SongProcessor songProcessor;

    private final RangeMapper heightMapper;

    private final RangeMapper widthMapper;

    private final double[] songBuffer;

    private int cycle = 0;

    private int renderedCycle = 0;

    public TestGUISongPlay003(int boardWidth, int boardHeight, SongProcessor songProcessor) {
        super(boardWidth, boardHeight);
        this.songProcessor = songProcessor;
        this.songBuffer = new double [songProcessor.getSamplingRate() / GAME_HERZ];
        double middleHeight = boardHeight / 2;
        this.heightMapper = new RangeMapper(0, songProcessor.getMaxVolume(),
                middleHeight - (3d/4d * middleHeight),  middleHeight + (3d/4d * middleHeight));
        this.widthMapper = new RangeMapper(0, songBuffer.length - 1, 0, boardWidth);
    }

    @Override
    protected void render() {
        if (cycle == renderedCycle) {
            return;
        }
        renderedCycle = cycle;
        Graphics g = null;
        try {
            g = buffer.getGraphics();
            int width = getWidth();
            int height = getHeight();
            g.setColor(Color.BLACK);
            g.fillRect(0,0, width, height);
            g.setColor(Color.WHITE);
            for (int i = 1; i < songBuffer.length; i++) {
                int xFrom = (int)widthMapper.map(i - 1);
                int yFrom = height - (int)heightMapper.map(songBuffer[i - 1]);
                int xTo = (int)widthMapper.map(i);
                int yTo = height - (int)heightMapper.map(songBuffer[i]);
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
        cycle++;
        DoubleCircularBuffer resultVolumeBuffer = songProcessor.getResultVolumeBuffer();
        if (resultVolumeBuffer != null && resultVolumeBuffer.isDataAvailable()) {
            for (int i = 0; i < songBuffer.length; i++) {
                songBuffer[i] = resultVolumeBuffer.readDatum();
            }
        }
    }

    @Override
    protected void onInit() {}

    public static void main(String[] args) {
        Song song = new FYMSong("data/muzfubit.fym");
        System.out.printf("Playing:\n\tAuthor: %s\n\tTrack:  %s\n", song.getAuthor(), song.getTrack());
        System.out.println("\tDuration: " + (double) song.getFrameCount() / song.getFrameRate() + " seconds");
        SongProcessor songProcessor = new SongProcessor(song);
        TestGUISongPlay003 testGUISongPlay001 = new TestGUISongPlay003(800, 600, songProcessor);
        testGUISongPlay001.start();
        songProcessor.process();
    }
}
