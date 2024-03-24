package me.unfear.Slayer.commands;

import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CancelTaskCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = PlayerArg.get(sender, args, 0, "slayer.canceltask.others");
        if (player == null) return true;

        if (!Main.inst.getSlayerLoader().isCancelTask()) {
            player.sendMessage(ChatColor.RED + "Cancelling tasks is disabled in the config.yml!");
            return true;
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
