package me.unfear.Slayer.listeners;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import me.unfear.Slayer.Language;
import me.unfear.Slayer.Main;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.SlayerLoader;
import me.unfear.Slayer.mobtypes.MobType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {
    private final Language language;
    private final SlayerLoader config;

    public EntityDeathListener(Language language, SlayerLoader config) {
        this.language = language;
        this.config = config;
    }

    private void sendActionBar(Player player, PlayerData data) {
        if (!config.isActionBarEnabled()) return;
        int kills = data.getKills();
        int required = data.getCurrentTask().getKills();
        if (required < kills) return;
        String text = language.actionBar(kills, required);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(text));

    }

    @EventHandler
    void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null || e.getEntity() instanceof Player) return;

        Player player = e.getEntity().getKiller();

        Entity entity = e.getEntity();
        NBTCompound nbt = new NBTEntity(entity).getPersistentDataContainer();
        Boolean fromSpawner = nbt.getBoolean(SpawnerSpawnListener.NBT_TAG);

        final SlayerLoader loader = Main.inst.getSlayerLoader();
        if (fromSpawner && !loader.isAllowSpawners()) return;

        final PlayerData data = loader.getPlayerData(player.getUniqueId());

        for (MobType mobType : Main.inst.getMobTypeLoader().getMobTypes()) {
            if (!mobType.isThis(entity)) continue;
            data.incrementEntityKills(mobType.getId());
            break;
        }

        if (data.getCurrentTask() == null || !data.getCurrentTask().getMobType().isThis(entity)) return;
        data.setKills(data.getKills() + 1);
        sendActionBar(player, data);
    }
}
