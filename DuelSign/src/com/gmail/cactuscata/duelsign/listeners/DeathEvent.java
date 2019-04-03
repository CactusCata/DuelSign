package com.gmail.cactuscata.duelsign.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.cactuscata.duelsign.DuelSign;
import com.gmail.cactuscata.duelsign.enums.PrefixMessage;
import com.gmail.cactuscata.duelsign.objects.Dual;
import com.gmail.cactuscata.duelsign.objects.SignObject;

public class DeathEvent implements Listener {

	private final DuelSign plugin;
	private final SignObject signObject;

	public DeathEvent(SignObject signObject, DuelSign plugin) {
		this.signObject = signObject;
		this.plugin = plugin;
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {

		Player player = event.getEntity();

		if (!signObject.contain(player))
			return;
		event.setDeathMessage(PrefixMessage.PREFIX + "Le joueur " + player.getName() + " s'est fait tuer par "
				+ signObject.getOtherPlayer(player).getName() + " !\nL'arène ré-ouvrira dans 10 secondes !");

		signObject.getOtherPlayer(player)
				.sendMessage(PrefixMessage.PREFIX_WARN + "Vous avez 10 secondes pour récuperer le stuff du joueur !");
		letsTime(player);

	}

	private void letsTime(Player player) {

		new BukkitRunnable() {

			int i = 0;

			public void run() {

				if (player == null || !player.isOnline())
					this.cancel();

				if (i > 10) {
					Dual.resetAll();
					this.cancel();
				}
				i++;
			}
		}.runTaskTimer(plugin, 0L, 20L);

	}

}
