package me.unfear.Slayer.commands;

import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import org.bukkit.Bukkit;
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
            sender.sendMessage(Main.inst.getLanguage().cancelTaskConsole());
            return true;
        }

        if (args.length > 0) {
            if (sender.hasPermission("slayer.canceltask.others")) {
                player = Bukkit.getPlayer(args[0]);
                if (player == null) {
                    sender.sendMessage(Main.inst.getLanguage().playerOffline(args[0]));
                    return true;
                }
            } else {
                sender.sendMessage(Main.inst.getLanguage().noPermission("slayer.canceltask.others"));
                return true;
            }
        }

        PlayerData playerData = Main.inst.getSlayerLoader().getPlayerData(player.getUniqueId());
        if (playerData.completedCurrentTask()) {
            sender.sendMessage(Main.inst.getLanguage().tryCancelCompletedTask(player.getName()));
            return true;
        } else if (playerData.getCurrentTask() == null) {
            sender.sendMessage(Main.inst.getLanguage().tryCancelNullTask(player.getName()));
            return true;
        }

        playerData.setCurrentTask(null);
        sender.sendMessage(Main.inst.getLanguage().taskCancelled());
        return true;
    }
}
