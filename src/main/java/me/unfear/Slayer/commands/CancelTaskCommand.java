package me.unfear.Slayer.commands;

import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CancelTaskCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "You are not a player, so you must specify a target. /canceltask <player>");
            return true;
        }

        if (args.length > 0) {
            if (sender.hasPermission("slayer.canceltask.others")) {
                player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + args[0] + " is not online!");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have the permission slayer.canceltask.others!");
                return true;
            }
        }

        PlayerData playerData = Main.inst.getSlayerLoader().getPlayerData(player.getUniqueId());
        if (playerData.completedCurrentTask()) {
            sender.sendMessage(ChatColor.RED + player.getName() + " has completed their task, so it can't be cancelled!");
            return true;
        } else if (playerData.getCurrentTask() == null) {
            sender.sendMessage(ChatColor.RED + player.getName() + " has no active Slayer task!");
            return true;
        }

        playerData.setCurrentTask(null);
        sender.sendMessage(ChatColor.GREEN + "Slayer task cancelled!");
        return true;
    }
}
