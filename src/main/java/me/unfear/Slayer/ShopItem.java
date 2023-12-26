package me.unfear.Slayer;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ShopItem implements Comparable<ShopItem> {

    public static final String NBT_TAG = "slayer.shop-item-id";

    private final int id;
    private final String name;
    private final ArrayList<String> description;
    private final int cost;
    private final ArrayList<String> commands;
    private final Material material;
    private final int itemAmount;
    private final int purchases;

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
        if (meta != null) {
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
        }

        NBTItem nbt = new NBTItem(item);
        nbt.setInteger(NBT_TAG, id);
        nbt.applyNBT(item);

        return item;
    }

    @Override
    public int compareTo(ShopItem o) {
        return this.id - o.id;
    }
}
