package me.cageydinosaur.addHearts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
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
		if (e.getItem().getItemMeta().hasCustomModelData()) {
			if (e.getItem().getItemMeta().getCustomModelData() == 6789) {
				if (eater.hasPermission("heart.use")) {
					e.setCancelled(true);
					double maxHealth = eater.getMaxHealth();
					int maxAllowedHearts = getMaxAllowedHearts(eater);

					if (maxHealth / 2 + 1 > maxAllowedHearts) {
						eater.sendMessage(this.plugin.translateColorCode(
								this.plugin.getConfig().getString("heart.GreaterThanMaxHeartsMessage").replace("<num>", String.valueOf(maxAllowedHearts))));
						return;
					}
					eater.setItemInHand(null);
					eater.setMaxHealth(maxHealth + 2);
				} else {
					eater.sendMessage(ChatColor.RED + "You do not have permission to eat hearts");
					e.setCancelled(true);
				}
			}
		}
	}

	public int getMaxAllowedHearts(final Player player) {
		final Pattern maxHeartPerm = Pattern.compile("heart\\.(\\d+)");

		return player.getEffectivePermissions().stream().map(i -> {
			Matcher matcher = maxHeartPerm.matcher(i.getPermission());
			if (matcher.matches()) {
				return Integer.parseInt(matcher.group(1));
			}
			return 0;
		}).max(Integer::compareTo).orElse(0);
	}
}