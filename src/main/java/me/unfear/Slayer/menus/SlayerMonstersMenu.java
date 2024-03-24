package me.unfear.Slayer.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.unfear.Slayer.Language;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.mobtypes.MobType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class SlayerMonstersMenu {
    private static final Language lang = Main.inst.getLanguage();

    private static ArrayList<ItemStack> getMonsterItems(PlayerData data) {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (Entry<Integer, Integer> entry : data.getEntityKills().entrySet()) {
            final MobType mobType = Main.inst.getMobTypeLoader().getMobType(entry.getKey());
            final ItemStack item = new ItemStack(mobType.getMaterial());
            final ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.setDisplayName(lang.monsterGuiMonsterName(mobType.getName()));
                meta.setLore(List.of(lang.monsterGuiMonsterLore(entry.getValue())));
                item.setItemMeta(meta);
            }
            items.add(item);
        }
        return items;
    }

    public static ChestGui create(Player player, PlayerData data, int page) {
        final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta backgroundMeta = background.getItemMeta();
        if (backgroundMeta != null) {
            backgroundMeta.setDisplayName(ChatColor.RED + " ");
            background.setItemMeta(backgroundMeta);
        }

        final ItemStack prevArrow = new ItemStack(Material.ARROW);
        final ItemMeta prevArrowMeta = prevArrow.getItemMeta();
        if (prevArrowMeta != null) {
            prevArrowMeta.setDisplayName(lang.monstersGuiPrevPageName());
            prevArrow.setItemMeta(prevArrowMeta);
        }

        final ItemStack nextArrow = new ItemStack(Material.ARROW);
        final ItemMeta nextArrowMeta = nextArrow.getItemMeta();
        if (nextArrowMeta != null) {
            nextArrowMeta.setDisplayName(lang.monstersGuiNextPageName());
            nextArrow.setItemMeta(nextArrowMeta);
        }

        final ItemStack backButton = new ItemStack(Material.ARROW);
        final ItemMeta slayerMasterMeta = backButton.getItemMeta();
        if (slayerMasterMeta != null) {
            slayerMasterMeta.setDisplayName(lang.monstersGuiBackName());
            slayerMasterMeta.setLore(lang.monstersGuiBackLore());
            backButton.setItemMeta(slayerMasterMeta);
        }

        ChestGui gui = new ChestGui(6, lang.monsterGuiTitle());

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
                    create(player, data, page - 1).show(event.getWhoClicked());
                }
            }), 0, 0);
        }

        if (pages.getPage() < pages.getPages() - 1) {
            navigation.addItem(new GuiItem(new ItemStack(nextArrow), event -> {
                if (pages.getPage() < pages.getPages() - 1) {
                    create(player, data, page + 1).show(event.getWhoClicked());
                }
            }), 8, 0);
        }

        String backButtonCommand = Main.inst.getSlayerLoader().getMonstersBackCommand(player.getName());
        if (!backButtonCommand.equalsIgnoreCase("none")) {
            navigation.addItem(
                    new GuiItem(backButton, event -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), backButtonCommand)), 0, 2);
        }

        gui.addPane(navigation);

        return gui;
    }
}
