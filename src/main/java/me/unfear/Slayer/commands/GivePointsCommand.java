package me.unfear.Slayer.commands;

import me.unfear.Slayer.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GivePointsCommand implements CommandExecutor {
    private final Language language;
    private final SlayerLoader slayerLoader;

    public GivePointsCommand(Language language, SlayerLoader slayerLoader) {
        this.language = language;
        this.slayerLoader = slayerLoader;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(Chat.format("&cInvalid usage! /givepoints <amount> <player> [-s]"));
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Chat.format("&cThat isn't an integer!"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(Chat.format("&cThat player is offline!"));
            return true;
        }


        final PlayerData data = Main.inst.getSlayerLoader().getPlayerData(target.getUniqueId());
        data.setPoints(data.getPoints() + amount);

        if (!(args.length >= 3 && args[2].equalsIgnoreCase("-s"))) {
            sender.sendMessage(Chat.format("&c" + target.getName() + " &7now has &2" + data.getPoints() + " Slayer Points&7."));
        }

        return true;
    }
}
