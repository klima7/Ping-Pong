package com.klima7.simulation;

import com.klima7.app.GameData;
import com.klima7.app.GameStatus;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static com.klima7.app.Constants.*;


public class Simulation {

	public static final int FPS = 30;
	public static final int WON_SCORE = 1;

	private final Ball ball;
	private final Player client;
	private final Player server;

	private int clientPoints;
	private int serverPoints;

	private final Timer timer = new Timer();
	private TimerTask updateTask;

	public Simulation() {
		ball = new Ball(this);
		server = new Player(this, MAP_WIDTH);
		client = new Player(this, -PLAYER_WIDTH);

		clientPoints = 0;
		serverPoints = 0;

		nextTurn();
	}

	private void nextTurn() {
		ball.centerPosition();
		int turnNumber = serverPoints + clientPoints;
		if(turnNumber % 2 == 0) ball.setVx(Ball.SPEED);
		else ball.setVx(-Ball.SPEED);
	}

	private synchronized void update(long millis) {

		if(getStatus(serverPoints, clientPoints) != GameStatus.PENDING)
			return;

		ball.update(millis);

		if(ball.getX() < -100) {
			serverPoints++;
			nextTurn();
		}

		else if(ball.getX() > MAP_WIDTH+100) {
			clientPoints++;
			nextTurn();
		}
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

	public Ball getBall() {
		return ball;
	}

	public Player getClient() {
		return client;
	}

	public Player getServer() {
		return server;
	}

	private static GameStatus getStatus(int myPoints, int opponentPoints) {
		if(myPoints >= WON_SCORE) return GameStatus.WON;
		else if(opponentPoints >= WON_SCORE) return GameStatus.LOST;
		else return GameStatus.PENDING;
	}

	public synchronized GameData getClientData() {
		Point ballPosition = new Point((int)(MAP_WIDTH-ball.getX()-ball.getWidth()), (int)ball.getY());
		GameStatus status = getStatus(clientPoints, serverPoints);
		return new GameData(server.getPosition(), ballPosition, clientPoints, serverPoints, status);
	}

	public synchronized GameData getServerData() {
		Point ballPosition = new Point((int)ball.getX(), (int)ball.getY());
		GameStatus status = getStatus(serverPoints, clientPoints);
		return new GameData(client.getPosition(), ballPosition, serverPoints, clientPoints, status);
	}

	public synchronized void setClientPosition(int position) {
		client.setPosition(position);
	}

	public synchronized void setServerPosition(int position) {
		server.setPosition(position);
	}
}
