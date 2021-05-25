package com.klima7.server;

import com.klima7.app.Activity;
import com.klima7.app.ModuleActivity;

import javax.swing.*;
import java.io.IOException;

public class ServerIdleActivity extends Activity {

	private Server server;

	@Override
	public void initUI() {
		setLayout(null);

		JLabel text = new JLabel("Waiting for clients");
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(170, 170, 700, 50);
		add(text);
	}

	@Override
	public void onStart() {
		super.onStart();
		server = Server.getInstance();

		try {
			server.startDiscoveryListen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}