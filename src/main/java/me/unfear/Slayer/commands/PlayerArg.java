package me.unfear.Slayer.commands;

import me.unfear.Slayer.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class PlayerArg {
    public static @Nullable Player get(CommandSender sender, String[] args, int index, String perm) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (args.length > index && sender.hasPermission(perm)) {
            player = Bukkit.getPlayer(args[index]);
            if (player == null) {
                sender.sendMessage(Main.inst.getLanguage().playerOffline(args[index]));
                return null;
            }
        }
        return player;
    }
}
