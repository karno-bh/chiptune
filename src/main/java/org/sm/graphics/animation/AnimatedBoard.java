package org.sm.graphics.animation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.*;

import org.sm.graphics.TestBoard010;

public abstract class AnimatedBoard extends JPanel implements Runnable {

	/**
	 * Make a compiler happy...
	 */
	private static final long serialVersionUID = 1L;

	protected final int GAME_HERZ = 30;
	private final double TIME_BETWEEN_UPDATES = 1000d / GAME_HERZ;
	private final int MAX_ITERATIONS = GAME_HERZ;
	private final double DESIRED_FPS = GAME_HERZ * 2;

	private Thread animator;
	private long was = System.currentTimeMillis();
	private long nextSleep = 0;
	long count = 0;
	
	
	protected final int bWidth;
	protected final int bHeight;
	protected double fps;
	protected final BufferedImage buffer;

	public AnimatedBoard(int boardWidth, int boardHeight) {
		this.bWidth = boardWidth;
		this.bHeight = boardHeight;
		this.buffer =  new BufferedImage(bWidth, bHeight, BufferedImage.TYPE_INT_ARGB);;
		initBoard();
	}

	private void initBoard() {
		setPreferredSize(new Dimension(bWidth, bHeight));
		setDoubleBuffered(false);
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
		g.drawImage(buffer, 0, 0, null);
		synchronized (this) {
			this.notifyAll();
		}
	}

	protected abstract void render();

	protected abstract void cycle();
	
	protected abstract void onInit();

	@Override
	public void run() {
		onInit();
		double lastUpdateTime = System.currentTimeMillis();
		while (true) {
			long now = System.currentTimeMillis();
			int iter = 0;
			while (now - lastUpdateTime >= TIME_BETWEEN_UPDATES && iter < MAX_ITERATIONS) {
				cycle();
				lastUpdateTime += TIME_BETWEEN_UPDATES;
				iter++;
			}
			try {
				Thread.sleep(nextSleep);
			} catch (InterruptedException ignore) {
			}
			render();
			repaint();
			synchronized (this) {
				try {
					this.wait(250);
				} catch (InterruptedException ignore) {
				}
			}
			Toolkit.getDefaultToolkit().sync();
			fps = 1000d / (now - was);
			was = now;
			if (fps > DESIRED_FPS) {
				nextSleep++;
			} else if (fps < DESIRED_FPS && nextSleep != 0) {
				nextSleep--;
			}
		}
	}
	
	protected final void start() {
		start(this);
	}
	
	public static void start(AnimatedBoard board) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Drawing Window");
			//frame.setSize(800, 600);
			frame.add(board);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}
}