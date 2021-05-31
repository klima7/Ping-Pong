package com.klima7.client.gui;

import com.klima7.app.back.GameData;
import com.klima7.app.gui.GameActivity;
import com.klima7.app.gui.ModuleActivity;
import com.klima7.client.back.Offer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientGameActivity extends GameActivity {

	private boolean controlledDisconnection = false;

	public ClientGameActivity(String myNick, Offer offer) {
		super(myNick, offer.getNick(), offer.getSocket());
	}

	@Override
	public void backClicked() {
		controlledDisconnection = true;
		startActivity(new ServerSelectionActivity(myNick));
	}

	@Override
	public void receiveData(DataInputStream dis) {
		try {
			GameData data = GameData.getFromStream(dis);
			setData(data);
			if(data.getStatus() == GameData.STATUS_WON) {
				controlledDisconnection = true;
				showInfoMessage("You won!", "Congratulation!");
				startActivity(new ServerSelectionActivity(myNick));
			}
			else if(data.getStatus() == GameData.STATUS_LOST) {
				controlledDisconnection = true;
				showInfoMessage("You lost!", "Maybe next time");
				startActivity(new ServerSelectionActivity(myNick));
			}
		} catch (IOException e) {
			if(controlledDisconnection)
				return;
			showErrorMessage("Connection error", "Connection lost");
			startActivity(new ServerSelectionActivity(myNick));
		}
	}

	@Override
	public void sendData(DataOutputStream dos) {
		try {
			dos.writeInt(getPosition());
		} catch (IOException ignored) { }
	}
}
