package com.weather.model;

public enum Source {

	SOURCE1(1), SOURCE2(2), SOURCE3(3);

	private int value;

	Source(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Source parse(int id) {
		Source right = null; // Default
		for (Source item : Source.values()) {
			if (item.getValue() == id) {
				right = item;
				break;
			}
		}
		return right;
	}

}
