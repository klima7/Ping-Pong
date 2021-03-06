package com.klima7.app;

import java.awt.*;
import java.io.*;

public class GameData {

	private final int playerPosition;
	private final Point ballPosition;
	private final int myScore;
	private final int opponentScore;
	private final GameStatus status;

	public GameData(int playerPosition, Point ballPosition, int myScore, int opponentScore, GameStatus status) {
		this.playerPosition = playerPosition;
		this.ballPosition = ballPosition;
		this.myScore = myScore;
		this.opponentScore = opponentScore;
		this.status = status;
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

	public GameStatus getStatus() {
		return status;
	}

	public void sendToStream(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeInt(playerPosition);
		dos.writeInt(ballPosition.x);
		dos.writeInt(ballPosition.y);
		dos.writeInt(myScore);
		dos.writeInt(opponentScore);
		dos.writeInt(status.getValue());
		dos.flush();
	}

	public static GameData getFromStream(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		int playerPosition = dis.readInt();
		int ballX = dis.readInt();
		int ballY = dis.readInt();
		int myScore = dis.readInt();
		int opponentScore = dis.readInt();
		int status = dis.readInt();
		return new GameData(playerPosition, new Point(ballX, ballY), myScore, opponentScore, GameStatus.valueOf(status));
	}
}
