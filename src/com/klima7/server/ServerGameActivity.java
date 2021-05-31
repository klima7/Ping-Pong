package com.klima7.server;

import com.klima7.app.GameData;
import com.klima7.app.GameActivity;
import com.klima7.app.ModuleActivity;
import com.klima7.simulation.Simulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerGameActivity extends GameActivity {

	private final Simulation simulation = new Simulation();
	private boolean controlledDisconnection = false;

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
		controlledDisconnection = true;
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
			if(controlledDisconnection) return;
			simulation.stop();
			showErrorMessage("Client disconnected", "Client disconnected");
			startActivity(new WaitingActivity(myNick));
		}
	}

	@Override
	public void sendData(DataOutputStream dos) {
		try {
			if(simulation == null) return;
			GameData clientData = simulation.getClientData();
			clientData.sendToStream(dos);
		} catch (IOException e) {
		}
	}

	@Override
	public void updateData() {
		if(simulation == null) return;
		GameData gameData = simulation.getServerData();
		setData(gameData);
		simulation.setServerPosition(getPosition());
		if(gameData.getStatus() == GameData.STATUS_WON) {
			controlledDisconnection = true;
			showInfoMessage("You won!", "Congratulation!");
			startActivity(new WaitingActivity(myNick));
		}
		else if(gameData.getStatus() == GameData.STATUS_LOST) {
			controlledDisconnection = true;
			showInfoMessage("You lost!", "Maybe next time");
			startActivity(new WaitingActivity(myNick));
		}
	}
}
