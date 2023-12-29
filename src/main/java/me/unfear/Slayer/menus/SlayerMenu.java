package me.unfear.Slayer.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlayerMenu {

    public static ChestGui create(PlayerData data) {
        final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta backgroundMeta = background.getItemMeta();
        if (backgroundMeta != null) {
            backgroundMeta.setDisplayName(ChatColor.RED + " ");
            background.setItemMeta(backgroundMeta);
        }

        // display some information
        final ItemStack profile = new ItemStack(Material.BIRCH_SIGN);
        final ItemMeta profileMeta = profile.getItemMeta();
        if (profileMeta != null) {
            profileMeta.setDisplayName(ChatColor.GOLD + "Slayer Profile");
            profileMeta.setLore(
                    Arrays.asList(ChatColor.GRAY + "Tasks Completed: " + ChatColor.DARK_GREEN + data.getTasksCompleted(),
                            ChatColor.GRAY + "Slayer Points: " + ChatColor.DARK_PURPLE + data.getPoints(),
                            ChatColor.translateAlternateColorCodes('&', "&7Current Task: &4"
                                    + (data.getCurrentTask() == null ? "None" : data.getCurrentTask().getName()))));
            profile.setItemMeta(profileMeta);
        }

        // shop
        final ItemStack shop = new ItemStack(Material.GOLD_NUGGET);
        final ItemMeta shopMeta = shop.getItemMeta();
        if (shopMeta != null) {
            shopMeta.setDisplayName(ChatColor.GOLD + "Slayer Shop");
            shopMeta.setLore(
                    List.of(ChatColor.GRAY + "Spend your hard-earned " + ChatColor.DARK_PURPLE + "Slayer Points"));
            shop.setItemMeta(shopMeta);
        }

        // monsters
        final ItemStack monsters = new ItemStack(Material.ZOMBIE_HEAD);
        final ItemMeta monstersMeta = monsters.getItemMeta();
        if (monstersMeta != null) {
            monstersMeta.setDisplayName(ChatColor.RED + "Monsters Slain");
            monstersMeta.setLore(List.of(ChatColor.GRAY + "How many monsters have you ended?"));
            monsters.setItemMeta(monstersMeta);
        }

        // current slayer task
        final ItemStack current = new ItemStack(Material.ROTTEN_FLESH);
        final ItemMeta currentMeta = current.getItemMeta();
        if (currentMeta != null) {
            currentMeta.setDisplayName(ChatColor.YELLOW + "Current Slayer Task");
            if (data.getCurrentTask() != null) {
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.translateAlternateColorCodes('&', "&4" + data.getCurrentTask().getName()));
                for (String line : data.getCurrentTask().getDescription())
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&f" + line));
                lore.add("");
                lore.add(ChatColor.translateAlternateColorCodes('&',
                        "&7Progress: &f" + data.getKills() + " &8/ &f" + data.getCurrentTask().getKills() + " &7"
                                + data.getCurrentTask().getMobType().getName() + " slain"));
                currentMeta.setLore(lore);
            }
            currentMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            current.setItemMeta(currentMeta);
            current.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        }

        // receive slayer task
        final ItemStack receiveTask = new ItemStack(Material.IRON_SWORD);
        final ItemMeta receiveTaskMeta = receiveTask.getItemMeta();
        if (receiveTaskMeta != null) {
            ArrayList<String> lore = new ArrayList<>();
            if (data.getCurrentTask() != null) {
                receiveTaskMeta.setDisplayName(ChatColor.DARK_RED + "Cancel Task");
                lore.add(ChatColor.RED + "Click to cancel your task");
            } else {
                receiveTaskMeta.setDisplayName(ChatColor.YELLOW + "Receive Task");
                lore.add(ChatColor.GRAY + "Click to receive a " + ChatColor.RED + "Slayer Task");
            }
            receiveTaskMeta.setLore(lore);
            receiveTask.setItemMeta(receiveTaskMeta);
        }

        final ChestGui gui = new ChestGui(3, "Slayer Master");

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        final OutlinePane backgroundPane = new OutlinePane(0, 0, 9, 3, Priority.LOWEST);
        backgroundPane.addItem(new GuiItem(background));
        backgroundPane.setRepeat(true);
        gui.addPane(backgroundPane);

        final StaticPane main = new StaticPane(0, 0, 9, 3);
        main.addItem(new GuiItem(profile), 4, 2);
        main.addItem(new GuiItem(shop, event -> SlayerShopMenu.create(data, 0).show(event.getWhoClicked())), 4, 1);
        main.addItem(new GuiItem(monsters, event -> SlayerMonstersMenu.create(data, 0).show(event.getWhoClicked())), 1, 2);
        main.addItem(new GuiItem(receiveTask, event -> {
            event.getWhoClicked().closeInventory();
            if (data.getCurrentTask() != null) {
                data.setCurrentTask(null);
                event.getWhoClicked().sendMessage(Main.inst.getLanguage().taskCancelled());
                return;
            }
            data.receiveTask((Player) event.getWhoClicked());
        }), 7, 1);
        if (data.getCurrentTask() != null)
            main.addItem(new GuiItem(current), 1, 1);

        gui.addPane(main);

        return gui;
    }
}
