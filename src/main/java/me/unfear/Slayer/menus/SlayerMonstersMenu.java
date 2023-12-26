package me.unfear.Slayer.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.Slayer;
import me.unfear.Slayer.mobtypes.MobType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SlayerMonstersMenu {

    private static ArrayList<ItemStack> getMonsterItems(PlayerData data) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Entry<Integer, Integer> entry : data.getEntityKills().entrySet()) {
            final MobType mobType = Slayer.inst.getMobTypeLoader().getMobType(entry.getKey());
            final ItemStack item = new ItemStack(mobType.getMaterial());
            final ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f" + mobType.getName()));
                meta.setLore(List.of(ChatColor.GRAY + "Number Defeated: " + ChatColor.RED + entry.getValue()));
                item.setItemMeta(meta);
            }
            items.add(item);
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
            prevArrowMeta.setDisplayName(ChatColor.GRAY + "Previous Page");
            prevArrow.setItemMeta(prevArrowMeta);
        }

        final ItemStack nextArrow = new ItemStack(Material.ARROW);
        final ItemMeta nextArrowMeta = nextArrow.getItemMeta();
        if (nextArrowMeta != null) {
            nextArrowMeta.setDisplayName(ChatColor.GRAY + "Next Page");
            nextArrow.setItemMeta(nextArrowMeta);
        }

        final ItemStack slayerMaster = new ItemStack(Material.PLAYER_HEAD);
        final ItemMeta slayerMasterMeta = slayerMaster.getItemMeta();
        if (slayerMasterMeta != null) {
            slayerMasterMeta.setDisplayName(ChatColor.GRAY + "Back");
            slayerMasterMeta.setLore(List.of(ChatColor.GRAY + "Go back to the " + ChatColor.WHITE + "Slayer Master"));
            slayerMaster.setItemMeta(slayerMasterMeta);
        }

        ChestGui gui = new ChestGui(6, "Slayer Master");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
        pages.populateWithItemStacks(getMonsterItems(data));

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
                new GuiItem(slayerMaster, event -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "slayer " + event.getWhoClicked().getName() + " -s")), 2, 0);

        gui.addPane(navigation);

        return gui;
    }
}
