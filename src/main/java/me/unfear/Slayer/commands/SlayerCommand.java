package me.unfear.Slayer.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.Slayer;
import me.unfear.Slayer.menus.SlayerMenu;
import me.unfear.Slayer.menus.SlayerRewardMenu;
import org.jetbrains.annotations.NotNull;

public class SlayerCommand implements CommandExecutor {

	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		
		Player target = null;
		if (sender instanceof Player) target = (Player) sender;
		
		if (args.length > 0) {
			target = Bukkit.getPlayer(args[0]);
			if (!sender.hasPermission("slayer.openothers")) {
				sender.sendMessage(ChatColor.RED + "You need the permission slayer.openothers to do this!");
				return false;
			}
			
			if (target == null) {
				sender.sendMessage(ChatColor.RED + args[0] + " is offline!");
				return false;
			}
		}
		
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "/slayer [<player>] [-s]");
			return false;
		}
		
		if (!target.getName().equals(sender.getName()) && args.length > 1 && args[1].equalsIgnoreCase("-s")) sender.sendMessage(ChatColor.GRAY + "Opened the Slayer menu for " + ChatColor.YELLOW + target.getName());
		
		final PlayerData data = Slayer.inst.getSlayerLoader().getPlayerData(target.getUniqueId());
		if (data.completedCurrentTask()) {
			SlayerRewardMenu.create(data).show(target);
		} else {
			SlayerMenu.create(data).show(target);
		}
		
		return true;
	}
}
