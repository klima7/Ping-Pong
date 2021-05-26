package com.klima7.client.gui;

import com.klima7.app.gui.Activity;
import com.klima7.app.gui.GameActivity;
import com.klima7.app.gui.NickActivity;
import com.klima7.client.back.Offer;
import com.klima7.client.back.PositionNotifier;

import javax.swing.*;

public class WaitingActivity extends Activity implements PositionNotifier.PositionNotifierListener {

	private Offer offer;
	private PositionNotifier notifier;

	private JLabel text;

	public WaitingActivity(Offer offer) {
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
		System.out.println("OnStart");
		notifier = new PositionNotifier(offer.getSocket(), "klima7", this);
		notifier.start();
	}

	private void cancelClicked() {
		startActivity(new ServerSelectionActivity());
	}

	@Override
	public void onPositionChanged(int position) {
		System.out.println("onPositionchanged " + position);
		setPosition(position);
	}

	@Override
	public void onInvalidNick() {
		System.out.println("onInvalidNick");
		showErrorMessage("Nick conflict", "You opponent have the same nick. Change your nick and try again");
		startActivity(new NickActivity());
	}

	@Override
	public void onValidNick() {
		System.out.println("onValidNick");
		startActivity(new GameActivity());
	}

	@Override
	public void onError() {
		showErrorMessage("Connection error", "Connection with this server has been broken");
	}

	private void setPosition(int position) {
		text.setText("You are " + position + " in queue");
	}
}
