package me.unfear.Slayer.commands;

import me.unfear.Slayer.Language;
import me.unfear.Slayer.SlayerLoader;
import me.unfear.Slayer.mobtypes.MobTypeLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadSlayerCommand implements CommandExecutor {
    private final MobTypeLoader mobTypeLoader;
    private final Language language;
    private final SlayerLoader config;

    public ReloadSlayerCommand(MobTypeLoader mobTypeLoader, Language language, SlayerLoader config) {
        this.mobTypeLoader = mobTypeLoader;
        this.language = language;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        language.reloadConfig();
        mobTypeLoader.reloadConfig();
        config.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reloaded Slayer config!");
        return true;
    }
}
