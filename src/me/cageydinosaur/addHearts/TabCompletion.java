package me.cageydinosaur.addHearts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompletion implements TabCompleter {

	Main plugin;

	public TabCompletion(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			List<String> commands = new ArrayList<>();
			if (sender.hasPermission("heart.add")) {
				commands.add("add");
			}
			if (sender.hasPermission("heart.remove")) {
				commands.add("remove");
			}
			if (sender.hasPermission("heart.reload")) {
				commands.add("reload");
			}
			if (commands.size() == 0) {
				return null;
			}
			return commands;
		}
		return null;
	}

}
