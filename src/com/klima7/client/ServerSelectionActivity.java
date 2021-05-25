package com.klima7.client;

import com.klima7.app.Activity;

import javax.swing.*;
import java.awt.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerSelectionActivity extends Activity {

	private JList<String> list;
	private final List<String> entries = new ArrayList();

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
		DiscoverySender.discoverAsync().thenAccept(list -> updateList(list));
	}

	private void updateList(List<InetSocketAddress> discovered) {
		System.out.println("Discovered");
		System.out.println(discovered);

		for(InetSocketAddress address : discovered) {
			addEntry(address.getHostString() + ":" + address.getPort());
		}
	}

	private void okClicked() {
		startActivity(new WaitingActivity());
	}

	private void addEntry(String entry) {
		entries.add(entry);
		String[] array = new String[entries.size()];
		entries.toArray(array);
		list.setListData(array);
	}
}
