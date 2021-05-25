package com.klima7.app;

import javax.swing.*;
import java.awt.*;

public class NickActivity extends Activity {

	public static final int MIN_NICK_LENGTH = 5;

	private JTextField nickField;

	@Override
	public void initUI() {
		setLayout(null);

		JLabel text = new JLabel("Enter you nick");
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(210, 120, 700, 50);
		add(text);

		nickField = new JTextField();
		nickField.setFont(nickField.getFont().deriveFont(40f));
		nickField.setBounds(200, 200, 300, 50);
		add(nickField);

		JButton okButton = new JButton("OK");
		okButton.setFont(okButton.getFont().deriveFont(40f));
		okButton.setBounds(200, 270, 300, 50);
		okButton.addActionListener(e -> okClicked());
		add(okButton);
	}

	private void okClicked() {
		String nick = nickField.getText();
		if(nick.length() < MIN_NICK_LENGTH) {
			showInfoMessage("Nick too short", "You nick must have at least " + MIN_NICK_LENGTH + " characters");
			return;
		}
		startActivity(new ModuleActivity(nick));
	}
}
