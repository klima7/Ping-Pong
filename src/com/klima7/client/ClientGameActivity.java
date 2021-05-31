package com.klima7.client;

import com.klima7.app.GameData;
import com.klima7.app.GameActivity;
import com.klima7.app.GameStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientGameActivity extends GameActivity {

	private boolean expectedDisconnect = false;

	public ClientGameActivity(String myNick, String nick, Socket socket) {
		super(myNick, nick, socket);
	}

	@Override
	public void backClicked() {
		expectedDisconnect = true;
		startActivity(new ServerSelectionActivity(myNick));
	}

	@Override
	public void receiveData(DataInputStream dis) {
		try {
			GameData data = GameData.getFromStream(dis);
			setData(data);

			if(data.getStatus() == GameStatus.WON) {
				expectedDisconnect = true;
				showInfoMessage("You won!", "Congratulation!");
				startActivity(new ServerSelectionActivity(myNick));
			}

			else if(data.getStatus() == GameStatus.LOST) {
				expectedDisconnect = true;
				showInfoMessage("You lost!", "Maybe next time");
				startActivity(new ServerSelectionActivity(myNick));
			}

		} catch (IOException e) {
			if(expectedDisconnect)
				return;

			showInfoMessage("Server disconnected", "Press ok and select another server");
			startActivity(new ServerSelectionActivity(myNick));
		}
	}

	@Override
	public void sendData(DataOutputStream dos) throws IOException {
		dos.writeInt(getPosition());
	}
}
