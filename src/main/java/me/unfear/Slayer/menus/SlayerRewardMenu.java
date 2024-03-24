package me.unfear.Slayer.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.unfear.Slayer.Language;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlayerRewardMenu {
    private static final Language lang = Main.inst.getLanguage();

    public static ChestGui create(PlayerData data) {
        final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta backgroundMeta = background.getItemMeta();
        if (backgroundMeta != null) {
            backgroundMeta.setDisplayName(ChatColor.RED + " ");
            background.setItemMeta(backgroundMeta);
        }

        final ItemStack reward = new ItemStack(Material.GOLD_BLOCK);
        final ItemMeta rewardMeta = background.getItemMeta();
        rewardMeta.setDisplayName(lang.rewardGuiRewardName());
        rewardMeta.setLore(lang.rewardGuiRewardLore(data.getReward()));
        reward.setItemMeta(rewardMeta);

        final ChestGui gui = new ChestGui(3, lang.rewardGuiTitle());

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        final OutlinePane backgroundPane = new OutlinePane(0, 0, 9, 3, Priority.LOWEST);
        backgroundPane.addItem(new GuiItem(background));
        backgroundPane.setRepeat(true);
        gui.addPane(backgroundPane);

        // Collect rewards button
        final StaticPane rewardPane = new StaticPane(4, 1, 1, 1);
        rewardPane.addItem(new GuiItem(reward, event -> {
            event.getWhoClicked().closeInventory();
            event.getWhoClicked().sendMessage(Main.inst.getLanguage().rewardClaimed());

            data.setTasksCompleted(data.getTasksCompleted() + 1);
            data.setPoints(data.getPoints() + data.getReward());
            data.setCurrentTask(null);
        }), 0, 0);
        gui.addPane(rewardPane);

        return gui;
    }
}
