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
import me.unfear.Slayer.mobtypes.MobType;

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
		
		for (MobType mobType : Slayer.inst.getMobTypeLoader().getMobTypes()) {
			if (!mobType.isThis(entity)) continue;
			data.incrementEntityKills(mobType.getId());
			break;
		}
		
		if (data.getCurrentTask() == null || !data.getCurrentTask().getMobType().isThis(entity)) return;
		data.setKills(data.getKills() + 1);
	}
}
