package me.unfear.Slayer.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.ShopItem;
import me.unfear.Slayer.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SlayerShopMenu {

    private static ArrayList<ItemStack> getShopItems(PlayerData data) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (ShopItem shopItem : Main.inst.getSlayerLoader().getShopItems()) {
            if (!data.getShopItemsPurchased().containsKey(shopItem.getId()))
                data.getShopItemsPurchased().put(shopItem.getId(), 0);

            // can player buy more of this item? if no then don't show it
            if (data.getShopItemsPurchased().get(shopItem.getId()) < shopItem.getPurchases()
                    || shopItem.getPurchases() == -1) {
                ItemStack item = shopItem.createItem();
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
                    if (shopItem.getCost() > data.getPoints()) {
                        lore.remove(lore.size() - 1);
                        lore.add(Main.inst.getLanguage().tooExpensive());
                    }
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                items.add(item);
            }
        }

        return items;
    }

    public static ChestGui create(PlayerData data, int page) {
        final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta backgroundMeta = background.getItemMeta();
        if (backgroundMeta != null) {
            backgroundMeta.setDisplayName(ChatColor.RED + " ");
            background.setItemMeta(backgroundMeta);
        }

        final ItemStack prevArrow = new ItemStack(Material.ARROW);
        final ItemMeta prevArrowMeta = prevArrow.getItemMeta();
        if (prevArrowMeta != null) {
            prevArrowMeta.setDisplayName(Main.inst.getLanguage().previousPage());
            prevArrow.setItemMeta(prevArrowMeta);
        }

        final ItemStack nextArrow = new ItemStack(Material.ARROW);
        final ItemMeta nextArrowMeta = nextArrow.getItemMeta();
        if (nextArrowMeta != null) {
            nextArrowMeta.setDisplayName(Main.inst.getLanguage().nextPage());
            nextArrow.setItemMeta(nextArrowMeta);
        }

        // points
        final ItemStack points = new ItemStack(Material.GOLD_NUGGET);
        final ItemMeta pointsMeta = points.getItemMeta();
        if (pointsMeta != null) {
            pointsMeta.setDisplayName(Main.inst.getLanguage().slayerPoints(data.getPoints()));
            points.setItemMeta(pointsMeta);
        }

        ChestGui gui = new ChestGui(6, Main.inst.getLanguage().shopGuiTitle());

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
        pages.populateWithItemStacks(getShopItems(data));
        pages.setOnClick(event -> {
            if (event.getCurrentItem() == null) return;

            // purchase item
            final NBTItem nbt = new NBTItem(event.getCurrentItem());
            if (!nbt.hasTag(ShopItem.NBT_TAG))
                return;
            int id = nbt.getInteger(ShopItem.NBT_TAG);
            ShopItem shopItem = Main.inst.getSlayerLoader().getShopItem(id);
            if (shopItem == null)
                return;
            // can player buy more of this item? shouldn't be shown, but just in case
            if (data.getShopItemsPurchased().get(shopItem.getId()) >= shopItem.getPurchases()
                    && shopItem.getPurchases() != -1)
                return;
            // can player afford this item?
            if (data.getPoints() < shopItem.getCost()) return;

            // player is able to buy it, so buy it
            data.setPoints(data.getPoints() - shopItem.getCost());
            data.getShopItemsPurchased().put(shopItem.getId(), data.getShopItemsPurchased().get(shopItem.getId()) + 1);
            for (String command : shopItem.getCommands()) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", event.getWhoClicked().getName()));
            }

            create(data, page).show(event.getWhoClicked());
            event.getWhoClicked().sendMessage(Main.inst.getLanguage().transactionComplete(shopItem.getCost()));

        });

        gui.addPane(pages);

        OutlinePane backgroundPane = new OutlinePane(0, 5, 9, 1);
        backgroundPane.addItem(new GuiItem(background));
        backgroundPane.setRepeat(true);
        backgroundPane.setPriority(Pane.Priority.LOWEST);

        gui.addPane(backgroundPane);

        pages.setPage(page);
        gui.update();

        StaticPane navigation = new StaticPane(0, 5, 9, 1);
        if (page > 0) {
            navigation.addItem(new GuiItem(prevArrow, event -> {
                if (pages.getPage() > 0) {
                    create(data, page - 1).show(event.getWhoClicked());
                }
            }), 0, 0);
        }

        if (pages.getPage() < pages.getPages() - 1) {
            navigation.addItem(new GuiItem(new ItemStack(nextArrow), event -> {
                if (pages.getPage() < pages.getPages() - 1) {
                    create(data, page + 1).show(event.getWhoClicked());
                }
            }), 8, 0);
        }

        navigation.addItem(
                new GuiItem(points), 4, 0);

        gui.addPane(navigation);

        return gui;
    }
}
