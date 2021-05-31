package com.klima7.client;

import com.klima7.app.Activity;
import com.klima7.app.NickActivity;

import javax.swing.*;

public class WaitingActivity extends Activity implements WaitingAssistant.PositionNotifierListener {

	private String nick;
	private Offer offer;
	private WaitingAssistant assistant;

	private JLabel text;

	public WaitingActivity(String nick, Offer offer) {
		this.nick = nick;
		this.offer = offer;
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
		assistant = new WaitingAssistant(offer.getSocket(), nick, this);
		assistant.start();
	}

	@Override
	public void onStop() {
		super.onStop();
		assistant.quit();
	}

	private void cancelClicked() {
		startActivity(new ServerSelectionActivity(nick));
	}

	@Override
	public void onPositionInQueueChanged(int position) {
		setPosition(position);
	}

	@Override
	public void onInvalidNick() {
		showErrorMessage("Nick conflict", "You opponent have the same nick. Change your nick and try again");
		startActivity(new NickActivity());
	}

	@Override
	public void onValidNick() {
		startActivity(new ClientGameActivity(nick, offer));
	}

	@Override
	public void onError() {
		showErrorMessage("Connection error", "Connection with server lost");
		assistant.quit();
		startActivity(new ServerSelectionActivity(nick));
	}

	private void setPosition(int position) {
		text.setText("You are " + position + " in queue");
	}
}