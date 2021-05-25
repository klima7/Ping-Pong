package com.klima7.client;

import com.klima7.app.Activity;
import com.klima7.app.App;
import com.klima7.app.ModuleActivity;

import javax.swing.*;
import java.awt.*;

public class ServerSelectionActivity extends Activity {

	@Override
	public void initUI() {
		setLayout(null);

		JLabel text = new JLabel("Select server");
		text.setFont(text.getFont().deriveFont(40f));
		text.setBounds(210, 10, 700, 50);
		add(text);

		JList list = new JList(new String[] {"Hello", "There"});
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

	private void okClicked() {
		startActivity(new WaitingActivity());
	}
}
