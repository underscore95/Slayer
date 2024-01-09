package me.unfear.Slayer.commands;

import me.unfear.Slayer.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SlayerBuyCommand implements CommandExecutor {
    private final Language language;
    private final SlayerLoader slayerLoader;

    public SlayerBuyCommand(Language language, SlayerLoader slayerLoader) {
        this.language = language;
        this.slayerLoader = slayerLoader;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = PlayerArg.get(sender, args, 1, "slayer.buy.others");
        if (player == null) return true;

        if (args.length < 1) {
            sender.sendMessage(language.shopUsage());
            return true;
        }

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(language.shopUsage());
            return true;
        }

        ShopItem shopItem = slayerLoader.getShopItem(id);
        if (shopItem == null) {
            sender.sendMessage(language.invalidShopItem());
            return true;
        }

        final PlayerData data = Main.inst.getSlayerLoader().getPlayerData(player.getUniqueId());

        // can player buy more of this item?
        if (data.getShopItemsPurchased().getOrDefault(shopItem.getId(), 0) >= shopItem.getPurchases()
                && shopItem.getPurchases() != -1) {
            sender.sendMessage(language.shopOutOfStock());
            return true;
        }
        // can player afford this item?
        if (data.getPoints() < shopItem.getCost()) {
            sender.sendMessage(language.tooExpensive());
            return true;
        }

        // player is able to buy it, so buy it
        data.setPoints(data.getPoints() - shopItem.getCost());
        data.getShopItemsPurchased().put(shopItem.getId(), data.getShopItemsPurchased().getOrDefault(shopItem.getId(), 0) + 1);
        for (String cmd : shopItem.getCommands()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }

        sender.sendMessage(Main.inst.getLanguage().transactionComplete(shopItem.getCost()));
        return true;
    }
}
