package com.klima7.app.gui;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

	private int x;
	private Timer timer;

	public GameActivity() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				repaint();
			}
		}, 0, 1);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		drawDonut(g);
	}

	private void drawDonut(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		RenderingHints rh
				= new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		rh.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setRenderingHints(rh);

		Dimension size = getSize();
		double w = size.getWidth();
		double h = size.getHeight();

		Ellipse2D e = new Ellipse2D.Double(0, 0, 80, 80);
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.gray);

		AffineTransform at = AffineTransform.getTranslateInstance(x++, h/2);
		g2d.draw(at.createTransformedShape(e));
	}
}
