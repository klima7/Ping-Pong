package com.klima7.app.back;

import java.awt.*;

public class GameData {

	private final int playerPosition;
	private final Point ballPosition;
	private final int myScore;
	private final int opponentScore;

	public GameData(int playerPosition, Point ballPosition, int myScore, int opponentScore) {
		this.playerPosition = playerPosition;
		this.ballPosition = ballPosition;
		this.myScore = myScore;
		this.opponentScore = opponentScore;
	}

	public int getPlayerPosition() {
		return playerPosition;
	}

	public Point getBallPosition() {
		return ballPosition;
	}

	public int getMyScore() {
		return myScore;
	}

	public int getOpponentScore() {
		return opponentScore;
	}
}
