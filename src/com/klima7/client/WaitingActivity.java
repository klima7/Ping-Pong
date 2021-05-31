package com.klima7.client;

import com.klima7.app.Activity;
import com.klima7.app.NickActivity;

import javax.swing.*;
import java.net.Socket;

public class WaitingActivity extends Activity implements WaitingAssistant.PositionNotifierListener {

	private final String myNick;
	private final String serverNick;
	private final Socket socket;
	private WaitingAssistant assistant;

	private JLabel text;

	public WaitingActivity(String myNick, String serverNick, Socket socket) {
		this.myNick = myNick;
		this.serverNick = serverNick;
		this.socket = socket;
	}

	@Override
	public void initUI() {
		setLayout(null);

		text = new JLabel();
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(180, 150, 700, 50);
		add(text);

		JButton okButton = new JButton("Cancel");
		okButton.setFont(okButton.getFont().deriveFont(40f));
		okButton.setBounds(200, 250, 300, 50);
		okButton.addActionListener(e -> cancelClicked());
		add(okButton);

		setPosition(0);
	}

	@Override
	public void onStart() {
		super.onStart();
		assistant = new WaitingAssistant(socket, myNick, this);
		assistant.start();
	}

	@Override
	public void onStop() {
		super.onStop();
		assistant.quit();
	}

	private void cancelClicked() {
		startActivity(new ServerSelectionActivity(myNick));
	}

	@Override
	public void onPositionInQueueChanged(int position) {
		setPosition(position);
	}

	@Override
	public void onInvalidNick() {
		showErrorMessage("Nick conflict", "You opponent have the same nick. Click ok and change nick");
		startActivity(new NickActivity());
	}

	@Override
	public void onValidNick() {
		startActivity(new ClientGameActivity(myNick, serverNick, socket));
	}

	@Override
	public void onError() {
		showErrorMessage("Connection error", "Connection with server lost");
		assistant.quit();
		startActivity(new ServerSelectionActivity(myNick));
	}

	private void setPosition(int position) {
		text.setText("You are " + position + " in queue");
	}
}
