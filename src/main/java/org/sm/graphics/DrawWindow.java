package org.sm.graphics;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class DrawWindow {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Drawing Window");
			//frame.setSize(800, 600);
			frame.add(new TestBoard010(800, 600));
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);	
		});
	}
}
