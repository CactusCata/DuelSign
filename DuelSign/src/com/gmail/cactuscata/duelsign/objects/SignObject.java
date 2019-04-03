package com.gmail.cactuscata.duelsign.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.duelsign.DuelSign;
import com.gmail.cactuscata.duelsign.enums.PrefixMessage;
import com.gmail.cactuscata.duelsign.listeners.ClickSignEvent;

public class SignObject {

	private final Player[] playersUUID;
	private final DuelSign plugin;

	public SignObject(DuelSign plugin) {
		playersUUID = new Player[2];
		this.plugin = plugin;
	}

	public void addPlayer(Player player, int i, Sign sign) {
		this.playersUUID[i] = player;
		signOccupedUpdate(player, i + 1, sign);
		activeTimeOut(player, i);
		if (this.playersUUID[0] != null && this.playersUUID[1] != null)
			Dual.launchFight();

	}

	public void removePlayer(Player player, int i) {
		playersUUID[i] = null;
		signOpeningUpdate(i + 1);
	}

	public Player[] getUUIDS() {
		return this.playersUUID;
	}

	public boolean contain(Player player) {
		return this.playersUUID[0] == player || this.playersUUID[1] == player;
	}

	private void activeTimeOut(Player player, int bound) {
		new BukkitRunnable() {

			int i = 0;

			public void run() {
				if (Dual.OnFight())
					this.cancel();

				if (player == null || !player.isOnline()) {
					removePlayer(player, bound);
					this.cancel();
				}

				if (i > 5) {
					player.sendMessage(PrefixMessage.PREFIX_WARN
							+ "Le delais de 5 secondes d'attente a été dépassé, merci de se ré-inscrire !");
					removePlayer(player, bound);
					this.cancel();
				}
				i++;
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}

	public Player getOtherPlayer(Player player) {
		if (this.playersUUID[0] == player)
			return this.playersUUID[1];
		return this.playersUUID[0];
	}

	public int getPlacement(Player player) {
		if (this.playersUUID[0] == player)
			return 0;
		return 1;
	}

	public Player getPlayer(int placement) {
		return this.playersUUID[placement];
	}

	private void signOccupedUpdate(Player player, int value, Sign sign) {
		sign.setLine(2, player.getName());
		sign.update();

	}

	private void signOpeningUpdate(int value) {
		Sign sign = ClickSignEvent.getSigns()[value - 1];
		sign.setLine(2, "[Disponible]");
		sign.update();

	}

	public static Location getSignLocation(int i) {
		if (i == 1)
			return new Location(Bukkit.getWorld("Faction"), 2.0d, 54.0d, 3.0d);
		return new Location(Bukkit.getWorld("Faction"), 2.0d, 54.0d, 7.0d);
	}

	public static Location getSpawnLocation() {
		return new Location(Bukkit.getWorld("Faction"), 0.0d, 52.1d, 5.0d, -90.0f, 0.0f);
	}

	public static Location getPlayerDualLocation(int i) {
		if (i == 1)
			return new Location(Bukkit.getWorld("Faction"), -2.0d, 47.1d, 5.0d, -90.0f, 0.0f);
		return new Location(Bukkit.getWorld("Faction"), 15.0d, 47.1d, 5.0d, 90.0f, 0.0f);
	}

}
