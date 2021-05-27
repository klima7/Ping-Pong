package com.klima7.server.back;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class GameManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameManager.class);

	private boolean inProgress;

	public void startGame(Client client) {
		LOGGER.info("Starting new game");
		inProgress = true;
	}

	public boolean isGameInProgress() {
		return inProgress;
	}

	public interface GameManagerListener {
		void onGameFinished();
	}
}
