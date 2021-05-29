package com.klima7.server.gui;

import com.klima7.app.back.GameData;
import com.klima7.app.gui.GameActivity;
import com.klima7.server.back.Simulation;

public class ServerGameActivity extends GameActivity {

	private final Simulation simulation = new Simulation();

	public ServerGameActivity(String myNick, String opponentNick) {
		super(myNick, opponentNick);
	}

	@Override
	public void onStart() {
		super.onStart();
		simulation.start();
	}

	@Override
	public GameData getData() {
		return simulation.getServerData();
	}
}
