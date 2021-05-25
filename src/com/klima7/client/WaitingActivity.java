package com.klima7.client;

import com.klima7.app.Activity;
import com.klima7.app.App;

import javax.swing.*;
import java.awt.*;

public class WaitingActivity extends Activity {

	private JLabel text;

	public WaitingActivity() {
		setLayout(null);

		text = new JLabel();
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(180, 150, 700, 50);
		add(text);

		JButton okButton = new JButton("Cancel");
		okButton.setFont(okButton.getFont().deriveFont(40f));
		okButton.setBounds(200, 250, 300, 50);
		okButton.addActionListener(e -> okClicked());
		add(okButton);

		setPosition(0);
	}

	private void okClicked() {
		startActivity(new ServerSelectionActivity());
	}

	private void setPosition(int position) {
		text.setText("You are " + position + " in queue");
	}
}
