package me.unfear.Slayer.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTEntity;

public class SpawnerSpawnListener implements Listener {
	
	public static final String NBT_TAG = "Slayer.fromSpawner";

	@EventHandler
	void onSpawn(SpawnerSpawnEvent e) {
		Entity entity = e.getEntity();
		NBTCompound nbt = new NBTEntity(entity).getPersistentDataContainer();
		nbt.setBoolean(NBT_TAG, true);
	}
}
