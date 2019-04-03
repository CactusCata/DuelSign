package com.gmail.cactuscata.duelsign.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.duelsign.DuelSign;
import com.gmail.cactuscata.duelsign.enums.PrefixMessage;
import com.gmail.cactuscata.duelsign.objects.Dual;
import com.gmail.cactuscata.duelsign.objects.SignObject;

public class LeaveGame implements Listener {

	private final SignObject signObject;
	private final DuelSign plugin;
	private static byte deconnection;

	public LeaveGame(SignObject signObject, DuelSign plugin) {
		this.signObject = signObject;
		this.plugin = plugin;
		deconnection = 0;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {

		Player player = event.getPlayer();

		if (!signObject.contain(player))
			return;

		if (Dual.onWait()) {
			signObject.getOtherPlayer(player).sendMessage(PrefixMessage.PREFIX_WARN + "Pendant le temps d'attente, le joueur " + player.getName() + " c'est déconnecté !");
			Dual.resetWait(player);
			return;
		}

		if (Dual.OnFight()) {
			if (deconnection == 0) {
				signObject.getOtherPlayer(player).sendMessage(PrefixMessage.PREFIX_WARN + "Pendant la phase pvp, le joueur " + player.getName() + " c'est déconnecté !\nVous gagnez donc ce combat, vous avez 10 secondes pour récuperer le stuff du joueur !");
				Bukkit.broadcastMessage("§e" + signObject.getOtherPlayer(player).getName() + " §ea gagné le tournois contre " + player.getName() + " !");
				deconnection++;
				player.setHealth(0.0d);
				letsTime(signObject.getOtherPlayer(player), player);
				
			} else {

				Dual.resetALLALL();
				Dual.teleportToSpawn(player);

			}
		}

	}

	private void letsTime(Player uuidWinner, Player uuidQuiter) {

		new BukkitRunnable() {

			int i = 0;

			public void run() {

				if (uuidWinner == null || !uuidWinner.isOnline())
					this.cancel();

				if (i > 10) {
					Dual.resetForOne(uuidWinner, uuidQuiter);
					resetDeconnection();
					this.cancel();
				}
				i++;
			}
		}.runTaskTimer(plugin, 0L, 20L);

	}

	public static void resetDeconnection() {
		LeaveGame.deconnection = 0;
	}

}
