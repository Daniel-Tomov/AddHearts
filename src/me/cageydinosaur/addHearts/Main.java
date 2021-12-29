package me.cageydinosaur.addHearts;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{

	ArrayList<String> itemLore = new ArrayList<String>();
	public void onEnable() {

		itemLore.add("Consume this to gain an extra heart");
		this.saveDefaultConfig();
		this.getServer().getPluginManager().registerEvents(new Events(this), this);
		getCommand("heart").setExecutor(new CMD(this));
		getCommand("heart").setTabCompleter(new TabCompletion(this));
		Bukkit.addRecipe(getRecipe());
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
	
	public void remRecipe() {
		NamespacedKey key = new NamespacedKey(this, "heart");
		Bukkit.removeRecipe(key);
	}
	
	public ShapedRecipe getRecipe() {
		ItemStack item  = new ItemStack(Material.DRIED_KELP);
		NamespacedKey key = new NamespacedKey(this, "heart");
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Heart");
		meta.setLore(itemLore);
		meta.setCustomModelData(6789);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		item.setItemMeta(meta);
		
		ShapedRecipe recipe = new ShapedRecipe(key, item);
		
		
		recipe.shape("DND", "GTG", "DED");
		recipe.setIngredient('D', Material.DIAMOND_BLOCK);
		recipe.setIngredient('G', Material.GOLD_BLOCK);
		recipe.setIngredient('N', Material.NETHERITE_BLOCK);
		recipe.setIngredient('T', Material.TOTEM_OF_UNDYING);
		recipe.setIngredient('E', Material.ELYTRA);
 		
		return recipe;
	}
}
