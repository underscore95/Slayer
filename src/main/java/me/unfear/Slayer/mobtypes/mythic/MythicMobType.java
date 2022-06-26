package me.unfear.Slayer.mobtypes.mythic;

import org.bukkit.Material;
import org.bukkit.entity.Entity;

import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.mobs.MobExecutor;
import me.unfear.Slayer.mobtypes.MobType;

public class MythicMobType extends MobType {

	private String mythicMobName;
	public MythicMobType(int id, String name, Material material, String mythicMobName) {
		super(id, name, material);
		this.mythicMobName = mythicMobName;
	}

	@Override
	public boolean isThis(Entity entity) {
		final MobExecutor mobManager = MythicMobsLoader.mythicMobs.getMobManager();
		final ActiveMob mob = mobManager.getMythicMobInstance(entity);
		return mob != null && mob.getType().getInternalName().equalsIgnoreCase(mythicMobName);
	}

}
