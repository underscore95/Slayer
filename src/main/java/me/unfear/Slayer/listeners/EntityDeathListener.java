package me.unfear.Slayer.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTEntity;
import me.unfear.Slayer.PlayerData;
import me.unfear.Slayer.Slayer;
import me.unfear.Slayer.SlayerLoader;

public class EntityDeathListener implements Listener {

	@EventHandler
	void onDeath(EntityDeathEvent e) {
		if (e.getEntity().getKiller() == null || e.getEntity() instanceof Player) return;
		
		Player player = e.getEntity().getKiller();
		
		Entity entity = e.getEntity();
		NBTCompound nbt = new NBTEntity(entity).getPersistentDataContainer();
		Boolean fromSpawner = nbt.getBoolean(SpawnerSpawnListener.NBT_TAG);
		
		final SlayerLoader loader = Slayer.inst.getSlayerLoader();
		if (fromSpawner && !loader.isAllowSpawners()) return;
		
		final PlayerData data = loader.getPlayerData(player.getUniqueId());
		if (data.getCurrentTask() == null || data.getCurrentTask().getMobType() != entity.getType()) return;
		data.setKills(data.getKills() + 1);
	}
}
