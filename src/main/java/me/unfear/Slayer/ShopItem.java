package me.unfear.Slayer;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.changeme.nbtapi.NBTItem;

public class ShopItem implements Comparable<ShopItem> {
	
	public static final String NBT_TAG = "slayer.shop-item-id";

	private int id;
	private String name;
	private ArrayList<String> description;
	private int cost;
	private ArrayList<String> commands;
	private Material material;
	private int itemAmount;
	private int purchases;
	
	public ShopItem(int id, String name, ArrayList<String> description, int cost, ArrayList<String> commands,
			Material material, int itemAmount, int purchases) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.commands = commands;
		this.material = material;
		this.itemAmount = itemAmount;
		this.purchases = purchases;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getDescription() {
		return description;
	}

	public int getCost() {
		return cost;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public Material getMaterial() {
		return material;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public int getPurchases() {
		return purchases;
	}

	public ItemStack createItem() {
		final ItemStack item = new ItemStack(material, itemAmount);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f" + name));
		ArrayList<String> lore = new ArrayList<>();
		for (String s : description) {
			lore.add(ChatColor.translateAlternateColorCodes('&', "&7" + s));
		}
		
		lore.add("");
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Cost: &5" + cost + " slayer points"));
		lore.add(ChatColor.translateAlternateColorCodes('&', "&7Click to purchase."));
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		NBTItem nbt = new NBTItem(item, true);
		nbt.setInteger(NBT_TAG, id);
		
		return item;
	}

	@Override
	public int compareTo(ShopItem o) {
		return this.id - o.id;
	}
}
