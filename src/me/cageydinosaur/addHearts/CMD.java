package me.cageydinosaur.addHearts;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMD implements CommandExecutor {
	Main plugin;

	public CMD(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// check if sender has permission to send command
		if (!sender.hasPermission("heart.add") && !sender.hasPermission("heart.remove")
				&& !sender.hasPermission("heart.use") && !sender.hasPermission("heart.reload")) {
			sender.sendMessage(ChatColor.RED + "You do not have permissions to use the commands for this plugin!");
			return true;
		}

		// sender has permission
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GREEN + "Usage:");
			if (sender.hasPermission("heart.add"))
				sender.sendMessage(ChatColor.GREEN + "/heart add <player> - add number of hearts a player has by 1");
			if (sender.hasPermission("heart.remove"))
				sender.sendMessage(
						ChatColor.GREEN + "/heart remove <player> - remove number of hearts a player has by 1");
			/*
			 * sender.sendMessage( ChatColor.GREEN +
			 * "Diamond Blocks in four corners. Netherite Block top middle. Elytra bottom middle. Gold Blocks middle left and right. Totem of Undying middle."
			 * );
			 */
			if (!sender.hasPermission("heart.use")) {
				sender.sendMessage(ChatColor.RED + "You can not use hearts");
				return true;
			}
			sender.sendMessage(ChatColor.GOLD + "===========================");
			sender.sendMessage(ChatColor.GOLD + "Heart Recipe (* is empty):");
			List<String> recipe = this.plugin.getConfig().getStringList("heart.recipe");
			int lineCount = 0;
			StringBuffer sb = new StringBuffer();
			for (String recipeChild : recipe) {
				boolean isAir = "".equals(recipeChild) || recipeChild == null;
				if (isAir) {
					recipeChild = "*";
				}
				sb.append(recipeChild).append("     ");
				lineCount++;

				if (lineCount % 3 == 0) {
					sender.sendMessage(ChatColor.BLACK + sb.toString());
					sb = new StringBuffer();
				}
			}
			sender.sendMessage(ChatColor.GOLD + "===========================");
			return true;
		}

		// sub commands
		if (args[0].equalsIgnoreCase("reload")) {
			if (!sender.hasPermission("heart.reload")) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
				return true;
			}
			this.plugin.reloadTheConfig();
			sender.sendMessage("Reloaded the config");
			return true;

		} else if (args[0].equalsIgnoreCase("add")) {
			addHearts(sender, args, 2);
			return true;
		} else if (args[0].equalsIgnoreCase("remove")) {
			addHearts(sender, args, -2);
			return true;
		} else if (args.length > 2) {
			sender.sendMessage(ChatColor.RED + "Invalid number of arguments");
			return true;
		}
		sender.sendMessage(ChatColor.RED + "Invalid command");

		return true;

	}

	@SuppressWarnings("deprecation")
	private void addHearts(CommandSender sender, String[] args, int amount) {
		if (!(sender.hasPermission("heart.remove"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
			return;
		}
		if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "You must specify a player name");
			return;
		} else if (args.length == 2) {
			Player recievingPlayer = Bukkit.getPlayer(args[1]);
			if (recievingPlayer == null) {
				sender.sendMessage(ChatColor.RED + "That player is not online");
				return;
			}
			if (recievingPlayer.getMaxHealth() == 2) {
				sender.sendMessage(
						ChatColor.RED + args[1] + ChatColor.GREEN + " already has the minimum amount of hearts");
				return;
			}
			recievingPlayer.setMaxHealth(recievingPlayer.getMaxHealth() + amount);
			sender.sendMessage(
					ChatColor.RED + recievingPlayer.getDisplayName() + ChatColor.GREEN + " now has " + ChatColor.RED
							+ Double.toString(recievingPlayer.getMaxHealth() / 2) + ChatColor.GREEN + " hearts.");
			return;
		}
	}

}
