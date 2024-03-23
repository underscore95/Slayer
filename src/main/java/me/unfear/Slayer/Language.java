package me.unfear.Slayer;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Language {

    private final FileConfiguration langConfig;
    private final Map<String, String> defaults = Map.ofEntries(
            Map.entry("PLAYER_OFFLINE", "&c%player% is offline."),
            Map.entry("TRY_CANCEL_COMPLETED_TASK", "&c%player% has completed their task, so it cannot be cancelled!"),
            Map.entry("TRY_CANCEL_NULL_TASK", "&c%player% has no active Slayer task!"),
            Map.entry("TASK_CANCELLED", "&aSlayer task cancelled!"),
            Map.entry("SHOP_COST", "&7Cost: &5%points% Slayer Points"),
            Map.entry("SLAYER_POINTS", "&7Your Slayer Points: &5%points%"),
            Map.entry("NEXT_PAGE", "&7Next Page"),
            Map.entry("PREVIOUS_PAGE", "&7Previous Page"),
            Map.entry("TRANSACTION_COMPLETE", "&6&lTRANSACTION COMPLETE! &7You have spent &5%points% &7Slayer Points."),
            Map.entry("SHOP_GUI_TITLE", "&7Slayer Master | Shop"),
            Map.entry("TOO_EXPENSIVE", "&cYou cannot afford this."),
            Map.entry("CLICK_TO_PURCHASE", "&7Click to purchase."),
            Map.entry("SLAYER_TASK", "&c&lSLAYER TASK &7Slay &f%kills% &7%entity%"),
            Map.entry("SHOP_USAGE", "&c/slayerbuy <item>"),
            Map.entry("INVALID_SHOP_ITEM", "&cThat item isn't in the shop!"),
            Map.entry("SHOP_OUT_OF_STOCK", "&cYou can't buy more of this item!"),
            Map.entry("REWARD_CLAIMED", "&6&lREWARDS CLAIMED! &7You have collected your reward."),
            Map.entry("TASK_INCOMPLETE", "&cRewards cannot be claimed for an incomplete task."),
            Map.entry("ALREADY_HAS_TASK", "&cCannot start two tasks simultaneously."),
            Map.entry("GUI_SLAYER_PROFILE_NAME", "&6Slayer Profile"),
            Map.entry("GUI_PROFILE_LORE", "&7Tasks Completed: &2%amount%%nl%&7Slayer Points: &5%points%%nl%&7Current Task: &4%task%"),
            Map.entry("GUI_SHOP_NAME", "&6Slayer Shop"),
            Map.entry("GUI_SHOP_LORE", "&7Spend your hard-earned &2Slayer Points"),
            Map.entry("GUI_MONSTERS_NAME", "&cMonsters Slain"),
            Map.entry("GUI_MONSTERS_LORE", "&7How many monsters have you ended?"),
            Map.entry("GUI_CURRENT_TASK_NAME", "&eCurrent Slayer Task"),
            Map.entry("GUI_CURRENT_TASK_PROGRESS", "&7Progress: &f%kills% &8/ &f%required% &7%mob% &7slain"),
            Map.entry("GUI_CANCEL_TASK_NAME", "&4Cancel Task"),
            Map.entry("GUI_CANCEL_TASK_LORE", "&cClick to cancel your task"),
            Map.entry("GUI_RECEIVE_TASK_NAME", "&eReceive Task"),
            Map.entry("GUI_RECEIVE_TASK_LORE", "&7Click to receive a &4Slayer Task"),
            Map.entry("GUI_TITLE", "Slayer Master"),
            Map.entry("MONSTER_GUI_MONSTER_NAME", "&f%mob%"),
            Map.entry("MONSTER_GUI_MONSTER_LORE", "&7Number Defeated: &c%kills%"),
            Map.entry("MONSTERS_GUI_PREV_PAGE_NAME", "&7Previous Page"),
            Map.entry("MONSTERS_GUI_NEXT_PAGE_NAME", "&7Next Page"),
            Map.entry("MONSTERS_GUI_BACK_NAME", "&7Back"),
            Map.entry("MONSTERS_GUI_BACK_LORE", "&7Go back to the &4Slayer Master"),
            Map.entry("MONSTER_GUI_TITLE", "Slayer Master"),
            Map.entry("REWARD_GUI_REWARD_NAME", "&aCollect Reward"),
            Map.entry("REWARD_GUI_REWARD_LORE", "&7You have completed your task.%nl%%nl%&7Click to collect your rewards:%nl%&5%points% slayer points"),
            Map.entry("REWARD_GUI_TITLE", "Slayer Master")
    );

    private final File langFile;

    public Language() {
        langFile = new File(Main.inst.getDataFolder(), "lang.yml");
        Main.inst.getDataFolder().mkdirs();
        if (!langFile.exists()) {
            Main.inst.saveResource("lang.yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(langFile);

        defaults.keySet().forEach(this::get);
    }

    private String get(String key) {
        String message = langConfig.getString(key);
        if (message == null) {
            message = defaults.getOrDefault(key, "");
            langConfig.set(key, message);
            try {
                langConfig.save(langFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public String playerOffline(String player) {
        return ChatColor.translateAlternateColorCodes('&', get("PLAYER_OFFLINE").replace("%player%", player));
    }

    public String tryCancelCompletedTask(String player) {
        return ChatColor.translateAlternateColorCodes('&', get("TRY_CANCEL_COMPLETED_TASK").replace("%player%", player));
    }

    public String tryCancelNullTask(String player) {
        return ChatColor.translateAlternateColorCodes('&', get("TRY_CANCEL_NULL_TASK").replace("%player%", player));
    }

    public String taskCancelled() {
        return ChatColor.translateAlternateColorCodes('&', get("TASK_CANCELLED"));
    }

    public String shopCost(int points) {
        return ChatColor.translateAlternateColorCodes('&', get("SHOP_COST").replace("%points%", String.valueOf(points)));
    }

    public String slayerPoints(int points) {
        return ChatColor.translateAlternateColorCodes('&', get("SLAYER_POINTS").replace("%points%", String.valueOf(points)));
    }

    public String nextPage() {
        return ChatColor.translateAlternateColorCodes('&', get("NEXT_PAGE"));
    }

    public String previousPage() {
        return ChatColor.translateAlternateColorCodes('&', get("PREVIOUS_PAGE"));
    }

    public String transactionComplete(int points) {
        return ChatColor.translateAlternateColorCodes('&', get("TRANSACTION_COMPLETE").replace("%points%", String.valueOf(points)));
    }

    public String shopGuiTitle() {
        return ChatColor.translateAlternateColorCodes('&', get("SHOP_GUI_TITLE"));
    }

    public String tooExpensive() {
        return ChatColor.translateAlternateColorCodes('&', get("TOO_EXPENSIVE"));
    }

    public String clickToPurchase() {
        return ChatColor.translateAlternateColorCodes('&', get("CLICK_TO_PURCHASE"));
    }

    public String slayerTask(int kills, String entity) {
        return ChatColor.translateAlternateColorCodes('&', get("SLAYER_TASK")
                .replace("%kills%", String.valueOf(kills))
                .replace("%entity%", entity));
    }

    public String shopUsage() {
        return ChatColor.translateAlternateColorCodes('&', get("SHOP_USAGE"));
    }

    public String invalidShopItem() {
        return ChatColor.translateAlternateColorCodes('&', get("INVALID_SHOP_ITEM"));
    }

    public String shopOutOfStock() {
        return ChatColor.translateAlternateColorCodes('&', get("SHOP_OUT_OF_STOCK"));
    }

    public String rewardClaimed() {
        return ChatColor.translateAlternateColorCodes('&', get("REWARD_CLAIMED"));
    }

    public String taskNotComplete() {        return ChatColor.translateAlternateColorCodes('&', get("TASK_INCOMPLETE"));
    }

    public String alreadyHasTask() {return ChatColor.translateAlternateColorCodes('&', get("ALREADY_HAS_TASK"));
    }
}
