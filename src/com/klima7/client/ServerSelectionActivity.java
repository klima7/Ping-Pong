package com.klima7.client;

import com.klima7.app.Activity;
import com.klima7.app.ModuleActivity;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ServerSelectionActivity extends Activity {

	private final String nick;
	private JList<Offer> list;
	private final LastServer lastServer = new LastServer();
	private final boolean queryLastServer;

	public ServerSelectionActivity(String nick, boolean queryLastServer) {
		this.nick = nick;
		this.queryLastServer = queryLastServer;
	}

	public ServerSelectionActivity(String nick) {
		this(nick, false);
	}

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

		JButton backButton = new JButton("Back");
		backButton.setFont(backButton.getFont().deriveFont(40f));
		backButton.setBounds(100, 390, 150, 50);
		backButton.addActionListener(e -> backClicked());
		add(backButton);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.setFont(refreshButton.getFont().deriveFont(35f));
		refreshButton.setBounds(260, 390, 180, 50);
		refreshButton.addActionListener(e -> refreshClicked());
		add(refreshButton);

		JButton okButton = new JButton("OK");
		okButton.setFont(okButton.getFont().deriveFont(40f));
		okButton.setBounds(450, 390, 150, 50);
		okButton.addActionListener(e -> okClicked());
		add(okButton);
	}

	@Override
	public void onStart() {
		super.onStart();
		UdpDiscoverer.discoverAsync().thenAccept(servers -> {
			updateList(servers);
			if(queryLastServer)
				queryAboutLastServer(servers);
		});
	}

	private void updateList(List<Offer> offers) {
		Offer[] array = new Offer[offers.size()];
		offers.toArray(array);
		list.setListData(array);
	}

	private void okClicked() {
		Offer offer = list.getSelectedValue();
		if(offer == null) {
			showErrorMessage("No server selected", "You have to select server to start game");
			return;
		}

		try {
			lastServer.set(offer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		connectToServer(offer);
	}

	private void backClicked() {
		startActivity(new ModuleActivity(nick));
	}

	private void refreshClicked() {
		UdpDiscoverer.discoverAsync().thenAccept(this::updateList);
	}

	private void queryAboutLastServer(List<Offer> offers) {
		Offer lastOffer;
		try {
			lastOffer = lastServer.get();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for(Offer offer : offers) {
			if(lastOffer.addressEquals(offer)) {
				boolean connectLast = showQueryDialog(offer);
				if(connectLast) {
					connectToServer(offer);
				}
			}
		}
	}

	private void connectToServer(Offer offer) {
		try {
			Socket socket = new Socket(offer.getAddress(), offer.getPort());
			startActivity(new WaitingActivity(nick, offer.getNick(), socket));
		} catch (IOException e) {
			showErrorMessage("Connection error", "Unable to connect to server");
		}
	}

	private boolean showQueryDialog(Offer offer) {
		String message = "Would you like to connect with last server?\n" + offer;
		int res = JOptionPane.showInternalConfirmDialog(getContext().getContentPane(), message, "Last server",
				JOptionPane.YES_NO_OPTION);
		return res == JOptionPane.YES_OPTION;
	}
}
