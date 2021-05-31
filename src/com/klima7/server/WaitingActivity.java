package com.klima7.server;

import com.klima7.app.Activity;
import com.klima7.app.ModuleActivity;

import javax.swing.*;
import java.io.IOException;

public class WaitingActivity extends Activity {

	private final String nick;
	private Server server;

	public WaitingActivity(String nick) {
		this.nick = nick;
	}

	@Override
	public void initUI() {
		setLayout(null);

		JLabel text = new JLabel("Waiting for clients");
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(170, 170, 700, 50);
		add(text);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(cancelButton.getFont().deriveFont(40f));
		cancelButton.setBounds(200, 270, 300, 50);
		cancelButton.addActionListener(e -> cancelClicked());
		add(cancelButton);
	}

	@Override
	public void onStart() {
		super.onStart();
		server = Server.getInstance();

		try {
			server.start(nick);
		} catch (IOException e) {
			e.printStackTrace();
		}

		server.takeFromQueueAsync().thenAccept(client -> {
			startActivity(new ServerGameActivity(nick, client));
		});
	}

	private void cancelClicked() {
		try {
			server.stop();
			startActivity(new ModuleActivity(nick));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
