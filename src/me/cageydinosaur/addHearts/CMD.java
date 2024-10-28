package me.cageydinosaur.addHearts;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CMD implements CommandExecutor {
	Main plugin;

	public CMD(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender.hasPermission("heart"))) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
		} else if (sender.hasPermission("heart")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GREEN + "Usage:");
				if (sender.hasPermission("heart.add")) sender.sendMessage(ChatColor.GREEN + "/heart add <player> - add number of hearts a player has by 1");
				if (sender.hasPermission("heart.remove")) sender.sendMessage(ChatColor.GREEN + "/heart remove <player> - remove number of hearts a player has by 1");
				/*sender.sendMessage(
						ChatColor.GREEN + "Diamond Blocks in four corners. Netherite Block top middle. Elytra bottom middle. Gold Blocks middle left and right. Totem of Undying middle.");
				*/
				List<String> recipe = Main.CONFIG.getStringList("heart.heart.recipe");
				int lineCount = 0;
				List<String> recipeDescPerLine = new ArrayList<>();
				StringBuffer sb = new StringBuffer();
				for (String recipeChild : recipe) {
					boolean isAir = "".equals(recipeChild) || recipeChild == null;
					if(isAir){
						recipeChild = "*";
					}
					if(lineCount == 3){
						recipeDescPerLine.add(sb.toString());
						sb = new StringBuffer();
						lineCount = 0;
					}

					sb.append(recipeChild).append("  ");
					lineCount++;
				}
				sender.sendMessage(
						ChatColor.GOLD + "==========================="
				);
				sender.sendMessage(
						ChatColor.GOLD + "Heart Recipe(* is empty):"
				);
				for (String line : recipeDescPerLine) {
					sender.sendMessage(ChatColor.BLACK + line);
				}
				sender.sendMessage(
						ChatColor.GOLD + "==========================="
				);
				} else if (args.length > 0) {
								
				if (args[0].equalsIgnoreCase("reload")) {
					if (!sender.hasPermission("heart.reload")) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					sender.sendMessage("Reloaded the config");
					plugin.getServer().getScheduler().runTaskAsynchronously(plugin,()->{
						plugin.loadConfig();
						plugin.reloadRecipe();
					});
					return true;

				}else if (args[0].equalsIgnoreCase("add")) {
					if (!(sender.hasPermission("heart.add"))) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					if (args.length == 1) {
						sender.sendMessage(ChatColor.RED + "You must specify a player name");
						return true;
					} else if (args.length == 2) {
						Player recievingPlayer = Bukkit.getPlayer(args[1]);
						if (recievingPlayer == null) {
							sender.sendMessage(ChatColor.RED + "That player is not online");
							return true;
						}
						recievingPlayer.setMaxHealth(recievingPlayer.getMaxHealth() + 2);
						sender.sendMessage(ChatColor.RED + recievingPlayer.getDisplayName() + ChatColor.GREEN
								+ " now has " + ChatColor.RED + Double.toString(recievingPlayer.getMaxHealth() / 2)
								+ ChatColor.GREEN + " hearts.");
						return true;
					}

				} else if (args[0].equalsIgnoreCase("remove")) {
					if (!(sender.hasPermission("heart.remove"))) {
						sender.sendMessage(ChatColor.RED + "You do not have permission to use that!");
						return true;
					}
					if (args.length == 1) {
						sender.sendMessage(ChatColor.RED + "You must specify a player name");
						return true;
					} else if (args.length == 2) {
						Player recievingPlayer = Bukkit.getPlayer(args[1]);
						if (recievingPlayer == null) {
							sender.sendMessage(ChatColor.RED + "That player is not online");
							return true;
						}
						if (recievingPlayer.getMaxHealth() == 2) {
							sender.sendMessage(ChatColor.RED + args[1] + ChatColor.GREEN + " already has the minimum amount of hearts");
							return true;
						}
						recievingPlayer.setMaxHealth(recievingPlayer.getMaxHealth() - 2);
						sender.sendMessage(ChatColor.RED + recievingPlayer.getDisplayName() + ChatColor.GREEN
								+ " now has " + ChatColor.RED + Double.toString(recievingPlayer.getMaxHealth() / 2)
								+ ChatColor.GREEN + " hearts.");
						return true;
					}

				}
			}
			return true;
		}
		return true;
	}

}
