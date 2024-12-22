package me.cageydinosaur.addHearts;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	Logger logger;
	private final String TOP_KEY_IN_CONFIG = "heart";
	NamespacedKey key;

	public void onEnable() {
		this.logger = Bukkit.getLogger();
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		getCommand("heart").setExecutor(new CMD(this));
		getCommand("heart").setTabCompleter(new TabCompletion(this));
		this.key = new NamespacedKey(this, "heart");
		this.reloadRecipe();
	}

	public void onDisable() {
		this.remRecipe();
	}

	String translateColorCode(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	void reloadTheConfig() {
		this.reloadConfig();
		this.reloadRecipe();
	}

	void reloadRecipe() {
		this.remRecipe();
		this.getServer().addRecipe(this.getRecipe());
	}

	void remRecipe() {
		this.getServer().removeRecipe(this.key);
	}

	private ShapedRecipe getRecipe() {
		String displayName = this.getConfig().getString(TOP_KEY_IN_CONFIG + ".displayName");
		List<String> lore = this.getConfig().getStringList(TOP_KEY_IN_CONFIG + ".lore");
		int modelDataId = this.getConfig().getInt(TOP_KEY_IN_CONFIG + ".modelData");
		List<String> hide = this.getConfig().getStringList(TOP_KEY_IN_CONFIG + ".hide");
		List<String> enchantments = this.getConfig().getStringList(TOP_KEY_IN_CONFIG + ".enchantments");
		List<String> itemsInRecipe = this.getConfig().getStringList(TOP_KEY_IN_CONFIG + ".recipe");
		if (itemsInRecipe.size() != 9) {
			throw new RuntimeException("Wrong number of items in recipe.");
		}
		ItemStack item = new ItemStack(
				Material.getMaterial(this.getConfig().getString(this.TOP_KEY_IN_CONFIG + ".item")));

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.translateColorCode(displayName));

		meta.setLore(lore.stream().map(x -> this.translateColorCode(x)).collect(Collectors.toList()));
		meta.setCustomModelData(modelDataId);

		for (String h : hide) {
			meta.addItemFlags(ItemFlag.valueOf(h));
		}

		for (String ench : enchantments) {
			try {
				meta.addEnchant(EnchantmentWrapper.getByKey(NamespacedKey.minecraft(ench.split(",")[0])),
						Integer.parseInt(ench.split(",")[1]), true);
			} catch (NumberFormatException e) {
				this.logger.severe(ench + " is not a number for an enchantment. Try uppercase or with underscores.");
				throw e;
			} catch (PatternSyntaxException e) {
				this.logger.severe(ench + " is not formatted properly. Format is 'enchantment,strength'.");
				throw e;
			} catch (IllegalArgumentException e) {
				this.logger.severe(ench + " caused an error:");
				throw e;
			}
		}
		item.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(this.key, item);

		recipe.shape("ABC", "DEF", "GHI");
		for (int i = 0; i < itemsInRecipe.size(); i++) {
			char charVal = (char) (i + 65);
			String materialName = itemsInRecipe.get(i);
			boolean isAir = "".equals(materialName) || materialName == null;
			try {
				recipe.setIngredient(charVal, isAir ? Material.AIR : Material.getMaterial(materialName));
			} catch (IllegalArgumentException e) {
				this.logger.severe(materialName + " is an invalid material name. Try uppercase or with underscores.");
				throw e;
			}
		}

		return recipe;
	}
}
