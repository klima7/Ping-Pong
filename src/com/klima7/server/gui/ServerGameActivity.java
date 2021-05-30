package com.klima7.server.gui;

import com.klima7.app.back.GameData;
import com.klima7.app.gui.GameActivity;
import com.klima7.client.gui.ServerSelectionActivity;
import com.klima7.server.back.Client;
import com.klima7.server.back.simulation.Simulation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerGameActivity extends GameActivity {

	private final Simulation simulation = new Simulation();

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
	public void receiveData(DataInputStream dis) {
		try {
			int position = dis.readInt();
			simulation.setClientPosition(position);
		} catch (IOException e) {
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
		setData(simulation.getServerData());
		simulation.setServerPosition(getPosition());
	}
}
