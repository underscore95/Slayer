package me.unfear.Slayer;

import de.tr7zw.changeme.nbtapi.NBTItem;
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
        this.name = Chat.format(name);
        this.description = new ArrayList<>(description.size());
        for (String line : description) {
            this.description.add(Chat.format(line));
        }
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
            meta.setDisplayName(name);
            ArrayList<String> lore = new ArrayList<>();
            lore.addAll(description);

            lore.add("");
            lore.add(Main.inst.getLanguage().shopCost(cost));
            lore.add(Main.inst.getLanguage().clickToPurchase());
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
