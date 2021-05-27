package com.klima7.app.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

	public static final int MAP_WIDTH = 600;
	public static final int MAP_HEIGHT = 360;
	public static final int MAP_X = 50;
	public static final int MAP_Y = 60;

	public static final int PLAYER_WIDTH = 40;
	public static final double PLAYER_SPEED = 0.4;

	public static final int BALL_SIZE = 10;

	private double myVelocity;

	private String myNick = "klima7";
	private String opponentNick = "opponent";
	private int myPoints;
	private int opponentPoints;
	private double myPos = 100;
	private int opponentPos = 100;
	private Point ballPosition = new Point(40, 40);

	private Timer timer;

	public GameActivity() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateGame(10);
				repaint();
			}
		}, 0, 10);
	}

	@Override
	public void onStart() {
		super.onStart();
		getContext().requestFocus();

		getContext().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e);
				if(e.getKeyCode() == KeyEvent.VK_UP)
					myVelocity = -PLAYER_SPEED;
				else if(e.getKeyCode() == KeyEvent.VK_DOWN)
					myVelocity = PLAYER_SPEED;
			}

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				System.out.println("Released");
				myVelocity = 0;
			}
		});
	}

	void updateGame(int elapsedMillis) {
		myPos += this.myVelocity *elapsedMillis;

		if(myPos < 0) {
			myPos = 0;
			myVelocity = 0;
		}

		if(myPos > MAP_HEIGHT - PLAYER_WIDTH) {
			myPos = MAP_HEIGHT - PLAYER_WIDTH;
			myVelocity = 0;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		configRender(g2);

		drawBackground(g2);
		drawGameArea(g2);
		drawNicks(g2);
		drawPoints(g2);
		drawPlayers(g2);
		drawBall(g2);
	}

	void configRender(Graphics2D g2) {
		RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(rh);
	}

	private void drawBackground(Graphics2D g2) {
		g2.setPaint(Color.pink);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}

	private void drawGameArea(Graphics2D g2) {
		g2.setPaint(Color.GREEN);
		g2.fillRect(MAP_X, MAP_Y, MAP_WIDTH, MAP_HEIGHT);
		g2.setPaint(Color.WHITE);
		g2.setStroke(new BasicStroke(4));
		g2.drawLine(350, 60, 350, 420);
	}

	private void drawNicks(Graphics2D g2) {
		g2.setPaint(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25f));
		g2.drawString(opponentNick, 100, 40);
		g2.drawString(myNick, 500, 40);
	}

	private void drawPoints(Graphics2D g2) {
		String text = opponentPoints + ":" + myPoints;
		g2.drawString(text, 330, 40);
	}

	private void drawPlayers(Graphics2D g2) {
		g2.setPaint(Color.red);
		g2.fillRect(MAP_X-15, MAP_Y+opponentPos, 10, PLAYER_WIDTH);
		g2.fillRect(MAP_X+MAP_WIDTH+5, MAP_Y+(int)myPos, 10, PLAYER_WIDTH);
	}

	private void drawBall(Graphics2D g2) {
		g2.setPaint(Color.YELLOW);
		int baseX = MAP_X + MAP_WIDTH/2;
		int baseY = MAP_Y;
		g2.fillOval(baseX+ballPosition.x, baseY+ballPosition.y, BALL_SIZE, BALL_SIZE);
	}
}
