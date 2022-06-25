package me.unfear.Slayer.mobtypes;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class VanillaMobType extends MobType {

	private EntityType entityType;
	public VanillaMobType(int id, String name, EntityType entityType) {
		super(id, name);
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
