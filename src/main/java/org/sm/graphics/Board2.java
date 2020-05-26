package org.sm.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board2 extends JPanel
        implements Runnable {

    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 600;
    private final int INITIAL_X = -40;
    private final int INITIAL_Y = -40;
    
    
    private final int GAME_HERZ = 30;
    private final double TIME_BETWEEN_UPDATES = 1000d / GAME_HERZ;
    private final int MAX_ITERATIONS = GAME_HERZ;
    private final double DESIRED_FPS = GAME_HERZ * 2;
    
    private Image star;
    private Thread animator;
    private long was = System.currentTimeMillis();
    private int x, y;
    private double fps;
    private long nextSleep = 0;
    private volatile BufferedImage buffer;
    private int k = 0;
    private int direction = 1;
    long count = 0;
    
    private final Random rnd = new Random();

    public Board2() {

        initBoard();
        //buffer = createImage(B_WIDTH, B_HEIGHT);
    }
    
    private synchronized BufferedImage getBuffer() {
    	if (buffer == null) {
    		System.out.println("Buffer is null");
    		//buffer = (BufferedImage)createImage(B_WIDTH, B_HEIGHT);
    		buffer = new BufferedImage(B_WIDTH, B_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    	}
    	return buffer;
    }
    
    private boolean checkImage() {
    	return buffer != null;
    }
    
    private void loadImage() {

        ImageIcon ii = new ImageIcon("star.png");
        star = ii.getImage();
    }

    private void initBoard() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setDoubleBuffered(false);

        loadImage();

        x = INITIAL_X;
        y = INITIAL_Y;
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (checkImage()) {
        	g.drawImage(getBuffer(), 0, 0, null);
        }
        synchronized (this) {
        	this.notifyAll();
		}
    }
    
    
    private void render() {
    	BufferedImage buffer = getBuffer();
    	Graphics g = null;
    	WritableRaster raster = buffer.getRaster();
    	ColorModel cm = buffer.getColorModel();
    	try {
    		if (checkImage()) {
    			//System.out.println("Here! " + count++);
	    		g = buffer.getGraphics();
	    		Color backGround = g.getColor();
	    		g.setColor(Color.WHITE);
	    		g.fillRect(0, 0, B_WIDTH, B_HEIGHT);
	    		
	    		long before = System.currentTimeMillis();
	    		//drawRect2(g);
	    		drawStar(g);
	    		drawRect(raster, cm);
	    		//drawRect2(g);
	    		//drawRect(raster, cm);
	    		long after = System.currentTimeMillis();
	    		System.out.println("MS: " + (after - before));
	    		/*
	    		for (int i = 0; i < 100; i++) {
		    		g.setColor(new Color(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat()));
		    		g.fillRect(20 + i, 20 + i, 300, 300);
	    		}
	    		//*/
	    		//drawRect(g);
	    		//drawStar(g);
	    		g.setColor(Color.white);
	    		g.drawString("" + fps, 20, 20);
	           Toolkit.getDefaultToolkit().sync();
//	    		try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
    		}
    	} finally {
    		if (g != null) {
    			g.dispose();
    		}
    	}
    	repaint();
    	synchronized (this) {
    		try {
				this.wait(250);
			} catch (InterruptedException ignore) {
				
			}
		}
    }
    
    private void drawRect2(Graphics g) {
    	for (int i = 0; i < 255; i++) {
			for (int j = 0; j < 255; j++) {
    			Color c = new Color(k, i, j);
    			g.setColor(c);
    			//System.out.println(rgb);
    			//pixel[0] = rgb;
    			g.drawLine(k+i, k+j, k+i, k+j);
			}
		}
    }
    
    private void drawRect(WritableRaster raster, ColorModel cm) {
    	int alpha = 64;
    	int[] pixelColor = new int[1];
		long start = System.currentTimeMillis();
    	for (int i = 0; i < 255; i++) {
			for (int j = 0; j < 255; j++) {
    			int rgb =  alpha << 24 | k << 16 | j << 8 | i;
    			//System.out.println(rgb);
    			//pixelColor[0] = rgb;
    			cm.getDataElements(rgb, pixelColor);
    			//buffer.setRGB(k + i , k + j, rgb);
    			raster.setDataElements(k + i, k + j, pixelColor);
    			
			}
		}
    	long diff = System.currentTimeMillis() - start;
    	System.out.printf("Diff %d\n", diff);
    }
    
    private void drawStar(Graphics g) {

        g.drawImage(star, x, y, this);
//        g.setColor(Color.RED);
//        g.drawString("" + fps, 20, 20);
//        Toolkit.getDefaultToolkit().sync();
    }

    private void cycle() {

        x += 1;
        y += 1;

        if (y > B_HEIGHT) {

            y = INITIAL_Y;
            x = INITIAL_X;
        }
    	if (direction == 1) {
    		k+=1;
    	} else {
    		k-=1;
    	}
    	if (k == 0 || k >= 255) {
    		direction *= -1;
    	}
    }

    @Override
    public void run() {

        //long beforeTime, timeDiff, sleep;

        //beforeTime = System.currentTimeMillis();
        double lastUpdateTime = System.currentTimeMillis();
        
        while (true) {
        	long now = System.currentTimeMillis();
        	int iter = 0;
        	//boolean firstTime = true;
        	while (now - lastUpdateTime >= TIME_BETWEEN_UPDATES && iter < MAX_ITERATIONS) {
            	/*
        		if (iter == 0) {
            		double diff = now - lastUpdateTime;
            		fps = (int) (1000d / diff);
            	}
            	*/
        		if (iter > 0) {
        			System.out.println("Iteration: " + iter);
        		}
        		cycle();
            	lastUpdateTime += TIME_BETWEEN_UPDATES;
            	iter++;
            }
        	///*
        	try {
				Thread.sleep(nextSleep);
			} catch (InterruptedException ignore) {	
			}
			//*/
        	render();
        	fps = 1000d / (now - was);
        	was = now;
        	
        	if (fps > DESIRED_FPS) {
        		nextSleep++;
        	}
        	
        	if (fps < DESIRED_FPS) {
        		if (nextSleep != 0) {
        			nextSleep--;
        		}
        	}
        	//System.out.println("Next Sleep: " + nextSleep);
        }
    }
}