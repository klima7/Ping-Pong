package com.klima7.app;

import com.klima7.client.ServerSelectionActivity;
import com.klima7.server.ServerIdleActivity;

import javax.swing.*;
import java.awt.*;

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
	}

	private void clientSelected() {
		startActivity(new ServerSelectionActivity());
	}

	private void serverSelected() {
		startActivity(new ServerIdleActivity());
	}
}
