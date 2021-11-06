import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Worker {
	Worker(Board b, int scaleFactor) {
		JFrame frame = new JFrame("Combat Simulator");
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);

		GPanel mainPanel = new GPanel(scaleFactor, b);
		mainPanel.setPreferredSize(new Dimension(scaleFactor * (b.gridWidth + 3), scaleFactor * b.gridHeight));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		MouseAdapter m = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX() / scaleFactor;
				int y = e.getY() / scaleFactor;

				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!(b.selX == x && b.selY == y)) {
						if ((x < b.gridWidth && y < b.gridHeight)) {
							b.selX = x;
							b.selY = y;
						}
					} else {
						b.selX = -1;
						b.selY = -1;
					}
				}

				else if (e.getButton() == MouseEvent.BUTTON3) {
					if (!(b.sel2X == x && b.sel2Y == y)) {
						if ((x < b.gridWidth && y < b.gridHeight)) {
							b.sel2X = x;
							b.sel2Y = y;
						}
					} else {
						b.sel2X = -1;
						b.sel2Y = -1;
					}
				}
				frame.repaint();
			}
		};
		mainPanel.addMouseListener(m);

		frame.add(mainPanel, BorderLayout.WEST);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}