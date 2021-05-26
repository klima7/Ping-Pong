package com.klima7.server.back;

import java.net.Socket;

public class GameManager {

	private boolean inProgress;

	public void startGame(Socket socket) {
		inProgress = true;
	}

	public boolean isGameInProgress() {
		return inProgress;
	}

	public interface GameManagerListener {
		void onGameFinished();
	}
}
