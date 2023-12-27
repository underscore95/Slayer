package me.unfear.Slayer;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Language {

    private final FileConfiguration langConfig;
    public Language() {
        File file = new File(Main.inst.getDataFolder(), "lang.yml");
        Main.inst.getDataFolder().mkdirs();
        if (!file.exists()) {
            Main.inst.saveResource("lang.yml", false);
        }

        langConfig = YamlConfiguration.loadConfiguration(file);
    }

    public String cancelTaskConsole() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("CANCEL_TASK_CONSOLE", "&cYou are not a player, so you must specify a target. /canceltask <player>"));
    }

    public String playerOffline(String player) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("PLAYER_OFFLINE", "&c%player% is offline.")).replace("%player%", player);
    }

    public String noPermission(String permission) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("NO_PERMISSION", "&cYou do not have the permission %permission%")).replace("%permission%", permission);
    }

    public String tryCancelCompletedTask(String player) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("TRY_CANCEL_COMPLETED_TASK", "&c%player% has completed their task, so it cannot be cancelled!")).replace("%player%", player);
    }

    public String tryCancelNullTask(String player) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("TRY_CANCEL_NULL_TASK", "&c%player% has no active Slayer task!")).replace("%player%", player);
    }

    public String taskCancelled() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("TASK_CANCELLED", "&aSlayer task cancelled!"));
    }

    public String openSlayerMenu(String player) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("OPEN_SLAYER_MENU", "&7Opened the Slayer menu for &e%player%")).replace("%player%", player);
    }

    public String shopCost(int points) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("SHOP_COST", "&7Cost: &5%points% Slayer Points")).replace("%points%", String.valueOf(points));
    }

    public String slayerPoints(int points) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("SLAYER_POINTS", "&7Your Slayer Points: &5%points%")).replace("%points%", String.valueOf(points));
    }

    public String nextPage() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("NEXT_PAGE", "&7Next Page"));
    }

    public String previousPage() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("PREVIOUS_PAGE", "&7Previous Page"));
    }

    public String transactionComplete(int points) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("TRANSACTION_COMPLETE", "&6&lTRANSACTION COMPLETE! &7You have spent &5%points% &7Slayer Points.")).replace("%points%", String.valueOf(points));
    }

    public String shopGuiTitle() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("SHOP_GUI_TITLE", "&7Slayer Master | Shop"));
    }

    public String tooExpensive() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("TOO_EXPENSIVE", "&cYou cannot afford this."));
    }

    public String clickToPurchase() {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("CLICK_TO_PURCHASE", "&7Click to purchase."));
    }

    public String slayerTask(int kills, String entity) {
        return ChatColor.translateAlternateColorCodes('&', langConfig.getString("SLAYER_TASK", "&c&lSLAYER TASK &7Slay &f%kills% &7%entity%"))
                .replace("%kills%", String.valueOf(kills))
                .replace("%entity%", entity);
    }
}
