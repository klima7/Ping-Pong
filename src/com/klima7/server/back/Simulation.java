package com.klima7.server.back;

import com.klima7.app.back.GameData;

import java.awt.*;

public class Simulation {

	private double clientPosition;
	private double serverPosition;
	private Point ballPosition;
	private int clientPoints;
	private int serverPoints;

	public Simulation() {
	}

	public synchronized GameData getClientData() {
		return new GameData((int)serverPosition, ballPosition, clientPoints, serverPoints);
	}

	public synchronized GameData getServerData() {
		return new GameData((int)clientPosition, ballPosition, serverPoints, clientPoints);
	}

	public synchronized void setClientPosition(int position) {
		this.clientPosition = position;
	}

	public synchronized void setServerPosition(int position) {
		this.serverPosition = position;
	}

	private synchronized void update(long millis) {

	}
}
