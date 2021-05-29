package com.klima7.app.back;

import java.awt.*;
import java.io.*;

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

	public void sendToStream(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeInt(playerPosition);
		dos.writeInt(ballPosition.x);
		dos.writeInt(ballPosition.y);
		dos.writeInt(myScore);
		dos.writeInt(opponentScore);
		dos.flush();
	}

	public static GameData getFromStream(InputStream is) throws IOException {
		DataInputStream dis = new DataInputStream(is);
		int playerPosition = dis.readInt();
		int ballX = dis.readInt();
		int ballY = dis.readInt();
		int myScore = dis.readInt();
		int opponentScore = dis.readInt();
		return new GameData(playerPosition, new Point(ballX, ballY), myScore, opponentScore);
	}
}
