package com.klima7.server.back;

import com.klima7.app.back.GameData;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.klima7.app.back.Constants.*;


public class Simulation {

	public static final double BALL_SPEED = 100;
	public static final double BOUNCE_FACTOR = 5;
	public static final int FPS = 30;

	private int clientPosition;
	private int serverPosition;

	private double ballX, ballY;
	private double ballVelX, ballVelY;
	private int clientPoints;
	private int serverPoints;

	private Timer timer = new Timer();
	private TimerTask updateTask;

	public Simulation() {
		clientPoints = 0;
		serverPoints = 0;
		nextTurn();
	}

	private void nextTurn() {
		int turnNumber = serverPoints + clientPoints;

		ballX = MAP_WIDTH / 2 - BALL_SIZE / 2;
		ballY = MAP_HEIGHT / 2 - BALL_SIZE / 2;

		if(turnNumber % 2 == 0)
			ballVelX = BALL_SPEED;
		else
			ballVelX = -BALL_SPEED;
	}

	private synchronized void update(long millis) {
		ballX += ballVelX * millis / 1000;
		ballY += ballVelY * millis / 1000;
	}

	private double isBallCollidePlayer(int platformX, int platformY) {
		if(ballX < platformX)
	}

	public void start() {
		updateTask = new TimerTask() {
			@Override
			public void run() {
				update(1000/FPS);
			}
		};

		timer.scheduleAtFixedRate(updateTask, 0, 1000/FPS);
	}

	public void stop() {
		updateTask.cancel();
		timer.purge();
	}

	public synchronized GameData getClientData() {
		Point ballPosition = new Point((int)(MAP_WIDTH-ballX), (int)ballY);
		return new GameData(serverPosition, ballPosition, clientPoints, serverPoints);
	}

	public synchronized GameData getServerData() {
		Point ballPosition = new Point((int)ballX, (int)ballY);
		return new GameData(clientPosition, ballPosition, serverPoints, clientPoints);
	}

	public synchronized void setClientPosition(int position) {
		this.clientPosition = position;
	}

	public synchronized void setServerPosition(int position) {
		this.serverPosition = position;
	}
}

class Sprite {

	private double x, y;
	private final double width, height;

	public Sprite(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public boolean isCollide(Sprite other) {
		return (x < other.x + other.width &&
				x + width > other.x &&
				y < other.y + other.height &&
				y + height > other.y);
	}

	public double getVerticalCollisionOffset(Sprite other) {
		double center1 = y+height/2;
		double center2 = other.y+other.height/2;
		return (center2-center1) / (getHeight() / 2);
	}
}
