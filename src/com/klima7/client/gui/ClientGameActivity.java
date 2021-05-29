package com.klima7.client.gui;

import com.klima7.app.back.GameData;
import com.klima7.app.gui.GameActivity;

public class ClientGameActivity extends GameActivity {

	public ClientGameActivity(String myNick, String opponentNick) {
		super(myNick, opponentNick);
	}

	@Override
	public GameData getData() {
		return null;
	}
}
