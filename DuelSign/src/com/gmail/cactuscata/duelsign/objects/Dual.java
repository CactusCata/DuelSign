package com.gmail.cactuscata.duelsign.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.duelsign.DuelSign;
import com.gmail.cactuscata.duelsign.enums.PrefixMessage;
import com.gmail.cactuscata.duelsign.listeners.LeaveGame;

public class Dual {

	private static boolean isInFight;
	private static boolean wait;
	private static SignObject signObject;
	private static DuelSign plugin;

	public Dual(SignObject signObject, DuelSign plugin) {
		wait = false;
		isInFight = false;
		Dual.signObject = signObject;
		Dual.plugin = plugin;
	}

	public static boolean OnFight() {
		return isInFight;
	}

	public static boolean onWait() {
		return wait;
	}

	public static void launchFight() {
		isInFight = true;
		launchWait();
	}

	public static void resetAll() {
		int i = 0;
		for (Player players : signObject.getUUIDS()) {
			if (players != null && players.isOnline()) {
				teleportToSpawn(players);
			}
			signObject.removePlayer(players, i);
			i++;
		}
		isInFight = false;
		Bukkit.broadcastMessage(PrefixMessage.PREFIX + "L'arène est de nouveau ouverte !");
		LeaveGame.resetDeconnection();
	}

	public static void resetForOne(Player playerWinner, Player playerQuiter) {

		int i = signObject.getPlacement(playerWinner);
		Bukkit.broadcastMessage(PrefixMessage.PREFIX + "L'arene a été reset !");
		teleportToSpawn(playerWinner);
		signObject.removePlayer(playerWinner, i);
		signObject.removePlayer(playerQuiter, signObject.getPlacement(playerQuiter));
		isInFight = false;

	}

	public static void resetALLALL() {
		int i = 0;
		LeaveGame.resetDeconnection();
		for (Player player : signObject.getUUIDS()) {
			signObject.removePlayer(player, i);
			i++;
		}
		isInFight = false;
		Bukkit.broadcastMessage(PrefixMessage.PREFIX + "L'arene a été reset !");
		LeaveGame.resetDeconnection();
	}

	public static void teleportToSpawn(Player player) {
		player.teleport(SignObject.getSpawnLocation());
	}

	public static void resetWait(Player player) {

		signObject.removePlayer(player, signObject.getPlacement(player));

	}

	private static void launchWait() {
		wait = true;

		for (Player player : signObject.getUUIDS())
			player.sendMessage(PrefixMessage.PREFIX_WARN + "Attention le combat va commencer dans 3 secondes !");

		new BukkitRunnable() {

			int i = 0;
			private final Player player1 = signObject.getPlayer(0), player2 = signObject.getPlayer(1);

			public void run() {

				if (player1 == null || !player1.isOnline() || player2 == null || !player2.isOnline())
					this.cancel();

				if (i > 3) {
					int place = 1;
					for (Player player : signObject.getUUIDS()) {

						player.teleport(SignObject.getPlayerDualLocation(place));
						player.sendMessage(PrefixMessage.PREFIX + "Vous êtes en combat contre "
								+ signObject.getOtherPlayer(player).getName() + " !");
						place++;

					}

					wait = false;
					this.cancel();
				}
				i++;
			}
		}.runTaskTimer(plugin, 0L, 20L);

	}

}
