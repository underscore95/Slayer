package me.unfear.Slayer.menus;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane.Priority;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.unfear.Slayer.Language;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SlayerMenu {
    private static final Language lang = Main.inst.getLanguage();

    public static ChestGui create(Player player, PlayerData data) {
        final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        final ItemMeta backgroundMeta = background.getItemMeta();
        if (backgroundMeta != null) {
            backgroundMeta.setDisplayName(ChatColor.RED + " ");
            background.setItemMeta(backgroundMeta);
        }

        // display some information
        String currentTask = (data.getCurrentTask() == null ? "None" : data.getCurrentTask().getName());
        final ItemStack profile = new ItemStack(Material.BIRCH_SIGN);
        final ItemMeta profileMeta = profile.getItemMeta();
        if (profileMeta != null) {
            profileMeta.setDisplayName(lang.slayerProfileName());
            profileMeta.setLore(lang.profileLore(data.getTasksCompleted(), data.getPoints(), currentTask));
            profile.setItemMeta(profileMeta);
        }

        // shop
        final ItemStack shop = new ItemStack(Material.GOLD_NUGGET);
        final ItemMeta shopMeta = shop.getItemMeta();
        if (shopMeta != null) {
            shopMeta.setDisplayName(lang.shopName());
            shopMeta.setLore(lang.shopLore());
            shop.setItemMeta(shopMeta);
        }

        // monsters
        final ItemStack monsters = new ItemStack(Material.ZOMBIE_HEAD);
        final ItemMeta monstersMeta = monsters.getItemMeta();
        if (monstersMeta != null) {
            monstersMeta.setDisplayName(lang.monstersName());
            monstersMeta.setLore(lang.monstersLore());
            monsters.setItemMeta(monstersMeta);
        }

        // current slayer task
        final ItemStack current = new ItemStack(Material.ROTTEN_FLESH);
        final ItemMeta currentMeta = current.getItemMeta();
        if (currentMeta != null) {
            currentMeta.setDisplayName(lang.currentTaskName());
            if (data.getCurrentTask() != null) {
                currentMeta.setLore(lang.currentTaskProgress(data.getCurrentTask().getName(), data.getCurrentTask().getDescription(),
                        data.getKills(), data.getCurrentTask().getKills(), data.getCurrentTask().getMobType().getName()));
            }
            currentMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            current.setItemMeta(currentMeta);
            current.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
        }

        // receive slayer task
        final ItemStack receiveTask = new ItemStack(Material.IRON_SWORD);
        final ItemMeta receiveTaskMeta = receiveTask.getItemMeta();
        if (receiveTaskMeta != null) {
            receiveTaskMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            if (data.getCurrentTask() != null) {
                receiveTaskMeta.setDisplayName(lang.cancelTaskName());
                receiveTaskMeta.setLore(lang.cancelTaskLore());
            } else {
                receiveTaskMeta.setDisplayName(lang.receiveTaskName());
                receiveTaskMeta.setLore(lang.receiveTaskLore());
            }
            receiveTask.setItemMeta(receiveTaskMeta);
        }

        // back
        final ItemStack backButton = new ItemStack(Material.ARROW);
        final ItemMeta backButtonMeta = backButton.getItemMeta();
        if (backButtonMeta != null) {
            backButtonMeta.setDisplayName(lang.guiBackName());
            backButtonMeta.setLore(lang.guiBackLore());
        }
        backButton.setItemMeta(backButtonMeta);

        final ChestGui gui = new ChestGui(3, lang.guiTitle());

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        final OutlinePane backgroundPane = new OutlinePane(0, 0, 9, 3, Priority.LOWEST);
        backgroundPane.addItem(new GuiItem(background));
        backgroundPane.setRepeat(true);
        gui.addPane(backgroundPane);

        final StaticPane main = new StaticPane(0, 0, 9, 3);
        String backButtonCommand = Main.inst.getSlayerLoader().getMainBackCommand(player.getName());
        if (!backButtonCommand.equalsIgnoreCase("none")) {
            main.addItem(
                    new GuiItem(backButton, event -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), backButtonCommand)), 0, 2);
        }

        main.addItem(new GuiItem(profile), 4, 2);
        main.addItem(new GuiItem(shop, event -> SlayerShopMenu.create(player, data, 0).show(event.getWhoClicked())), 4, 1);
        main.addItem(new GuiItem(monsters, event -> SlayerMonstersMenu.create(player, data, 0).show(event.getWhoClicked())), 1, 2);

        if (data.getCurrentTask() == null || Main.inst.getSlayerLoader().isCancelTask()) {
            main.addItem(new GuiItem(receiveTask, event -> {
                event.getWhoClicked().closeInventory();
                if (data.getCurrentTask() != null) {
                    data.setCurrentTask(null);
                    event.getWhoClicked().sendMessage(Main.inst.getLanguage().taskCancelled());
                    return;
                }
                data.receiveTask((Player) event.getWhoClicked());
            }), 7, 1);
        }

        if (data.getCurrentTask() != null)
            main.addItem(new GuiItem(current), 1, 1);

        gui.addPane(main);

        return gui;
    }
}
