package com.klima7.client;

import com.klima7.app.GameData;
import com.klima7.app.GameActivity;
import com.klima7.app.GameStatus;

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
			if(data.getStatus() == GameStatus.WON) {
				controlledDisconnection = true;
				showInfoMessage("You won!", "Congratulation!");
				startActivity(new ServerSelectionActivity(myNick));
			}
			else if(data.getStatus() == GameStatus.LOST) {
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
