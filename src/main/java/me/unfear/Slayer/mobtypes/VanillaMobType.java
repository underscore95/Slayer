package me.unfear.Slayer.mobtypes;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class VanillaMobType extends MobType {

	private final EntityType entityType;
	public VanillaMobType(int id, String name, Material material, EntityType entityType) {
		super(id, name, material);
		this.entityType = entityType;
	}
	
	public EntityType getEntityType() {
		return entityType;
	}

	@Override
	public boolean isThis(Entity entity) {
		return entity != null && entity.getType() == this.getEntityType();
	}

	
}
