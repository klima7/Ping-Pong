package com.klima7.app.gui;

import com.klima7.app.back.GameData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import static com.klima7.app.back.Constants.*;

public abstract class GameActivity extends Activity {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameActivity.class);

	public static final int MAP_X = 50;
	public static final int MAP_Y = 60;
	public static final double PLAYER_SPEED = 0.4;

	private final String myNick;
	private final String opponentNick;

	private double myVelocity = 0;
	private double myPosition = 100;

	private GameData data;
	private Socket socket;
	private Timer timer;

	private GameListener listener;

	public GameActivity(String myNick, String opponentNick, Socket socket) {
		this.myNick = myNick;
		this.opponentNick = opponentNick;
		this.socket = socket;

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				updateData();
				updateGame(10);
				triggerSendData();
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
				myVelocity = 0;
			}
		});

		try {
			listener = new GameListener(socket.getInputStream());
			listener.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void updateGame(int elapsedMillis) {
		myPosition += this.myVelocity *elapsedMillis;

		if(myPosition < 0) {
			myPosition = 0;
			myVelocity = 0;
		}

		if(myPosition > MAP_HEIGHT - PLAYER_WIDTH) {
			myPosition = MAP_HEIGHT - PLAYER_WIDTH;
			myVelocity = 0;
		}
	}

	private void triggerSendData() {
		try {
			DataOutputStream os = new DataOutputStream(socket.getOutputStream());
			sendData(os);
		} catch (IOException e) {
			e.printStackTrace();
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
		if(this.data == null)
			return;
		String text = data.getOpponentScore() + ":" + data.getMyScore();
		g2.drawString(text, 330, 40);
	}

	private void drawPlayers(Graphics2D g2) {
		g2.setPaint(Color.red);
		g2.fillRect(MAP_X+MAP_WIDTH, MAP_Y+(int) myPosition, 10, PLAYER_WIDTH);
		if(this.data != null)
			g2.fillRect(MAP_X-10, MAP_Y+data.getPlayerPosition(), 10, PLAYER_WIDTH);
	}

	private void drawBall(Graphics2D g2) {
		if(this.data == null)
			return;

		g2.setPaint(Color.YELLOW);
		g2.fillOval(MAP_X+data.getBallPosition().x, MAP_Y+data.getBallPosition().y, BALL_SIZE, BALL_SIZE);
	}

	public int getPosition() {
		return (int)myPosition;
	}

	public void setData(GameData data) {
		this.data = data;
	}

	public void updateData() {};

	public abstract void receiveData(DataInputStream dis);

	public abstract void sendData(DataOutputStream dos);


	private class GameListener extends Thread {

		private final DataInputStream dis;

		public GameListener(InputStream is) {
			this.dis = new DataInputStream(is);
		}

		@Override
		public void run() {
			LOGGER.info("Starting GameListener");
			while(!Thread.interrupted()) {
				receiveData(dis);
			}
			LOGGER.info("Exiting GameListener");
		}
	}

}
