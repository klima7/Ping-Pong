package com.klima7.client.gui;

import com.klima7.app.gui.Activity;
import com.klima7.client.back.UdpDiscovery;
import com.klima7.client.back.Offer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerSelectionActivity extends Activity {

	private JList<Offer> list;
	private final List<Offer> entries = new ArrayList<>();

	@Override
	public void initUI() {
		setLayout(null);

		JLabel text = new JLabel("Select server");
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(210, 10, 700, 50);
		add(text);

		list = new JList<>();
		list.setFont(list.getFont().deriveFont(30f));
		list.setBounds(100, 70, 500, 300);
		list.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.BLACK, 2),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		add(list);

		JButton okButton = new JButton("OK");
		okButton.setFont(okButton.getFont().deriveFont(40f));
		okButton.setBounds(100, 390, 500, 50);
		okButton.addActionListener(e -> okClicked());
		add(okButton);
	}

	@Override
	public void onStart() {
		super.onStart();
		UdpDiscovery.discoverAsync().thenAccept(this::updateList);
	}

	private void updateList(List<Offer> offers) {
		for(Offer offer : offers) {
			addOffer(offer);
		}
	}

	private void addOffer(Offer offer) {
		entries.add(offer);
		Offer[] array = new Offer[entries.size()];
		entries.toArray(array);
		list.setListData(array);
	}

	private void okClicked() {
		Offer offer = list.getSelectedValue();
		if(offer == null) {
			showErrorMessage("No server selected", "You have to select server to start game");
			return;
		}

		try {
			offer.connect();
		} catch (IOException e) {
			showErrorMessage("Connection error", "Unable to connect to server");
			return;
		}

		startActivity(new WaitingActivity(offer));
	}
}
