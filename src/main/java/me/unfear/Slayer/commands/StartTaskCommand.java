package me.unfear.Slayer.commands;

import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartTaskCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = PlayerArg.get(sender, args, 0, "slayer.starttask.others");
        if (player == null) return true;

        PlayerData data = Main.inst.getSlayerLoader().getPlayerData(player.getUniqueId());
        if (data.getCurrentTask() == null) {
            data.receiveTask(player);
        } else {
            sender.sendMessage(Main.inst.getLanguage().alreadyHasTask());
        }
        return true;
    }
}
