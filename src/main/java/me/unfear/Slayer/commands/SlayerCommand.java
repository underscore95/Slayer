package me.unfear.Slayer.commands;

import me.unfear.Slayer.Chat;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.menus.SlayerMenu;
import me.unfear.Slayer.menus.SlayerRewardMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SlayerCommand implements CommandExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        Player target = null;
        if (sender instanceof Player) target = (Player) sender;

        if (args.length > 0) {
            target = Bukkit.getPlayer(args[0]);
            if (!sender.hasPermission(Chat.format("slayer.openothers"))) {
                sender.sendMessage(Chat.format("&cYou need the permission slayer.openothers to do this!"));
                return false;
            }

            if (target == null) {
                sender.sendMessage(Chat.format("&c" + args[0] + " is offline!"));
                return false;
            }
        }

        if (target == null) {
            sender.sendMessage(Chat.format("&c/slayer [<player>] [-s]"));
            return false;
        }

        if (!target.getName().equals(sender.getName()) && args.length > 1 && args[1].equalsIgnoreCase("-s")) {
            sender.sendMessage(Chat.format("&7Opened the Slayer menu for &e" + target.getName()));
        }

        final PlayerData data = Main.inst.getSlayerLoader().getPlayerData(target.getUniqueId());
        if (data.completedCurrentTask()) {
            SlayerRewardMenu.create(target, data).show(target);
        } else {
            SlayerMenu.create(target, data).show(target);
        }

        return true;
    }
}
