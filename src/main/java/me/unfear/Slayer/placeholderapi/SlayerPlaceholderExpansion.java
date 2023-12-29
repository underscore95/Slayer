package me.unfear.Slayer.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.SlayerTask;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class SlayerPlaceholderExpansion extends PlaceholderExpansion {

    private final PluginDescriptionFile pluginDescriptionFile;

    public SlayerPlaceholderExpansion(PluginDescriptionFile pluginDescriptionFile) {
        this.pluginDescriptionFile = pluginDescriptionFile;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "slayer";
    }

    @Override
    public @NotNull String getAuthor() {
        return pluginDescriptionFile.getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return pluginDescriptionFile.getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null) return null;

        params = params.toLowerCase();

        PlayerData data = Main.inst.getSlayerLoader().getPlayerData(player.getUniqueId());
        SlayerTask task = data.getCurrentTask();

        switch (params) {
            case "points":
                return String.valueOf(data.getPoints());
            case "tasks_completed":
                return String.valueOf(data.getTasksCompleted());
            case "kills":
                return String.valueOf(data.getKills());
            case "required_kills":
                return task == null ? "N/A" : String.valueOf(task.getKills());
            case "current_task_name":
                return task == null ? "N/A" : task.getName();
            case "current_task_entity":
                return task == null ? "N/A" : task.getMobType().getName();
            default:
                return null; // Placeholder is unknown by the Expansion
        }
    }
}
