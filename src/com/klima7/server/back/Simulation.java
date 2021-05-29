package com.klima7.server.back;

import com.klima7.app.back.GameData;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.klima7.app.back.Constants.*;


public class Simulation {

	public static final int BALL_SPEED = 100;
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

	public synchronized GameData getClientData() {
		Point ballPosition = new Point((int)-ballX, (int)ballY);
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
}
