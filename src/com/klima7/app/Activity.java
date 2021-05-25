package com.klima7.app;

import javax.swing.*;

public class Activity extends JPanel {

	private App context;

	public App getContext() {
		return context;
	}

	public void setContext(App context) {
		this.context = context;
	}

	public void startActivity(Activity activity) {
		context.setActivity(activity);
	}
}
