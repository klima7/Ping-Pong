package com.klima7.server;

import com.klima7.app.GameData;
import com.klima7.app.GameActivity;
import com.klima7.app.GameStatus;
import com.klima7.app.ModuleActivity;
import com.klima7.simulation.Simulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerGameActivity extends GameActivity {

	private final Simulation simulation = new Simulation();
	private boolean expectedDisconnect = false;

	public ServerGameActivity(String myNick, Client client) {
		super(myNick, client.getNick(), client.getSocket());
	}

	@Override
	public void onStart() {
		super.onStart();
		simulation.start();
	}

	@Override
	public void onStop() {
		super.onStop();
		simulation.stop();
	}

	@Override
	public void backClicked() {
		expectedDisconnect = true;
		try {
			Server.getInstance().stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		startActivity(new ModuleActivity(myNick));
	}

	@Override
	public void receiveData(DataInputStream dis) {
		try {
			int position = dis.readInt();
			simulation.setClientPosition(position);
		} catch (IOException e) {
			if(expectedDisconnect)
				return;

			simulation.stop();
			showInfoMessage("Client disconnected", "Click ok to wait for another client");
			startActivity(new WaitingActivity(myNick));
		}
	}

	@Override
	public void sendData(DataOutputStream dos) throws IOException {
		if(simulation == null)
			return;

		GameData clientData = simulation.getClientData();
		clientData.sendToStream(dos);
	}

	@Override
	public void updateData() {
		if(simulation == null)
			return;

		GameData gameData = simulation.getServerData();
		setData(gameData);
		simulation.setServerPosition(getPosition());

		if(gameData.getStatus() == GameStatus.WON) {
			expectedDisconnect = true;
			showInfoMessage("You won!", "Congratulation!");
			startActivity(new WaitingActivity(myNick));
		}

		else if(gameData.getStatus() == GameStatus.LOST) {
			expectedDisconnect = true;
			showInfoMessage("You lost!", "Try again!");
			startActivity(new WaitingActivity(myNick));
		}
	}
}
