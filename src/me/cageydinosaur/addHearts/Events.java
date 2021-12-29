package me.cageydinosaur.addHearts;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class Events implements Listener {
	Main plugin;

	public Events(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		Player eater = e.getPlayer();
		if (eater.hasPermission("heart.use")) {
			if (e.getItem().getItemMeta().hasCustomModelData()) {
				if (e.getItem().getItemMeta().getCustomModelData() == 6789) {
					eater.setMaxHealth(eater.getMaxHealth() + 2);
				}
			} else {
				eater.sendMessage(ChatColor.RED + "You do not have permission to eat hearts");
			}
		}
	}
}
