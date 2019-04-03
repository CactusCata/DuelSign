package com.gmail.cactuscata.duelsign.listeners;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gmail.cactuscata.duelsign.enums.PrefixMessage;
import com.gmail.cactuscata.duelsign.objects.SignObject;

public class ClickSignEvent implements Listener {

	private final SignObject signObj;
	private static final Sign[] sign = new Sign[2];

	public ClickSignEvent(SignObject signObj) {
		this.signObj = signObj;
	}

	@EventHandler
	public void onRightClickSign(PlayerInteractEvent event) {

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (event.getClickedBlock().getType() != Material.SIGN
				&& event.getClickedBlock().getType() != Material.WALL_SIGN
				&& event.getClickedBlock().getType() != Material.SIGN_POST)
			return;

		Sign sign = (Sign) event.getClickedBlock().getState();

		if (!sign.getLine(0).equals("Duel 1v1"))
			return;

		Player player = event.getPlayer();

		for (int i = 1; i < 3; i++) {

			if (sign.getLine(1).equals("Joueur " + i + " :")) {
				if (!(SignObject.getSignLocation(i).equals(sign.getLocation()))) {
					break;

				}

				if (!sign.getLine(2).equals("[Disponible]")) {
					player.sendMessage(
							PrefixMessage.PREFIX_FATALITY + "Il y a déjà quelqu'un d'inscrit dans l'arène !");
					return;
				}

				if (signObj.contain(player)) {
					player.sendMessage(PrefixMessage.PREFIX_FATALITY + "Vous avez déjà été inscrit !");
					return;
				}

				player.sendMessage(PrefixMessage.PREFIX + "Vous avez rejoins la zone d'attente !");
				ClickSignEvent.sign[i - 1] = sign;
				signObj.addPlayer(player, i - 1, sign);
				return;
			}

		}

	}

	public static Sign[] getSigns() {
		return sign;
	}

}
