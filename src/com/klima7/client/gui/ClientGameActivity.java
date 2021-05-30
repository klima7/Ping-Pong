package com.klima7.client.gui;

import com.klima7.app.back.GameData;
import com.klima7.app.gui.GameActivity;
import com.klima7.client.back.Offer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ClientGameActivity extends GameActivity {

	public ClientGameActivity(String myNick, Offer offer) {
		super(myNick, offer.getNick(), offer.getSocket());
	}

	@Override
	public void receiveData(DataInputStream dis) {
		try {
			GameData data = GameData.getFromStream(dis);
			setData(data);
		} catch (IOException e) {
			showErrorMessage("Connection error", "Connection lost");
			startActivity(new ServerSelectionActivity(myNick));
		}
	}

	@Override
	public void sendData(DataOutputStream dos) {
		try {
			dos.writeInt(getPosition());
		} catch (IOException e) {
//			showErrorMessage("Connection error", "Connection lost");
//			startActivity(new ServerSelectionActivity(myNick));
		}
	}
}
