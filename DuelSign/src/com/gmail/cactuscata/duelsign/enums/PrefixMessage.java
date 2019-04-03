package com.gmail.cactuscata.duelsign.enums;

public enum PrefixMessage {

	PREFIX("§6§l[§9§lDuel§l§6] §e"),
	PREFIX_WARN("§6§l[§9§lDuel§l§6] §6"),
	PREFIX_FATALITY("§6§l[§9§lDuel§l§6] §c");

	private final String message;

	private PrefixMessage(final String message) {
		this.message = message;
	}

	@Override
	public final String toString() {
		return this.message;
	}

}
