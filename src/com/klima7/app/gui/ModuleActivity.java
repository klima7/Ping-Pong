package com.klima7.app.gui;

import com.klima7.client.gui.ServerSelectionActivity;
import com.klima7.server.back.Server;
import com.klima7.server.gui.WaitingActivity;

import javax.swing.*;

public class ModuleActivity extends Activity {

	private String nick;

	public ModuleActivity(String nick) {
		this.nick = nick;
	}

	@Override
	public void initUI() {
		setLayout(null);

		JLabel text = new JLabel("Select mode");
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(210, 30, 700, 50);
		add(text);

		JButton clientButton = new JButton("Client");
		clientButton.setFont(clientButton.getFont().deriveFont(40f));
		clientButton.setBounds(20, 150, 300, 200);
		clientButton.addActionListener(e -> clientSelected());
		add(clientButton);

		JButton serverButton = new JButton("Server");
		serverButton.setFont(serverButton.getFont().deriveFont(40f));
		serverButton.setBounds(370, 150, 300, 200);
		serverButton.addActionListener(e -> serverSelected());
		add(serverButton);

		JButton backButton = new JButton("Back");
		backButton.setFont(backButton.getFont().deriveFont(40f));
		backButton.setBounds(200, 380, 300, 50);
		backButton.addActionListener(e -> backSelected());
		add(backButton);
	}

	private void clientSelected() {
		startActivity(new ServerSelectionActivity(nick, true));
	}

	private void serverSelected() {
		startActivity(new WaitingActivity(nick));
	}

	private void backSelected() {
		startActivity(new NickActivity());
	}
}
