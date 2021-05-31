package com.klima7.app;

import java.util.HashMap;
import java.util.Map;

public enum GameStatus {

	PENDING(1),
	WON(2),
	LOST(3);

	private final int value;
	private static Map<Integer, GameStatus> map = new HashMap<>();

	GameStatus(int value) {
		this.value = value;
	}

	static {
		for (GameStatus pageType : GameStatus.values()) {
			map.put(pageType.value, pageType);
		}
	}

	public static GameStatus valueOf(int pageType) {
		return map.get(pageType);
	}

	public int getValue() {
		return value;
	}
}
