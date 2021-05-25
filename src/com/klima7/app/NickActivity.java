package com.klima7.app;

import javax.swing.*;
import java.awt.*;

public class NickActivity extends Activity {

	private JTextField nickField;

	public NickActivity() {
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
		startActivity(new ModuleActivity());
	}
}
