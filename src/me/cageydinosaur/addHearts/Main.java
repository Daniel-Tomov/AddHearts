package me.cageydinosaur.addHearts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{

	public static YamlConfiguration CONFIG;
	public static Logger logger;
	public static final String TOP_KEY_IN_CONFIG = "heart";

	public void onEnable() {
		logger = getLogger();
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		// Using in reload command
		this.loadConfig();
		getCommand("heart").setExecutor(new CMD(this));
		getCommand("heart").setTabCompleter(new TabCompletion(this));
		this.reloadRecipe();

	}

	public void reloadRecipe() {
		ConfigurationSection heart = CONFIG.getConfigurationSection("heart");
		Set<String> customKeys = heart.getKeys(false);
		if(customKeys == null && customKeys.isEmpty()){
			logger.info("Not custom key exists");
			return;
		}
		for (String customKey : customKeys) {
			this.remRecipe(customKey);
			this.getRecipe(customKey);
		}
	}

	public void loadConfig(){
		YamlConfiguration config = new YamlConfiguration();
		String configPath = this.getDataFolder() + File.separator + "config.yml";
		File configFile = new File(configPath);
		if (!configFile.exists()) {
            this.saveResource("config.yml", false);
            try {
                config.load(Objects.requireNonNull(this.getTextResource("config.yml")));
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
        CONFIG = config;
	}
	
	public void onDisable() {
		remRecipe();
	}

	public String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public void reloadTheConfig() {
		this.reloadConfig();
	}

	public void remRecipe(String key) {
		Bukkit.removeRecipe(new NamespacedKey(this, key));
	}
	
	public void remRecipe() {
		NamespacedKey key = new NamespacedKey(this, "heart");
		Bukkit.removeRecipe(key);
	}

	@SuppressWarnings("all")
	public ShapedRecipe getRecipe(String path) {
		String displayName = CONFIG.getString(TOP_KEY_IN_CONFIG + path + "displayName");
		List<String> lore = CONFIG.getStringList(TOP_KEY_IN_CONFIG + path + "lore");
		int modelDataId = CONFIG.getInt(TOP_KEY_IN_CONFIG + path + "modelData");
		String hide = CONFIG.getString(TOP_KEY_IN_CONFIG + path + "hide");
		List<String> recipes = CONFIG.getStringList(TOP_KEY_IN_CONFIG + path + "recipe");
		if(recipes.size() != 9){
			throw new RuntimeException("Wrong custom item recipe.");
		}
		ItemStack item = new ItemStack(Material.SUSPICIOUS_STEW);
		NamespacedKey key = new NamespacedKey(this, path);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + displayName);
		meta.setLore(lore);
		meta.setCustomModelData(modelDataId);
		meta.addItemFlags(ItemFlag.valueOf(hide));
		// TODO Maybe doing config.yml
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		item.setItemMeta(meta);

		ShapedRecipe recipe = new ShapedRecipe(key, item);

		recipe.shape("ABC", "DEF", "GHI");
		for (int i = 65; i < recipes.size(); i++) {
			char charVal = (char) i;
			String materialName = recipes.get(i);
			boolean isAir = "".equals(materialName) || materialName == null;
			recipe.setIngredient(charVal, isAir ? Material.AIR : Material.getMaterial(materialName));
		}

		return recipe;
	}


}
